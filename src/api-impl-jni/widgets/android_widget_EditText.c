#include <gtk/gtk.h>

#include "../defines.h"
#include "../util.h"

#include "WrapperWidget.h"

#include "../generated_headers/android_widget_EditText.h"

/* single-line EditTexts are a GtkText; multi-line ones (inputType textMultiLine, maxLines/lines > 1)
   a word-wrapping GtkTextView that grows with its text, with the hint as an overlay label
   (GtkTextView has no placeholder) */

static void update_placeholder(GtkTextBuffer *buffer, GtkWidget *placeholder)
{
	gtk_widget_set_visible(placeholder, gtk_text_buffer_get_char_count(buffer) == 0 && gtk_label_get_text(GTK_LABEL(placeholder))[0]);
}

/* GtkTextView reports the height of its lazily validated layout (a few px before the first
   validation idle); measure the wrapped text with Pango instead, at least one line */
G_DECLARE_FINAL_TYPE(AtlTextView, atl_text_view, ATL, TEXT_VIEW, GtkTextView)
struct _AtlTextView {
	GtkTextView parent_instance;
};
G_DEFINE_TYPE(AtlTextView, atl_text_view, GTK_TYPE_TEXT_VIEW)

static void atl_text_view_measure(GtkWidget *widget, GtkOrientation orientation, int for_size, int *minimum, int *natural, int *minimum_baseline, int *natural_baseline)
{
	GTK_WIDGET_CLASS(atl_text_view_parent_class)->measure(widget, orientation, for_size, minimum, natural, minimum_baseline, natural_baseline);
	GtkTextView *view = GTK_TEXT_VIEW(widget);
	GtkTextBuffer *buffer = gtk_text_view_get_buffer(view);
	GtkTextIter start, end;
	gtk_text_buffer_get_bounds(buffer, &start, &end);
	char *text = gtk_text_buffer_get_text(buffer, &start, &end, FALSE);
	PangoLayout *layout = gtk_widget_create_pango_layout(widget, text);
	g_free(text);
	int hmargin = gtk_text_view_get_left_margin(view) + gtk_text_view_get_right_margin(view);
	int width, height;
	if (orientation == GTK_ORIENTATION_HORIZONTAL) {
		/* natural = the text on one line (callers cap it at their width), minimum = the chain's */
		pango_layout_get_pixel_size(layout, &width, NULL);
		*natural = MAX(*natural, width + hmargin + 1 /* cursor */);
	} else {
		if (for_size > hmargin) {
			pango_layout_set_width(layout, (for_size - hmargin) * PANGO_SCALE);
			pango_layout_set_wrap(layout, PANGO_WRAP_WORD_CHAR);
		}
		pango_layout_get_pixel_size(layout, NULL, &height);
		height += gtk_text_view_get_top_margin(view) + gtk_text_view_get_bottom_margin(view);
		/* not MAX with the chain's: that is the last validated layout, stale right after an edit */
		*minimum = *natural = height;
	}
	g_object_unref(layout);
	*minimum_baseline = *natural_baseline = -1;
}

static GtkSizeRequestMode atl_text_view_get_request_mode(GtkWidget *widget)
{
	return GTK_SIZE_REQUEST_HEIGHT_FOR_WIDTH;
}

static void atl_text_view_class_init(AtlTextViewClass *class)
{
	GTK_WIDGET_CLASS(class)->measure = atl_text_view_measure;
	GTK_WIDGET_CLASS(class)->get_request_mode = atl_text_view_get_request_mode;
}

static void atl_text_view_init(AtlTextView *view)
{
}

/* the Java side lays out, so a growing text has to ask it for a new pass */
static void request_java_layout(GtkTextBuffer *buffer, GtkWidget *view)
{
	GtkWidget *wrapper = gtk_widget_get_parent(view);
	if (!wrapper || !WRAPPER_IS_WIDGET(wrapper) || !WRAPPER_WIDGET(wrapper)->jobj)
		return;
	JNIEnv *env = get_jni_env();
	jobject jobj = WRAPPER_WIDGET(wrapper)->jobj;
	(*env)->CallVoidMethod(env, jobj, _METHOD(_CLASS(jobj), "requestLayout", "()V"));
	if ((*env)->ExceptionCheck(env))
		(*env)->ExceptionDescribe(env);
}

