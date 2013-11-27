package com.sincetimes.game.push.gcm;

import static com.sincetimes.game.push.gcm.Constants.TAG;

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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class GCMReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String regId = intent.getStringExtra(Constants.FIELD_REGISTRATION_ID);
		sendIdToServer(regId);
	}

	public static void sendIdToServer(String regId) {
		//(new SendRegistrationIdTask(regId)).execute();
		Log.i(TAG, regId);
	}

	/**
	 * This utility is used to send regId to server. Change it to accommandate
	 * our needs.
	 * 
	 * @author wangqi
	 * 
	 */
	static final class SendRegistrationIdTask extends
			AsyncTask<String, Void, HttpResponse> {
		private String mRegId;

		public SendRegistrationIdTask(String regId) {
			mRegId = regId;
		}

		@Override
		protected HttpResponse doInBackground(String... regIds) {
			String url = Constants.SERVER_URL + "/register";
			HttpPost httppost = new HttpPost(url);

			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						1);
				nameValuePairs.add(new BasicNameValuePair("reg_id", mRegId));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpClient httpclient = new DefaultHttpClient();
				return httpclient.execute(httppost);
			} catch (ClientProtocolException e) {
				Log.e(Constants.TAG, e.getMessage(), e);
			} catch (IOException e) {
				Log.e(Constants.TAG, e.getMessage(), e);
			}

			return null;
		}

		@Override
		protected void onPostExecute(HttpResponse response) {
			if (response == null) {
				Log.e(Constants.TAG, "HttpResponse is null");
				return;
			}

			StatusLine httpStatus = response.getStatusLine();
			if (httpStatus.getStatusCode() != 200) {
				Log.e(Constants.TAG, "Status: " + httpStatus.getStatusCode());
				return;
			}

		}
	}
}