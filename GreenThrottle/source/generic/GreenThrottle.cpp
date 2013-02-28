/*
Generic implementation of the GreenThrottle extension.
This file should perform any platform-indepedentent functionality
(e.g. error checking) before calling platform-dependent implementations.
*/

/*
 * NOTE: This file was originally written by the extension builder, but will not
 * be overwritten (unless --force is specified) and is intended to be modified.
 */


#include "GreenThrottle_internal.h"
s3eResult GreenThrottleInit()
{
    //Add any generic initialisation code here
    return GreenThrottleInit_platform();
}

void GreenThrottleTerminate()
{
    //Add any generic termination code here
    GreenThrottleTerminate_platform();
}

bool GetButtonState(int buttonState, int controllerId, int button)
{
	return GetButtonState_platform(buttonState, controllerId, button);
}

float GetAnalogState(int axis, int controllerId, int analog)
{
	return GetAnalogState_platform(axis, controllerId, analog);
}

bool GetAnalogChanged(int controllerId, int analog)
{
	return GetAnalogChanged_platform(controllerId, analog);
}

bool GetConnectedState(int controllerId)
{
	return GetConnectedState_platform(controllerId);
}

bool GetServiceConnected()
{
	return GetServiceConnected_platform();
}

void ClearControllerState(int controllerId)
{
	ClearControllerState_platform(controllerId);
}

void AddLeftButtonRemap()
{
	AddLeftButtonRemap_platform();
}

void RemoveLeftButtonRemap()
{
	RemoveLeftButtonRemap_platform();
}

void AddRightButtonRemap()
{
	AddRightButtonRemap_platform();
}

void RemoveRightButtonRemap()
{
	RemoveRightButtonRemap_platform();
}

void HideAndroidSystemUI()
{
	HideAndroidSystemUI_platform();
}

void StartFrame()
{
	StartFrame_platform();
}
