/*
 * Acknowledgements: Some of the method calls used here are adopted from 
 * the samples given on the mapquestapi developers website
 * http://developer.mapquest.com/web/products/featured/android-maps-api/documentation
 * Thanks are also due to various "stackoverflow.com" posters for their code examples.
 */

package com.example.punctualityalarm;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Activity;
//import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.punctualityalarm.AlarmWakeupService.AlarmWakeupServiceBinder;
import com.example.punctualityalarm.JSONParser.MyCallbackInterface;


import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.RouteManager;
import com.mapquest.android.maps.RouteResponse;
import com.mapquest.android.maps.ServiceResponse.Info;
//import com.mapquest.android.samples.JSONParser;
//import com.mapquest.android.samples.R;


//public class MainActivity extends ActionBarActivity implements MyCallbackInterface {
public class MainActivity extends MapActivity implements MyCallbackInterface {

	// Private variables
	private CalledByAlarm RunNewQuery;
	private int mServiceStarted;
	boolean mBound = false;
	private AlarmWakeupService mService;
	private Calendar calendar;
	private String url_str ="";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//initialize
		calendar = Calendar.getInstance();
		mServiceStarted = 0; 
		//execute_url();
		
		init();
					
	}
	
	// Register the broadcastreceiver inside the activity's onResume()
	@Override
	protected void onResume() {
		super.onResume();
		if (RunNewQuery == null) {
			RunNewQuery = new CalledByAlarm();
		}
		IntentFilter intentFilter = new IntentFilter("com.example.punctualityalarm.ALARM_RECEIVED");
		registerReceiver(RunNewQuery, intentFilter);
	}
	
	@Override
	protected void onStop() {
	 
	// Unbind from the service
      if (mBound) {
          unbindService(mConnection);
          mBound = false;
          Log.d("Main:","onStop: unbindService()");
      }
      super.onStop();
	}
	
	@Override
	protected void onPause() {
	 
	// Unbind from the service
      if (mBound) {
          unbindService(mConnection);
          mBound = false;
          Log.d("Main:","onPause: unbindService()");
      }
      super.onPause();
	}
	
	@Override
	protected void onDestroy() {
	  super.onDestroy();
	  unregisterReceiver(RunNewQuery);
	}
	
	/** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
        	AlarmWakeupServiceBinder binder = (AlarmWakeupServiceBinder) service;
            mService = binder.getService();
            mBound = true;
            Log.d("Main:","onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            Log.d("Main:","onServiceDisconnected");
        }
    };
	
	
	@Override
    public void onRequestCompleted(JSONObject result) {
		// No longer used
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// This method is invoked by a click on the "Set Alarm" button
	// It triggers a new service and passes appointment information to it
	// through an Intent
	public void StartTracking(View V)
	{
		Intent serviceIntent = new Intent(this.getApplicationContext(), AlarmWakeupService.class);
		serviceIntent.putExtra("hour", calendar.get(Calendar.HOUR));
		serviceIntent.putExtra("minute", calendar.get(Calendar.MINUTE));
		serviceIntent.putExtra("month", calendar.get(Calendar.MONTH));
		serviceIntent.putExtra("day", calendar.get(Calendar.DATE));
		serviceIntent.putExtra("year", calendar.get(Calendar.YEAR));
		serviceIntent.putExtra("FromAddr", "");
		serviceIntent.putExtra("ToAddr", "");
		serviceIntent.putExtra("url_str", url_str);
		//this.getApplicationContext().startService(serviceIntent);
		startService(serviceIntent);
		Toast.makeText(getApplicationContext(), "Alarm Set!", Toast.LENGTH_LONG).show();
		mServiceStarted = 1;
		bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
		Log.d("Main:","bindService");
		
	}
	
	// This method is invoked by a click on the "Cancel Alarm" button
	public void CancelAlarm(View V) {
		Toast.makeText(getApplicationContext(), "Alarm Cancelled!", Toast.LENGTH_LONG).show();
	}
	
	public class CalledByAlarm extends BroadcastReceiver {
		@Override
	    public void onReceive(Context context, Intent intent) {
			
		}
	}
	
	// Code for DatePicker
	public void showDatePickerDialog(View v) {
		
		final Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getDefault());
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                            int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
            			calendar.set(Calendar.YEAR,year);
            			calendar.set(Calendar.MONTH,monthOfYear);
            			calendar.set(Calendar.DATE,dayOfMonth);


                    }
                }, mYear, mMonth, mDay);
        dpd.show();
	}
	
	// Code for DatePicker
	public void showTimePickerDialog(View v) {
		// Process to get Current Time
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getDefault());
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

     // Launch Time Picker Dialog
        TimePickerDialog tpd = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                            int minute) {
                    	calendar.set(Calendar.HOUR,hourOfDay);
            			calendar.set(Calendar.MINUTE,minute);
                    }
                }, mHour, mMinute, false);
        		tpd.show();
	}
		
	
	
	//@Override
	protected void init() {
		
		// Adopted from Mapquest sample code on mapquestapi developer page
		//super.init();

		//find the objects we need to interact with
		final MapView mapView = (MapView)findViewById(R.id.map);
		final WebView itinerary=(WebView)findViewById(R.id.itinerary);
				
		//grab refs to the UI elements we will be using
		final RelativeLayout mapLayout=(RelativeLayout)findViewById(R.id.mapLayout);
		final RelativeLayout itineraryLayout=(RelativeLayout)findViewById(R.id.itineraryLayout);
		final Button createRouteButton=(Button)findViewById(R.id.createRouteButton);
		final Button showItineraryButton=(Button)findViewById(R.id.showItineraryButton);
		final Button showMapButton=(Button)findViewById(R.id.showMapButton);
		final Button clearButton=(Button)findViewById(R.id.clearButton);
		final EditText start=(EditText)findViewById(R.id.startTextView);
		final EditText end=(EditText)findViewById(R.id.endTextView);
		final CheckBox avoidTollRoads=(CheckBox)findViewById(R.id.avoidTollRoads);
		final CheckBox avoidHighways=(CheckBox)findViewById(R.id.avoidHighways);
		
		//create a routeManager
		final RouteManager routeManager=new RouteManager(this);
		routeManager.setMapView(mapView);
		routeManager.setItineraryView(itinerary);
		routeManager.setRouteCallback(new RouteManager.RouteCallback() {
			@Override
			public void onError(RouteResponse routeResponse) {
				Info info=routeResponse.info;
				int statusCode=info.statusCode;
				
				StringBuilder message =new StringBuilder();
				message.append("Unable to create route.\n")
					.append("Error: ").append(statusCode).append("\n")
					.append("Message: ").append(info.messages);
				Toast.makeText(getApplicationContext(), message.toString(), Toast.LENGTH_LONG).show();
				createRouteButton.setEnabled(true);
			}

			@Override
			public void onSuccess(RouteResponse routeResponse) {
				clearButton.setVisibility(View.VISIBLE);
				if(showItineraryButton.getVisibility()==View.GONE &&
						showMapButton.getVisibility()==View.GONE){
					showItineraryButton.setVisibility(View.VISIBLE);
				}
				createRouteButton.setEnabled(true);
				//routeTime = routeResponse.route.time; 
			}
		});
		
		//attach the show itinerary listener
		showItineraryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mapLayout.setVisibility(View.GONE);
				itineraryLayout.setVisibility(View.VISIBLE);
				showItineraryButton.setVisibility(View.GONE);
				showMapButton.setVisibility(View.VISIBLE);
			}
		});
		
		//attach the show map listener
		showMapButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mapLayout.setVisibility(View.VISIBLE);
				itineraryLayout.setVisibility(View.GONE);
				showMapButton.setVisibility(View.GONE);
				showItineraryButton.setVisibility(View.VISIBLE);
			}
		});

		//attach the clear route listener
		clearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				routeManager.clearRoute();
				clearButton.setVisibility(View.GONE);
				showItineraryButton.setVisibility(View.GONE);
				showMapButton.setVisibility(View.GONE);
				mapLayout.setVisibility(View.VISIBLE);
				itineraryLayout.setVisibility(View.GONE);
			}
		});
		
		
		//create an onclick listener for the instructional text
		createRouteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {	

				createRouteButton.setEnabled(false);
				hideSoftKeyboard(view);
				
				//get the start and end locations
				String startAt = getText(start);
				String endAt = getText(end);
				
				String str_startAt="";
				String str_endAt="";
				try {
					str_startAt = URLEncoder.encode(startAt,"utf-8");
				} catch(UnsupportedEncodingException e) {
				    e.printStackTrace();
				} 
				try {
					str_endAt = URLEncoder.encode(endAt,"utf-8");
				} catch(UnsupportedEncodingException e) {
				    e.printStackTrace();
				}
				String url = "http://www.mapquestapi.com/directions/v2/route?key=";
				url += "Fmjtd%7Cluur256al9%2C8s%3Do5-9a1auy";
				url += "&callback=renderAdvancedNarrative&ambiguities=ignore&avoidTimedConditions=false";
				url += "&doReverseGeocode=true&outFormat=json&routeType=fastest&timeType=1&enhancedNarrative=false";
				url += "&shapeFormat=raw&generalize=0&locale=en_US&unit=m&from=";
				url += str_startAt;
				url += "&to=";
				url += str_endAt;
				url +="&drivingStyle=2&highwayEfficiency=21.0";
				 
				url_str = url;
				//This is an example of building an options object to send to the 
				//route service.  you can send any valid json that the directions 
				//service accepts.  Have a look at the direction service devloper
				//documentation at http://www.mapquestapi.com/directions/
				try{
					JSONObject options=new JSONObject();
					if(avoidHighways.isChecked() || avoidTollRoads.isChecked()){
						JSONArray avoids=new JSONArray();
						if(avoidTollRoads.isChecked())avoids.put("Toll Road");
						if(avoidHighways.isChecked())avoids.put("Limited Access");
						options.put("avoids",avoids);
					}
					routeManager.setOptions(options.toString());
				}catch (Exception e) {
					//this can be thrown by the JSON lib, but should not ever happen 
					//with this simple usage
				}

				//execute the route
				routeManager.createRoute(startAt, endAt);
			}
		});

	}
	/**
	 * Utility method for getting the text of an EditText, if no text was entered the hint is returned
	 * @param editText
	 * @return
	 */
	private String getText(EditText editText){
		String s = editText.getText().toString();
		if("".equals(s)) s=editText.getHint().toString();
		return s;
	}
	
	/**
	 * Hides the softkeyboard
	 * @param v
	 */
	private void hideSoftKeyboard(View v){
		//hides soft keyboard
		final InputMethodManager imm = (InputMethodManager)getSystemService(
				Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
