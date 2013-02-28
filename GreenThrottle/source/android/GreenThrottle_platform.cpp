/*
 * android-specific implementation of the GreenThrottle extension.
 * Add any platform-specific functionality here.
 */
/*
 * NOTE: This file was originally written by the extension builder, but will not
 * be overwritten (unless --force is specified) and is intended to be modified.
 */
#include "GreenThrottle_internal.h"

#include "s3eEdk.h"
#include "s3eEdk_android.h"
#include <jni.h>
#include "IwDebug.h"

static jobject g_Obj;
static jmethodID g_GetButtonState;
static jmethodID g_GetAnalogState;
static jmethodID g_GetAnalogChanged;
static jmethodID g_GetConnectedState;
static jmethodID g_GetServiceConnected;
static jmethodID g_ClearControllerState;
static jmethodID g_AddLeftButtonRemap;
static jmethodID g_RemoveLeftButtonRemap;
static jmethodID g_AddRightButtonRemap;
static jmethodID g_RemoveRightButtonRemap;
static jmethodID g_HideAndroidSystemUI;
static jmethodID g_StartFrame;

s3eResult GreenThrottleInit_platform()
{
    // Get the environment from the pointer
    JNIEnv* env = s3eEdkJNIGetEnv();
    jobject obj = NULL;
    jmethodID cons = NULL;

    // Get the extension class
    jclass cls = s3eEdkAndroidFindClass("GreenThrottle");
    if (!cls)
        goto fail;

    // Get its constructor
    cons = env->GetMethodID(cls, "<init>", "()V");
    if (!cons)
        goto fail;

    // Construct the java class
    obj = env->NewObject(cls, cons);
    if (!obj)
        goto fail;

    // Get all the extension methods
    g_GetButtonState = env->GetMethodID(cls, "GetButtonState", "(III)Z");
    if (!g_GetButtonState)
        goto fail;

    g_GetAnalogState = env->GetMethodID(cls, "GetAnalogState", "(III)F");
    if (!g_GetAnalogState)
        goto fail;

    g_GetAnalogChanged = env->GetMethodID(cls, "GetAnalogChanged", "(II)Z");
    if (!g_GetAnalogChanged)
        goto fail;

    g_GetConnectedState = env->GetMethodID(cls, "GetConnectedState", "(I)Z");
    if (!g_GetConnectedState)
        goto fail;

    g_GetServiceConnected = env->GetMethodID(cls, "GetServiceConnected", "()Z");
    if (!g_GetServiceConnected)
        goto fail;

    g_ClearControllerState = env->GetMethodID(cls, "ClearControllerState", "(I)V");
    if (!g_ClearControllerState)
        goto fail;

    g_AddLeftButtonRemap = env->GetMethodID(cls, "AddLeftButtonRemap", "()V");
    if (!g_AddLeftButtonRemap)
        goto fail;

    g_RemoveLeftButtonRemap = env->GetMethodID(cls, "RemoveLeftButtonRemap", "()V");
    if (!g_RemoveLeftButtonRemap)
        goto fail;

    g_AddRightButtonRemap = env->GetMethodID(cls, "AddRightButtonRemap", "()V");
    if (!g_AddRightButtonRemap)
        goto fail;

    g_RemoveRightButtonRemap = env->GetMethodID(cls, "RemoveRightButtonRemap", "()V");
    if (!g_RemoveRightButtonRemap)
        goto fail;

    g_HideAndroidSystemUI = env->GetMethodID(cls, "HideAndroidSystemUI", "()V");
    if (!g_HideAndroidSystemUI)
        goto fail;

    g_StartFrame = env->GetMethodID(cls, "StartFrame", "()V");
    if (!g_StartFrame)
        goto fail;



    IwTrace(GREENTHROTTLE, ("GREENTHROTTLE init success"));
    g_Obj = env->NewGlobalRef(obj);
    env->DeleteLocalRef(obj);
    env->DeleteGlobalRef(cls);

    // Add any platform-specific initialisation code here
    return S3E_RESULT_SUCCESS;

fail:
    jthrowable exc = env->ExceptionOccurred();
    if (exc)
    {
        env->ExceptionDescribe();
        env->ExceptionClear();
        IwTrace(GreenThrottle, ("One or more java methods could not be found"));
    }
    return S3E_RESULT_ERROR;

}

void GreenThrottleTerminate_platform()
{
    // Add any platform-specific termination code here
}

bool GetButtonState_platform(int buttonState, int controllerId, int button)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    return (bool)env->CallBooleanMethod(g_Obj, g_GetButtonState, buttonState, controllerId, button);
}

float GetAnalogState_platform(int axis, int controllerId, int analog)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    return (float)env->CallFloatMethod(g_Obj, g_GetAnalogState, axis, controllerId, analog);
}

bool GetAnalogChanged_platform(int controllerId, int analog)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    return (bool)env->CallBooleanMethod(g_Obj, g_GetAnalogChanged, controllerId, analog);
}

bool GetConnectedState_platform(int controllerId)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    return (bool)env->CallBooleanMethod(g_Obj, g_GetConnectedState, controllerId);
}

bool GetServiceConnected_platform()
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    return (bool)env->CallBooleanMethod(g_Obj, g_GetServiceConnected);
}

void ClearControllerState_platform(int controllerId)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_ClearControllerState, controllerId);
}

void AddLeftButtonRemap_platform()
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_AddLeftButtonRemap);
}

void RemoveLeftButtonRemap_platform()
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_RemoveLeftButtonRemap);
}

void AddRightButtonRemap_platform()
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_AddRightButtonRemap);
}

void RemoveRightButtonRemap_platform()
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_RemoveRightButtonRemap);
}

void HideAndroidSystemUI_platform()
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_HideAndroidSystemUI);
}

void StartFrame_platform()
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_StartFrame);
}
