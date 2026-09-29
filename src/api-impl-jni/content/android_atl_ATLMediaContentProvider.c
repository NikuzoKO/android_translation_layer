#include <gtk/gtk.h>
#include <stdbool.h>

#include "../defines.h"
#include "../util.h"
#include "../generated_headers/android_atl_ATLMediaContentProvider.h"

extern GtkWindow *window;

static void file_dialog_callback(GObject *dialog, GAsyncResult *res, gpointer user_data)
{
	GFile *file = gtk_file_dialog_open_finish(GTK_FILE_DIALOG(dialog), res, NULL);
	JNIEnv *env = get_jni_env();
	jobject this = (jobject)user_data;
	char *path = file ? g_file_get_path(file) : NULL; // NULL when the dialog was cancelled
	(*env)->CallVoidMethod(env, this, _METHOD(_CLASS(this), "setSelectedFile", "(Ljava/lang/String;)V"), path ? _JSTRING(path) : NULL);
	g_free(path);
	if (file)
		g_object_unref(file);
	_UNREF(this);
}

JNIEXPORT void JNICALL Java_android_atl_ATLMediaContentProvider_native_1open_1media_1folder(JNIEnv *env, jobject this)
{
	GtkFileDialog *dialog = gtk_file_dialog_new();
	gtk_file_dialog_set_title(GTK_FILE_DIALOG(dialog), "Open Media Folder");
	gtk_file_dialog_set_modal(GTK_FILE_DIALOG(dialog), TRUE);
	gtk_file_dialog_open(dialog, window, NULL, file_dialog_callback, _REF(this));
}
