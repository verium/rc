package com.mtsteak.remotecontrol;



import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;
public class RemoteControlService extends Service {
	//定义个一个Tag标签
	private static final String TAG = "MyService";
	//这里定义吧一个Binder类，用在onBind()有方法里，这样Activity那边可以获取到
	private RemoteControlBinder mBinder = new RemoteControlBinder();
	@Override
	public IBinder onBind(Intent intent) {
		Log.e(TAG, "start IBinder~~~");
		return mBinder;
	}
	@Override
	public void onCreate() {
		Log.e(TAG, "start onCreate~~~");
		super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		Log.e(TAG, "start onStart~~~");
		super.onStart(intent, startId);	
	}
	
	@Override
	public void onDestroy() {
		Log.e(TAG, "start onDestroy~~~");
		super.onDestroy();
	}
	
	
	@Override
	public boolean onUnbind(Intent intent) {
		Log.e(TAG, "start onUnbind~~~");
		return super.onUnbind(intent);
	}
	
	//这里我写了一个获取当前时间的函数，不过没有格式化就先这么着吧
	public String getSystemTime(){
		
		Time t = new Time();
		t.setToNow();
		return t.toString();
	}
	
	public class RemoteControlBinder extends Binder{
		RemoteControlService getService()
		{
			return RemoteControlService.this;
		}
	}
}
