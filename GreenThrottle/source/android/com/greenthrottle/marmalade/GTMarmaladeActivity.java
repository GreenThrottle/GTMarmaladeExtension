package com.greenthrottle.marmalade;

import com.ideaworks3d.marmalade.LoaderActivity;

import com.greenthrottle.gcs.api.ControllerEvent;
import com.greenthrottle.unifier.ControllerPlayer;
import com.greenthrottle.unifier.GreenThrottleService;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.util.Log;

import java.util.ArrayList;

public class GTMarmaladeActivity extends LoaderActivity
{

	private static GTMarmaladeActivity m_Activity = null;
	private MarmaladeService service = null;

	public static final int BUTTON_A = 0;
	public static final int BUTTON_B = 1;
	public static final int BUTTON_Y = 2;
	public static final int BUTTON_X = 3;
	public static final int BUTTON_DOWN = 4;
	public static final int BUTTON_UP = 5;
	public static final int BUTTON_LEFT = 6;
	public static final int BUTTON_RIGHT = 7;
	public static final int BUTTON_L1 = 8;
	public static final int BUTTON_L2 = 9;
	public static final int BUTTON_L3 = 10;
	public static final int BUTTON_R1 = 11;
	public static final int BUTTON_R2 = 12;
	public static final int BUTTON_R3 = 13;
	public static final int BUTTON_ANALOG_LEFT_UP = 14;
	public static final int BUTTON_ANALOG_LEFT_DOWN = 15;
	public static final int BUTTON_ANALOG_LEFT_LEFT = 16;
	public static final int BUTTON_ANALOG_LEFT_RIGHT = 17;
	public static final int BUTTON_ANALOG_RIGHT_UP = 18;
	public static final int BUTTON_ANALOG_RIGHT_DOWN = 19;
	public static final int BUTTON_ANALOG_RIGHT_LEFT = 20;
	public static final int BUTTON_ANALOG_RIGHT_RIGHT = 21;
	public static final int BUTTON_BACK = 22;
	public static final int BUTTON_START = 23;
	public static final int BUTTON_GTHOME = 24;

	private static final int BUTTONS_COUNT = 25;

	public static final int CONTROLLER_1 = 0;
	public static final int CONTROLLER_2 = 1;
	public static final int CONTROLLER_3 = 2;
	public static final int CONTROLLER_4 = 3;

	//max number of controllers
	public static final int MAX_CONTROLLERS = 4;

	//Button States
	//HELD - if the button is down
	//DOWN - if the button was pressed down this frame
	//UP - if the button was released up this frame
	public static final int BUTTON_STATE_HELD = 0;
	public static final int BUTTON_STATE_DOWN = 1;
	public static final int BUTTON_STATE_UP = 2;

	private static final int BUTTON_STATES_COUNT = 3;

	//Analog controls
	//LEFT - Left analog Stick, x and y axis information ( -127 to 127, 0 centered)
	//RIGHT - Right analog Stick, x and y axis information ( -127 to 127, 0 centered)
	//L2 - Left Trigger, x axis info  ( 0 to 255 (fully engaged))
	//R2 - Right Trigger, x axis info ( 0 to 255 (fully engaged))
	public static final int ANALOG_LEFT = 0;
	public static final int ANALOG_RIGHT = 1;
	public static final int ANALOG_L2 = 2;
	public static final int ANALOG_R2 = 3;

	private static final int ANALOG_CONTROL_COUNT = 4;

	//Analog AXIS
	//ANALOG_AXIS_X - X Data for dual axis controls, only value for single axis.
	//ANALOG_AXIS_Y - Y Data for dual axis controls.  Not used for single axis.
	public static final int ANALOG_AXIS_X = 0;
	public static final int ANALOG_AXIS_Y = 1;

	private static final int ANALOG_AXIS_COUNT = 2;

	public static boolean[][][] buttonState = null;
	public static float[][][]   analogState = null;
	public static boolean[][] analogChanged = null;
	public static boolean[] controllerConnected = null;
	public static boolean serviceConnected = false;

	public static final int STATE_BUTTON = 0;
	public static final int STATE_ANALOG = 1;

	class StateData
	{
		public int type;
		public int controllerId;
		public int controlKey;

		boolean pressed;
		float x;
		float y;
	}

	private static ArrayList<StateData> newStates = new ArrayList<StateData>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		m_Activity = this;
		service = new MarmaladeService();

