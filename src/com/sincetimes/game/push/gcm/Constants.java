package com.sincetimes.game.push.gcm;

import android.content.Context;
import android.widget.Toast;

public abstract class Constants {
  // Change this to the project id from your API project created at
  // code.google.com, as shown in the url of your project.
  public static final String SENDER_ID = "1049962075919";
  // Change this to match your server.
  public static final String SERVER_URL = "http://localhost";


  public static final String TAG = "GCMService";

  public static final String ACTION_ON_REGISTERED
      = "com.sqisland.android.gcm_client.ON_REGISTERED";

  public static final String FIELD_REGISTRATION_ID = "registration_id";
  public static final String FIELD_MESSAGE = "msg";
  
  /**
   * Notifies UI to display a message.
   * <p>
   * This method is defined in the common helper because it's used both by
   * the UI and the background service.
   *
   * @param context application's context.
   * @param message message to be displayed.
   */
  public static final void displayMessage(Context context, String message) {
//      Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
//      toast.show();
  }
  
}