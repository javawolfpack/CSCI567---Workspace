package csci567.eventreceiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class DataReceiver extends BroadcastReceiver {
    private static final String TAG = "EventReciever:DataReceiver";
    private NotificationManager mNotificationManager;
	public static final int NOTIFICATION_ID = 1;
    
    public DataReceiver(){
    	
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
        	Log.d(TAG,"Battery Changed");
        	sendNotification(context,"Battery Changed");
        	batteryChanged(intent);            
        } else if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
        	Toast.makeText(context, "Power Connected", Toast.LENGTH_LONG).show();
        } else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
        	Toast.makeText(context, "Power Dis-connected", Toast.LENGTH_LONG).show();          
        } else if (intent.getAction().equals(Intent.ACTION_REBOOT)) {
        	Toast.makeText(context, "Rebooting Phone", Toast.LENGTH_LONG).show();
        } else if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
        	Toast.makeText(context, "Shutdown Phone", Toast.LENGTH_LONG).show();
        } 
        else {
           //UNDEFINED
        }       
        
    }
    
    
    public void batteryChanged(Intent intent) {
        Bundle extras = intent.getExtras();
        int level = -1, plug = -1, scale = -1, stat = -1, volt=-1;
        String plugged = "";
        String status = "";
        if (extras != null) {
            level = extras.getInt("level");
            scale = extras.getInt("scale");
            plug = extras.getInt("plugged", 10);
            stat = extras.getInt("status", 10);
            volt = extras.getInt("voltage");
            switch (plug) {
                case 0:  plugged = "UN"; break;
                case 1:  plugged = "AC"; break;
                case 2:  plugged = "USB"; break;
                default:  plugged = "NOT RECEIVED"; break;
            }
            switch (stat) {
                case 1: status = "UNKNOWN" ; break;
                case 2: status = "CHARGING" ; break;
                case 3: status = "DISCHARGING" ; break;
                case 4: status = "NOT_CHARGING" ; break;
                case 5: status = "FULL" ; break;
                default: status = "NOT RECEIVED" ; break;
            }
            
        }
        Log.d(TAG,level + " " + scale + " " + plugged + " " + status + " " + volt);
    }
    
    private void sendNotification(Context context, String msg) {
        mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
            new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder = 
        	new NotificationCompat.Builder(context)
        		.setSmallIcon(R.drawable.ic_launcher)
        		.setContentTitle(context.getString(R.string.reboot_alert))
        		.setStyle(new NotificationCompat.BigTextStyle()
        		.bigText(msg))
        		.setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
  
}
