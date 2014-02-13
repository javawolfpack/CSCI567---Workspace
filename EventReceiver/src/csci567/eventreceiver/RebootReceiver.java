package csci567.eventreceiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


public class RebootReceiver extends BroadcastReceiver{
	
	private NotificationManager mNotificationManager;
	public static final int NOTIFICATION_ID = 1;
	
	private static final String TAG = "EventReciever:RebootReceiver";
   	@Override
	public void onReceive(Context context, Intent intent) {
   		if(intent.getAction() != null){
    		if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
    			Log.d(TAG,"Reboot Succeded");
    			sendNotification(context, "Reboot Occurred");
    		}
    	}   		
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
