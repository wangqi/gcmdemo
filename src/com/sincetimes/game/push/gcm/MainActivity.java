package com.sincetimes.game.push.gcm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends Activity {
  /**
   * Declare the instances
   */
  private GCMReceiver mGCMReceiver;
  private IntentFilter mOnRegisteredFilter;

  private TextView mStatus;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mStatus = (TextView) findViewById(R.id.status);

    /**
     * Initialize the CloudMessage Service
     */
    mGCMReceiver = new GCMReceiver();
    mOnRegisteredFilter = new IntentFilter();
    mOnRegisteredFilter.addAction(Constants.ACTION_ON_REGISTERED);

    if (Constants.SENDER_ID == null) {
      mStatus.setText("Missing SENDER_ID");
      return;
    }
    if (Constants.SERVER_URL == null) {
      mStatus.setText("Missing SERVER_URL");
      return;
    }

    try {
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);
		if (!regId.equals("")) {
			GCMReceiver.sendIdToServer(regId);
		} else {
		  GCMRegistrar.register(this, Constants.SENDER_ID);
		}
	} catch (Exception e) {
		Log.w("MainActivity", "User device does not support GCM");
	}
  }

  @Override
  public void onResume() {
    super.onResume();
    registerReceiver(mGCMReceiver, mOnRegisteredFilter);
  }

  @Override
  public void onPause() {
    super.onPause();
    unregisterReceiver(mGCMReceiver);
  }

}