JNIEXPORT jlong JNICALL Java_android_widget_EditText_native_1constructor_1multiline(JNIEnv *env, jobject this, jobject context, jobject attrs, jboolean multiline)
{
	GtkWidget *wrapper = g_object_ref(wrapper_widget_new());
	GtkWidget *child;
	if (multiline) {
		child = g_object_new(atl_text_view_get_type(), NULL);
		gtk_text_view_set_wrap_mode(GTK_TEXT_VIEW(child), GTK_WRAP_WORD_CHAR);
		gtk_text_view_set_accepts_tab(GTK_TEXT_VIEW(child), FALSE);
		gtk_widget_add_css_class(child, "ATL-edittext");
		GtkWidget *placeholder = gtk_label_new("");
		gtk_widget_add_css_class(placeholder, "ATL-edittext-hint");
		gtk_widget_set_can_target(placeholder, FALSE);
		gtk_widget_set_visible(placeholder, FALSE);
		gtk_text_view_add_overlay(GTK_TEXT_VIEW(child), placeholder, 0, 0);
		g_object_set_data(G_OBJECT(child), "atl-placeholder", placeholder);
		g_signal_connect(gtk_text_view_get_buffer(GTK_TEXT_VIEW(child)), "changed", G_CALLBACK(update_placeholder), placeholder);
		g_signal_connect(gtk_text_view_get_buffer(GTK_TEXT_VIEW(child)), "changed", G_CALLBACK(request_java_layout), child);
	} else {
		child = gtk_text_new();
	}
	wrapper_widget_set_child(WRAPPER_WIDGET(wrapper), child);
	/* app onDraw overrides run (WhatsApp paints its composer hint itself); touch stays with GTK */
	wrapper_widget_set_jobject_drawing(WRAPPER_WIDGET(wrapper), env, this);
	return _INTPTR(child);
}

/* returns a newly allocated string */
static char *edittext_get_text(GtkWidget *widget)
{
	if (GTK_IS_TEXT_VIEW(widget)) {
		GtkTextBuffer *buffer = gtk_text_view_get_buffer(GTK_TEXT_VIEW(widget));
		GtkTextIter start, end;
		gtk_text_buffer_get_bounds(buffer, &start, &end);
		return gtk_text_buffer_get_text(buffer, &start, &end, FALSE);
	}
	return g_strdup(gtk_editable_get_text(GTK_EDITABLE(widget)));
}

JNIEXPORT jstring JNICALL Java_android_widget_EditText_native_1getText(JNIEnv *env, jobject this, jlong widget_ptr)
{
	char *text = edittext_get_text(_PTR(widget_ptr));
	jstring ret = _JSTRING(text);
	g_free(text);
	return ret;
}

struct changed_callback_data {
	jobject this;
	jobject listener;
	jmethodID listener_method;
	jmethodID getText;
};

static void notify_text_changed(const char *text_utf8, jobject listener)
{
	JNIEnv *env = get_jni_env();

	jstring text = _JSTRING(text_utf8);
	jclass spannable_string_builder = (*env)->FindClass(env, "android/text/SpannableStringBuilder");
	jmethodID spannable_string_builder_constructor = _METHOD(spannable_string_builder, "<init>", "(Ljava/lang/CharSequence;)V");
	jobject text_obj = (*env)->NewObject(env, spannable_string_builder, spannable_string_builder_constructor, text);
	jmethodID onTextChanged = _METHOD(_CLASS(listener), "onTextChanged", "(Ljava/lang/CharSequence;III)V");
	// Java counts UTF-16 units, not UTF-8 bytes: strlen() overshoots on any non-ASCII text
	(*env)->CallVoidMethod(env, listener, onTextChanged, text_obj, 0, 0, (*env)->GetStringLength(env, text));
	if ((*env)->ExceptionCheck(env))
		(*env)->ExceptionDescribe(env);
	jmethodID listener_method = _METHOD(_CLASS(listener), "afterTextChanged", "(Landroid/text/Editable;)V");
	(*env)->CallVoidMethod(env, listener, listener_method, text_obj);
	if ((*env)->ExceptionCheck(env))
		(*env)->ExceptionDescribe(env);
}

static void changed_cb(GtkEditable *self, jobject listener)
{
	notify_text_changed(gtk_editable_get_text(self), listener);
}

static void buffer_changed_cb(GtkTextBuffer *buffer, jobject listener)
{
	GtkTextIter start, end;
	gtk_text_buffer_get_bounds(buffer, &start, &end);
	char *text = gtk_text_buffer_get_text(buffer, &start, &end, FALSE);
	notify_text_changed(text, listener);
	g_free(text);
}

JNIEXPORT void JNICALL Java_android_widget_EditText_native_1addTextChangedListener(JNIEnv *env, jobject this, jlong widget_ptr, jobject listener)
{
	GtkWidget *widget = _PTR(widget_ptr);
	listener = _REF(listener);

	GList *listeners = g_object_get_data(G_OBJECT(widget), "text_changed_listeners");
	listeners = g_list_append(listeners, listener);
	g_object_set_data(G_OBJECT(widget), "text_changed_listeners", listeners);
	if (GTK_IS_TEXT_VIEW(widget))
		g_signal_connect(gtk_text_view_get_buffer(GTK_TEXT_VIEW(widget)), "changed", G_CALLBACK(buffer_changed_cb), listener);
	else
		g_signal_connect(widget, "changed", G_CALLBACK(changed_cb), listener);
}

