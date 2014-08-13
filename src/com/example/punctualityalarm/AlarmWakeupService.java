package com.example.punctualityalarm;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.punctualityalarm.MainActivity.CalledByAlarm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.AlarmClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.punctualityalarm.JSONParser.MyCallbackInterface;

public class AlarmWakeupService extends Service implements MyCallbackInterface
{
	// Private variable to store the output of the mapquest query
	private int day;
	private int month;
	private int year;
	private int hour;
	private int minute;
	
	private String my_url;
	private long curTravelTime;
	private CalledByAlarm RunNewQuery;
	private Calendar calendar;
	
	private final IBinder mBinder = new AlarmWakeupServiceBinder();
	
    Alarm alarm = new Alarm();
    public void onCreate()
    {
        super.onCreate();   
        calendar = Calendar.getInstance();
        Log.d("AWS:","OnCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) 
    {
    	// hasExtra will be set only when the service is started by the activity.
    	// When this onStartCommand method is invoked by alarm trigger, there will be no has extras.
    	if (intent.hasExtra("hour")){
    	 hour = intent.getIntExtra("hour",0);
    	 minute = intent.getIntExtra("minute",0);
    	 day = intent.getIntExtra("day",0);
    	 month = intent.getIntExtra("month",0);
    	 year = intent.getIntExtra("year",0);
    	 // Set the Appointment Time
    	 calendar.set(year, month, day, hour, minute, 0);
    	 if (calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
    		 Toast.makeText(this, "Appointment Time is in the past?" , Toast.LENGTH_LONG).show();
			 stopSelf();
    	 }
    	 my_url = intent.getStringExtra("url_str");
    	 Log.d("AWS:onStartCommand hour=",Integer.toString(hour));
    	 Log.d("AWS:onStartCommand URL=",my_url);
    	 //calendar.set
    	}
         //alarm.SetAlarm(AlarmWakeupService.this);
         
         execute_url();
         //scheduleAlarm();
         //return START_STICKY;
         return START_REDELIVER_INTENT;
    }



    public void onStart(Context context,Intent intent, int startId)
    {
        //alarm.SetAlarm(context);
        Log.d("AWS:","onStart invoked");
    }

    @Override
    public IBinder onBind(Intent intent) 
    {
    	return mBinder;
    }
    
    //@Override
    //public void onDestroy() {
        // TODO Auto-generated method stub
     //   super.onDestroy();
    //}
    
    public void execute_url() {
		//URL to get JSON Array
		  //String url = "http://www.mapquestapi.com/directions/v2/route?key=Fmjtd%7Cluur256al9%2C8s%3Do5-9a1auy&callback=renderAdvancedNarrative&ambiguities=ignore&avoidTimedConditions=false&doReverseGeocode=true&outFormat=json&routeType=fastest&timeType=1&enhancedNarrative=false&shapeFormat=raw&generalize=0&locale=en_US&unit=m&from=Clarendon%20Blvd,%20Arlington,%20VA&to=2400%20S%20Glebe%20Rd,%20Arlington,%20VA&drivingStyle=2&highwayEfficiency=21.0";
		String default_url = "http://www.mapquestapi.com/directions/v2/route?key=Fmjtd%7Cluur256al9%2C8s%3Do5-9a1auy&callback=renderAdvancedNarrative&ambiguities=ignore&avoidTimedConditions=false&doReverseGeocode=true&outFormat=json&routeType=fastest&timeType=1&enhancedNarrative=false&shapeFormat=raw&generalize=0&locale=en_US&unit=m&from=Clarendon%20Blvd,%20Arlington,%20VA&to=2400%20S%20Glebe%20Rd,%20Arlington,%20VA&drivingStyle=2&highwayEfficiency=21.0";
		// Creating new JSON Parser
	    JSONParser jParser = new JSONParser(this);
	    jParser.execute(my_url);
	}
    

	@Override
	public void onRequestCompleted(JSONObject result) {    
		//Here's my JSONObject for the Activity to use!
		String INFO = "info";
		String STATUS_CODE = "statuscode";
		String TIME_STRING = "realTime";
		String ROUTE_STRING = "route";
		try {
			JSONObject jInfo = result.getJSONObject(INFO);
			int status = jInfo.getInt(STATUS_CODE);
			JSONObject jTime = result.getJSONObject(ROUTE_STRING);
			
			if (status == 0) {
				String myTime = jTime.getString(TIME_STRING);
				this.curTravelTime =  jTime.optLong(TIME_STRING);
				Log.d("AWS: TravelTime",myTime);
			} else {
				StringBuilder message =new StringBuilder();
				message.append("Unable to create route.\n")
					.append("Error: ").append(status).append("\n");
				Log.d("AWS: Error!!!!", message.toString());
				//Toast.makeText(getApplicationContext(), message.toString(), Toast.LENGTH_LONG).show();
			}
		} catch (JSONException e) {
		    e.printStackTrace();
		}
		scheduleAlarm();		
    }

    public void scheduleAlarm()
    {
            // time at which alarm will be scheduled here alarm is scheduled at 1 day from current time, 
            // we fetch  the current time in milliseconds and added 1 day time
            // i.e. 24*60*60*1000= 86,400,000   milliseconds in a day        
            //Long time = new GregorianCalendar().getTimeInMillis()+24*60*60*1000;
			//Long CurTime = new GregorianCalendar().getTimeInMillis();
			Long CurTime = Calendar.getInstance().getTimeInMillis();
			//Long time = new GregorianCalendar().getTimeInMillis()+ this.curTravelTime * 1000;
			Long alarmtime = this.curTravelTime;
			
			Long AppointmentTime = calendar.getTimeInMillis();
			//String date_time = "Date " + calendar.get(month) + "-" + calendar.get(day) + "-" + calendar.get(year)
			//					+ "-" + calendar.get(hour) + "-" + calendar.get(min);
			SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyy HH:mm:ss z");
			String date_time=date.format(calendar.getTime());
			String cur_date_time=date.format(new GregorianCalendar().getTime());
			Log.d("AWS: AppointmentTime is ",date_time);
			Log.d("AWS: CurrentTime is ",cur_date_time);
			if (AppointmentTime < CurTime) {
				Toast.makeText(this, "Appointment Time is in the past?" , Toast.LENGTH_LONG).show();
				stopSelf();
			}
			
			Long AlarmStartTime = AppointmentTime - (this.curTravelTime * 1000);
			Log.d("AWS: AppointmentTime is ",Float.toString(AppointmentTime));
			Log.d("AWS: AlarmStartTime is ",Float.toString(AlarmStartTime));
			Log.d("AWS: CurrentTime is ",Float.toString(CurTime));
			Long TimeDiff = AlarmStartTime - CurTime; 
			// If you have less than 15 minutes to go before your StartTravelTime, inform the user and exit the activity.
			if (TimeDiff < (15*60*1000)) {
				String msg0 ="Timediff =" + TimeDiff + ". Less than 15 mins remaining.";
				Log.d("AWS: Schedule Alarm",msg0);
				String msg1 = "You should leave now for your appointment at " + date_time ;
				String msg2 = "Your travel time is " + this.curTravelTime + " seconds." ;
				Log.d("AWS: Alarm!!!",msg1);
				Log.d("AWS: Alarm!!!",msg2);
				//RingAlarm();
				notification_alarm(); 
				//Toast.makeText(this, msg1 , Toast.LENGTH_LONG).show();
				//Toast.makeText(this, msg2 , Toast.LENGTH_LONG).show();
				//finish();
				stopSelf();
				Log.d("AWS:","Service Stopped");
			} else {
				// If your StartTravelTime is more than 2 hrs away, start the alarm 2hrs before the StartTravelTime
				// Else set the alarm to re-query mapquest in 15 minutes from now.
				if (TimeDiff > 2*60*60*1000) {
					String msg3 ="Timediff =" + TimeDiff + " More than 2 hours remaining.";
					Log.d("AWS: Schedule Alarm",msg3);
					AlarmStartTime = AlarmStartTime - 2*60*60*1000;
				} else {
					//AlarmStartTime = CurTime + 15*60*1000;
					// use shorter time for testing
					String msg4 ="Timediff =" + TimeDiff + " Less than 2 hours remaining.";
					Log.d("AWS: Schedule Alarm", msg4);
					AlarmStartTime = CurTime + 15*60*1000;
				}
	            // create an Intent and set the class which will execute when Alarm triggers, here we have
	            // given Alarm in the Intent, the onRecieve() method of this class will execute when
	            // alarm triggers and 
	            //we will write the code to send SMS inside onRecieve() method of Alarm class
	            //Intent intentAlarm = new Intent(this, Alarm.class);
				Intent intentAlarm = new Intent(this, AlarmWakeupService.class);
	       
	            // to invoke service 08/05/14 11:43pm
	            PendingIntent pintent = PendingIntent.getService(this, 0, intentAlarm, 0);
	            
	            // create the object
	            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

	            //set the alarm for particular time
	            //alarmManager.set(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(this,1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
	            //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,curtime, 30*1000, pintent);
	            alarmManager.set(AlarmManager.RTC_WAKEUP,AlarmStartTime, pintent);
            
	            String str2 = "Alarm Scheduled for " + AlarmStartTime + " seconds";
	            //Toast.makeText(this, str2, Toast.LENGTH_LONG).show();
	            Log.d("AWS: ALARM_SET", str2);
			}
    }

	
	public class AlarmWakeupServiceBinder extends Binder
    {
        AlarmWakeupService getService()
        {
            return AlarmWakeupService.this;
        }
    }
		
	public void RingAlarm() {
		Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra(AlarmClock.EXTRA_MESSAGE, "You must leave now"); 
		i.putExtra(AlarmClock.EXTRA_HOUR, 0); 
		i.putExtra(AlarmClock.EXTRA_MINUTES, 1); 
		startActivity(i); 
	}

	public void notification_alarm() {
		SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss z");
		String date_time=date.format(calendar.getTime());
		//Define Notification Manager
		NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

		//Define sound URI
		Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		String title = "Alarm!!!";
		String message = "You must leave now for your appointment at " + date_time;
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
		        .setSmallIcon(R.drawable.abc_ic_go)
		        .setContentTitle(title)
		        .setContentText(message)
		        .setSound(soundUri); //This sets the sound to play

		//Display notification
		notificationManager.notify(0, mBuilder.build());
	}
}
