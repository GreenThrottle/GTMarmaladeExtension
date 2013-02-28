/*
 * Internal header for the GreenThrottle extension.
 *
 * This file should be used for any common function definitions etc that need to
 * be shared between the platform-dependent and platform-indepdendent parts of
 * this extension.
 */

/*
 * NOTE: This file was originally written by the extension builder, but will not
 * be overwritten (unless --force is specified) and is intended to be modified.
 */


#ifndef GREENTHROTTLE_INTERNAL_H
#define GREENTHROTTLE_INTERNAL_H

#include "s3eTypes.h"
#include "GreenThrottle.h"
#include "GreenThrottle_autodefs.h"


/**
 * Initialise the extension.  This is called once then the extension is first
 * accessed by s3eregister.  If this function returns S3E_RESULT_ERROR the
 * extension will be reported as not-existing on the device.
 */
s3eResult GreenThrottleInit();

/**
 * Platform-specific initialisation, implemented on each platform
 */
s3eResult GreenThrottleInit_platform();

/**
 * Terminate the extension.  This is called once on shutdown, but only if the
 * extension was loader and Init() was successful.
 */
void GreenThrottleTerminate();

/**
 * Platform-specific termination, implemented on each platform
 */
void GreenThrottleTerminate_platform();
bool GetButtonState_platform(int buttonState, int controllerId, int button);

float GetAnalogState_platform(int axis, int controllerId, int analog);

bool GetAnalogChanged_platform(int controllerId, int analog);

bool GetConnectedState_platform(int controllerId);

bool GetServiceConnected_platform();

void ClearControllerState_platform(int controllerId);

void AddLeftButtonRemap_platform();

void RemoveLeftButtonRemap_platform();

void AddRightButtonRemap_platform();

void RemoveRightButtonRemap_platform();

void HideAndroidSystemUI_platform();

void StartFrame_platform();


#endif /* !GREENTHROTTLE_INTERNAL_H */
