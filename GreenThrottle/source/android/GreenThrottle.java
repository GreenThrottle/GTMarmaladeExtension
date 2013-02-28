/*
java implementation of the GreenThrottle extension.

Add android-specific functionality here.

These functions are called via JNI from native code.
*/
/*
 * NOTE: This file was originally written by the extension builder, but will not
 * be overwritten (unless --force is specified) and is intended to be modified.
 */
import com.ideaworks3d.marmalade.LoaderAPI;

import com.greenthrottle.marmalade.GTMarmaladeActivity;

class GreenThrottle
{
    public boolean GetButtonState(int buttonState, int controllerId, int button)
    {
        if(GTMarmaladeActivity.buttonState != null)
		{
			return GTMarmaladeActivity.buttonState[buttonState][controllerId][button];
		}

		return false;
    }
    public float GetAnalogState(int axis, int controllerId, int analog)
    {
        if(GTMarmaladeActivity.analogState != null)
		{
			return GTMarmaladeActivity.analogState[axis][controllerId][analog];
		}

		return 0;
    }
    public boolean GetAnalogChanged(int controllerId, int analog)
    {
        if(GTMarmaladeActivity.analogChanged != null)
		{
			return GTMarmaladeActivity.analogChanged[controllerId][analog];
		}

        return false;
    }
    public boolean GetConnectedState(int controllerId)
    {
        if(GTMarmaladeActivity.controllerConnected != null)
		{
			return GTMarmaladeActivity.controllerConnected[controllerId];
		}

        return false;
    }
    public boolean GetServiceConnected()
    {
        return GTMarmaladeActivity.serviceConnected;
    }

    public void ClearControllerState(int controllerId)
    {
    	GTMarmaladeActivity.ClearControllerState(controllerId);    
    }

    public void AddLeftButtonRemap()
    {
    	GTMarmaladeActivity.AddLeftButtonRemap();    
    }
    public void RemoveLeftButtonRemap()
    {
    	GTMarmaladeActivity.RemoveLeftButtonRemap();    
    }
    public void AddRightButtonRemap()
    {
    	GTMarmaladeActivity.AddRightButtonRemap();    
        
    }
    public void RemoveRightButtonRemap()
    {
    	GTMarmaladeActivity.RemoveRightButtonRemap();    
        
    }
    public void HideAndroidSystemUI()
    {
    	GTMarmaladeActivity.HideAndroidSystemUI();    
        
    }
    public void StartFrame()
    {
    	GTMarmaladeActivity.StartFrame();    
    }
}