		buttonState = new boolean[BUTTON_STATES_COUNT][MAX_CONTROLLERS][BUTTONS_COUNT];
		analogState = new float[ANALOG_AXIS_COUNT][MAX_CONTROLLERS][ANALOG_CONTROL_COUNT];
		analogChanged = new boolean[MAX_CONTROLLERS][ANALOG_CONTROL_COUNT];
		controllerConnected = new boolean[MAX_CONTROLLERS];

		service.BindService(this);
		service.addLeftRemap();
		service.hideSystemUI(this);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		service.UnbindService(this);
	}

	@Override
	protected void onResume()
	{
		m_Activity = this;
		super.onResume();
		service.hideSystemUI(this);
	}

	@Override
	protected void onPause()
	{
		m_Activity = null;
		super.onPause();
	}

	public static void StartFrame()
	{
		if( buttonState != null  && analogChanged != null)
		{
			for (int j = 0; j < MAX_CONTROLLERS; ++j)
			{
				for( int k = 0 ; k < BUTTONS_COUNT ; ++k )
				{
					buttonState[BUTTON_STATE_DOWN][j][k] = false;
					buttonState[BUTTON_STATE_UP][j][k] = false;
				}

				for( int k = 0; k < ANALOG_CONTROL_COUNT; ++k)
				{
					analogChanged[j][k] = false;
				}
			}
		}

		synchronized(newStates)
		{
			for(StateData state : newStates)
			{
				if(state.type == STATE_BUTTON)
				{
					buttonState[BUTTON_STATE_UP][state.controllerId][state.controlKey] = !state.pressed;
					buttonState[BUTTON_STATE_HELD][state.controllerId][state.controlKey] = state.pressed;
					buttonState[BUTTON_STATE_DOWN][state.controllerId][state.controlKey] = state.pressed;
				}
				else if(state.type == STATE_ANALOG)
				{
					analogState[ANALOG_AXIS_X][state.controllerId][state.controlKey] = state.x;
					analogState[ANALOG_AXIS_Y][state.controllerId][state.controlKey] = state.y;
					analogChanged[state.controllerId][state.controlKey] = true;
				}
			}

			newStates.clear();
		}


	}

	public static void AddLeftButtonRemap()
	{
		if( m_Activity != null  && m_Activity.service != null )
		{
			m_Activity.service.addLeftRemap();
		}
	}

	public static void RemoveLeftButtonRemap()
	{
		if( m_Activity != null && m_Activity.service != null )
		{
			m_Activity.service.removeLeftRemap();
		}
	}

	public static void AddRightButtonRemap()
	{
		if( m_Activity != null && m_Activity.service != null )
		{
			m_Activity.service.addRightRemap();
		}
	}

	public static void RemoveRightButtonRemap()
	{
		if( m_Activity != null && m_Activity.service != null )
		{
			m_Activity.service.removeRightRemap();
		}
	}
	
	public static void HideAndroidSystemUI()
	{
		if( m_Activity != null  && m_Activity.service != null )
		{
			m_Activity.service.hideSystemUI(m_Activity);
		}
	}

	public static void ClearControllerState(int controllerId)
	{			
		if( buttonState != null && analogChanged != null && controllerId < MAX_CONTROLLERS)
		{
			for( int k = 0 ; k < BUTTONS_COUNT ; ++k )
			{
				buttonState[BUTTON_STATE_HELD][controllerId][k] = false;
				buttonState[BUTTON_STATE_DOWN][controllerId][k] = false;
				buttonState[BUTTON_STATE_UP][controllerId][k] = false;
			}

			for( int k = 0; k < ANALOG_CONTROL_COUNT; ++k)
			{
				analogChanged[controllerId][k] = false;
			}
		}
	}

	class MarmaladeService extends GreenThrottleService
	{
		protected void ButtonAction(ControllerPlayer player, ControllerEvent.CommonCodes code, boolean pressed)
		{
			int controllerId = player.getPlayerNumber()-1;
			if(controllerId >= MAX_CONTROLLERS)
			{
				return;
			}

			int button = -1;
			//Log.i("marmalade", "ButtonAction: " + controllerId + " code: " + code.n() + " pressed: " + (pressed?"true":"false"));
			switch(code)
			{
				case A_BUTTON:
					button = BUTTON_A;
					break;
				case B_BUTTON:
					button = BUTTON_B;
					break;
				case X_BUTTON:
					button = BUTTON_X;
					break;
				case Y_BUTTON:
					button = BUTTON_Y;
					break;
				case L1_BUTTON:
					button = BUTTON_L1;
					break;
				case L2_BUTTON:
					button = BUTTON_L2;
					break;
				case L3_BUTTON:
					button = BUTTON_L3;
					break;
				case R1_BUTTON:
					button = BUTTON_R1;
					break;
				case R2_BUTTON:
					button = BUTTON_R2;
					break;
				case R3_BUTTON:
					button = BUTTON_R3;
					break;
				case BACK_BUTTON:
					button = BUTTON_BACK;
					break;
				case START_BUTTON:
					button = BUTTON_START;
					break;
				case HOME_BUTTON:
					button = BUTTON_GTHOME;
					break;
				case LEFT_ANALOG_AS_DPAD_UP:
					button = BUTTON_ANALOG_LEFT_UP;
					break;
				case LEFT_ANALOG_AS_DPAD_RIGHT:
					button = BUTTON_ANALOG_LEFT_RIGHT;
					break;
				case LEFT_ANALOG_AS_DPAD_DOWN:
					button = BUTTON_ANALOG_LEFT_DOWN;
					break;
				case LEFT_ANALOG_AS_DPAD_LEFT:
					button = BUTTON_ANALOG_LEFT_LEFT;
					break;

				case RIGHT_ANALOG_AS_DPAD_UP:
					button = BUTTON_ANALOG_RIGHT_UP;
					break;
				case RIGHT_ANALOG_AS_DPAD_RIGHT:
					button = BUTTON_ANALOG_RIGHT_RIGHT;
					break;
				case RIGHT_ANALOG_AS_DPAD_DOWN:
					button = BUTTON_ANALOG_RIGHT_DOWN;
					break;
				case RIGHT_ANALOG_AS_DPAD_LEFT:
					button = BUTTON_ANALOG_RIGHT_LEFT;
					break;
				case DPAD_LEFT:
					button = BUTTON_LEFT;
					break;
				case DPAD_RIGHT:
					button = BUTTON_RIGHT;
					break;
				case DPAD_UP:
					button = BUTTON_UP;
					break;
				case DPAD_DOWN:
					button = BUTTON_DOWN;
					break;
			}

			if(button != -1)
			{
				StateData data = new StateData();
				data.type = STATE_BUTTON;
				data.controllerId = controllerId;
				data.controlKey = button;
				data.pressed = pressed;
				synchronized(newStates)
				{
					newStates.add(data);
				}
			}
		}

		protected void AnalogEvent(ControllerPlayer player, ControllerEvent.CommonCodes code, float x, float y)
		{
			int controllerId = player.getPlayerNumber()-1;
			if(controllerId >= MAX_CONTROLLERS)
			{
				return;
			}

			int controlKey = -1;

			switch (code) 
			{
				case LEFT_ANALOG:
					controlKey = GTMarmaladeActivity.ANALOG_LEFT;
					break;
				case RIGHT_ANALOG:
					controlKey = GTMarmaladeActivity.ANALOG_RIGHT;
					break;
				case L2_ANALOG:
					controlKey = GTMarmaladeActivity.ANALOG_L2;
					y = 0;
					break;
				case R2_ANALOG:
					controlKey = GTMarmaladeActivity.ANALOG_R2;
					y = 0;
					break;
			}

			if(controlKey != -1)
			{
				StateData data = new StateData();
				data.type = STATE_ANALOG;
				data.controllerId = controllerId;
				data.controlKey = controlKey;
				data.x = x;
				data.y = y;
				synchronized(newStates)
				{
					newStates.add(data);
				}
			}
		}

		protected void ControllerAction(ControllerPlayer player, boolean connected)
		{
			int controllerId = player.getPlayerNumber()-1;
			if(controllerId >= MAX_CONTROLLERS)
			{
				return;
			}

			controllerConnected[controllerId] = connected;
		}

		protected void ServiceStatus(boolean isConnected)
		{
			if(isConnected)
			{
				// As part of the sample, tell the user what happened.
				Toast.makeText(GTMarmaladeActivity.this, "GCS connected",
						Toast.LENGTH_SHORT).show();			// As part of the sample, tell the user what happened.
			}
			else
			{
				Toast.makeText(GTMarmaladeActivity.this, "GCS DIS-connected",
						Toast.LENGTH_SHORT).show();
			}

			serviceConnected = isConnected;
		}
	};
}
