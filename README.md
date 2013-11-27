GCM Client Demo
============================

参考手册： [Google Cloud Messaging for Android][1]. 

集成步骤
------
  - 使用Eclipse导入工程到Android的项目中
  - 打开游戏的主Activity，申请如下全局变量
  ```java
      /**
	   * Declare the instances
	   */
	  private GCMReceiver mGCMReceiver;
	  private IntentFilter mOnRegisteredFilter;
  ```
  - 在onCreate方法中添加如下初始化代码
  ```java
	 @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    ......

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
	```
  - onResume()和onPause()方法中要添加如下代码
  	```java
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
	 ```
  - 打开Constants类，将`SENDER_ID`修改为项目正确的数值
  	```java
		public static final String SENDER_ID = "1222211110000";
  	```
  	如果需要调试信息，可以移除这个类中displayMessage方法的注释
  - 资源文件strings.xml中需要添加如下字符串设定（主要用于调试）
  	```xml
		<resources>
		  ......
		  <string name="gcm_registration">Got id from Google: %1$s</string>
		  <string name="gcm_registered">From GCM: device successfully registered (regId = %1$s)!</string>
		  <string name="gcm_unregistered">From GCM: device successfully unregistered!</string>
		  <string name="gcm_message">From GCM: you got message!</string>
		  <string name="gcm_error">From GCM: error (%1$s).</string>
		  <string name="gcm_recoverable_error">From GCM: recoverable error (%1$s).</string>
		  <string name="gcm_deleted">From GCM: server deleted %1$d pending messages!</string>
		  <string name="gcm_server_registering">Trying (attempt %1$d/%2$d) to register device on Demo Server.</string>
		  <string name="gcm_server_registered">From Demo Server: successfully added device!</string>
		  <string name="gcm_server_unregistered">From Demo Server: successfully removed device!</string>
		  <string name="gcm_server_register_error">Could not register device on Demo Server after %1$d attempts.</string>
		  <string name="gcm_server_unregister_error">Could not unregister device on Demo Server (%1$s).</string>
		</resources>
  	```
  - AndroidManifest.xml中需要添加如下设置
  	- 权限
  	```xml
	    <permission
	        android:name="com.sincetimes.game.push.gcm.permission.C2D_MESSAGE"
	        android:protectionLevel="signature" />

	    <uses-permission android:name="com.sincetimes.game.push.gcm.permission.C2D_MESSAGE" />

	    <!-- App receives GCM messages. -->
	    <uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />
	    <!-- GCM connects to Google Services. -->
	    <uses-permission android:name="android.permission.INTERNET" />
	    <!-- GCM requires a Google account. -->
	    <uses-permission android:name="android.permission.GET_ACCOUNTS" />
	    <!-- Keeps the processor from sleeping when a message is received. -->
	    <uses-permission android:name="android.permission.WAKE_LOCK" />
  	```
  	- 应用
  	```xml
	    <application
	        ......
	        <receiver
	            android:name="com.google.android.gcm.GCMBroadcastReceiver"
	            android:permission="com.google.android.c2dm.permission.SEND" >
	            <intent-filter>
	                <action android:name="com.google.android.c2dm.intent.RECEIVE" />
	                <action android:name="com.google.android.c2dm.intent.REGISTRATION" />
	                <category android:name="com.sincetimes.game.push.gcm" />
	            </intent-filter>
	        </receiver>

	        <service android:name="com.sincetimes.game.push.gcm.GCMIntentService" />
	    </application>
  	```
    - GCMReceiver的sendIdToServer方法应进行相应的改造，以将手机端的RegID同步到服务器


  [1]: http://developer.android.com/guide/google/gcm/index.html