JNIEXPORT void JNICALL Java_android_widget_EditText_native_1removeTextChangedListener(JNIEnv *env, jobject this, jlong widget_ptr, jobject listener)
{
	GtkWidget *widget = _PTR(widget_ptr);

	GList *listeners = g_object_get_data(G_OBJECT(widget), "text_changed_listeners");
	GList *l;
	for (l = listeners; l != NULL; l = l->next) {
		if ((*env)->IsSameObject(env, l->data, listener)) {
			if (GTK_IS_TEXT_VIEW(widget))
				g_signal_handlers_disconnect_by_func(gtk_text_view_get_buffer(GTK_TEXT_VIEW(widget)), buffer_changed_cb, l->data);
			else
				g_signal_handlers_disconnect_by_func(widget, changed_cb, l->data);
			_UNREF(l->data);
			listeners = g_list_delete_link(listeners, l);
			break;
		}
	}
	g_object_set_data(G_OBJECT(widget), "text_changed_listeners", listeners);
}

#define IME_ACTION_SEARCH 3
#define KEYCODE_ENTER     66

static void on_activate(GtkText *gtk_text, struct changed_callback_data *d)
{
	JNIEnv *env = get_jni_env();

	jobject key_event = (*env)->NewObject(env, handle_cache.key_event.class, handle_cache.key_event.constructor, (jlong)0, (jlong)0, IME_ACTION_SEARCH, KEYCODE_ENTER, 0, 0);
	(*env)->CallBooleanMethod(env, d->listener, d->listener_method, d->this, 0, key_event);
	if ((*env)->ExceptionCheck(env))
		(*env)->ExceptionDescribe(env);
}

JNIEXPORT void JNICALL Java_android_widget_EditText_native_1setOnEditorActionListener(JNIEnv *env, jobject this, jlong widget_ptr, jobject listener)
{
	GtkWidget *widget = _PTR(widget_ptr);

	// Enter in a multi-line field inserts a newline, as on Android
	if (!listener || !GTK_IS_TEXT(widget))
		return;

	struct changed_callback_data *callback_data = malloc(sizeof(struct changed_callback_data));
	callback_data->this = _WEAK_REF(this);
	callback_data->listener = _REF(listener);
	callback_data->listener_method = _METHOD(_CLASS(listener), "onEditorAction", "(Landroid/widget/TextView;ILandroid/view/KeyEvent;)Z");

	g_signal_handlers_disconnect_matched(widget, G_SIGNAL_MATCH_FUNC, 0, 0, NULL, on_activate, NULL);
	g_signal_connect(widget, "activate", G_CALLBACK(on_activate), callback_data);
}

JNIEXPORT void JNICALL Java_android_widget_EditText_native_1setText(JNIEnv *env, jobject this, jlong widget_ptr, jstring text_jstr)
{
	GtkWidget *widget = _PTR(widget_ptr);
	const char *text = (*env)->GetStringUTFChars(env, text_jstr, NULL);
	jsize length = (*env)->GetStringUTFLength(env, text_jstr);
	if (GTK_IS_TEXT_VIEW(widget))
		gtk_text_buffer_set_text(gtk_text_view_get_buffer(GTK_TEXT_VIEW(widget)), text, length);
	else
		gtk_entry_buffer_set_text(gtk_text_get_buffer(GTK_TEXT(widget)), text, length);
	(*env)->ReleaseStringUTFChars(env, text_jstr, text);
}

JNIEXPORT void JNICALL Java_android_widget_EditText_native_1setHint(JNIEnv *env, jobject this, jlong widget_ptr, jstring text_jstr)
{
	GtkWidget *widget = _PTR(widget_ptr);
	const char *text = (*env)->GetStringUTFChars(env, text_jstr, NULL);
	if (GTK_IS_TEXT_VIEW(widget)) {
		GtkWidget *placeholder = g_object_get_data(G_OBJECT(widget), "atl-placeholder");
		gtk_label_set_text(GTK_LABEL(placeholder), text);
		update_placeholder(gtk_text_view_get_buffer(GTK_TEXT_VIEW(widget)), placeholder);
	} else {
		gtk_text_set_placeholder_text(GTK_TEXT(widget), text);
	}
	(*env)->ReleaseStringUTFChars(env, text_jstr, text);
}

JNIEXPORT jstring JNICALL Java_android_widget_EditText_native_1getHint(JNIEnv *env, jobject this, jlong widget_ptr)
{
	GtkWidget *widget = _PTR(widget_ptr);
	const char *text;
	if (GTK_IS_TEXT_VIEW(widget))
		text = gtk_label_get_text(GTK_LABEL(g_object_get_data(G_OBJECT(widget), "atl-placeholder")));
	else
		text = gtk_text_get_placeholder_text(GTK_TEXT(widget));
	return _JSTRING(text);
}
