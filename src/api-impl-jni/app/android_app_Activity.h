#include <jni.h>
#include <stdbool.h>

void activity_start(JNIEnv *env, jobject activity_object);
void activity_window_ready(void);
void activity_close_all(void);
void current_activity_back_pressed(void);

/* background mode (ATL_BACKGROUND=1): hiding the window stops the current activity instead of closing the app */
bool activity_background_mode(void);
void activity_window_hide(void);
void activity_window_show(void);
void activity_window_start_hidden(void);
void activity_window_show_on_sigusr1(void);
