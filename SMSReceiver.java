package edu.armstrong.fonefinder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {
	private SharedPreferences FFPrefs;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
			FFPrefs = context.getSharedPreferences("FoneFinder", Context.MODE_PRIVATE);
			String activationString = FFPrefs.getString(context.getString(R.string.prefsname), "FoneFinder");
			Bundle bundle = intent.getExtras(); // ---get the SMS message passed
												// in---
			SmsMessage[] msgs = null;
			String msg_from = "";
			String msgBody = "";
			if (bundle != null) {
				// ---retrieve the SMS message received---
				try {
					Object[] pdus = (Object[]) bundle.get("pdus");
					msgs = new SmsMessage[pdus.length];
					for (int i = 0; i < msgs.length; i++) {
						msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
						msg_from = msgs[i].getOriginatingAddress();
						msgBody = msgs[i].getMessageBody();
						if (msgBody.equalsIgnoreCase(activationString)) {
							// Set ringer on
							AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
							am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
							am.setStreamVolume(AudioManager.STREAM_RING, am.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
							Log.d("Set Ringer", "ON!");
							
							// Get phone's last known location in lat/long
							LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
							Criteria cri = new Criteria();
					        cri.setAccuracy(Criteria.ACCURACY_FINE);
					        String provider = lm.getBestProvider(cri, true);
					        Location loc = lm.getLastKnownLocation(provider);
					        double lat, lon;
					        
					        lat = loc.getLatitude();
				        	lon = loc.getLongitude();
				        	
				        	String text = "https://maps.google.com/maps?ll=" + String.valueOf(lat) + "," + String.valueOf(lon);
				        	
				        	SmsManager sms = SmsManager.getDefault();
				        	sms.sendTextMessage(msg_from, null, text, null, null);
				        	Log.d("SMS From", msg_from);
				        	Log.d("Send SMS", text);
					        
						}
					}
				} catch (Exception e) {
					// Log.d("Exception caught",e.getMessage());
				}
			}
		}
	}
}