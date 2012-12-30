package com.mtsteak.remotecontrol;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
public class RemoteControlActivity extends Activity implements OnClickListener{
   
	private RemoteControlService  mMyService;
	private TextView status_tv;
//	private Button startServiceButton;
//	private Button stopServiceButton;
//	private Button bindServiceButton;
	private Button stopComputerButton;
	private Button setting_bt;
	private Button send_bt;
	private EditText url;
	private RCPreference rcp;
	//private Button unbindServiceButton;
	private Context mContext;
	Map<String, String> prefs;
	private MulticastLock multicastLock;
	
	//这里需要用到ServiceConnection在Context.bindService和context.unBindService()里用到
//	private ServiceConnection mServiceConnection = new ServiceConnection() {
//		//当我bindService时，让TextView显示MyService里getSystemTime()方法的返回值	
//		public void onServiceConnected(ComponentName name, IBinder service) {
//			// TODO Auto-generated method stub
//			mMyService = ((RemoteControlService.RemoteControlBinder)service).getService();
//			//mTextView.setText("I am frome Service :" + mMyService.getSystemTime());
//		}
//		
//		public void onServiceDisconnected(ComponentName name) {
//			// TODO Auto-generated method stub
//			
//		}
//	};
	  @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.activity_remote_control, menu);
	        return true;
	    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_control);
        setupViews();
    }
    
    public void setupViews(){
    
    	mContext = RemoteControlActivity.this;
    	
    	url = (EditText)findViewById(R.id.url);
    	
    	rcp = new RCPreference(this);
    	prefs=rcp.getPreference();
    	
    	
    	
    	
        
    	
//    	startServiceButton = (Button)findViewById(R.id.startservice);
//    	stopServiceButton = (Button)findViewById(R.id.stopservice);
//    	bindServiceButton = (Button)findViewById(R.id.bindservice);
    	stopComputerButton = (Button)findViewById(R.id.stopcomputer);
    	setting_bt = (Button)findViewById(R.id.setting);
    	send_bt = (Button)findViewById(R.id.send_bt);

  //  	unbindServiceButton = (Button)findViewById(R.id.unbindservice);
    	status_tv=(TextView)findViewById(R.id.status);
    	String ipaddr=prefs.get("ip");
    	if(ipaddr.equals(""))
    	{
    		ipaddr="192.168.1.6";
    	}
    	url.setText(ipaddr);
//    	startServiceButton.setOnClickListener(this);
//    	stopServiceButton.setOnClickListener(this);
//    	bindServiceButton.setOnClickListener(this);
//    	unbindServiceButton.setOnClickListener(this);
    	stopComputerButton.setOnClickListener(this);
    	send_bt.setOnClickListener(this);
    	setting_bt.setOnClickListener(this);
    	
    	allowMulticast();
    }
   public void execCmd(String cmd)
   {
	
	 	  status_tv.setText("http://"+this.url.getText().toString()+"/rc_cmd/?cmd="+cmd);
	   AsyncHttpClient client = new AsyncHttpClient();
	   cmd= URLEncoder.encode(cmd);
	   client.get("http://"+this.url.getText().toString()+"/rc_cmd/?cmd="+cmd, new AsyncHttpResponseHandler() {
	       @Override
	       public void onSuccess(String response) {
	    	  status_tv.setText(response);
	       }
	   
	   });
   }
   public void updateStatus(String txt)
   {
	   url.setText(txt);
   }
   private void allowMulticast(){ 
       WifiManager wifiManager=(WifiManager)getSystemService(Context.WIFI_SERVICE); 
       multicastLock=wifiManager.createMulticastLock("multicast.test"); 
       multicastLock.acquire(); 
   } 

	public void onClick(View v) {
		// TODO Auto-generated method stub

//		if(v == startServiceButton){
//			Intent i  = new Intent();
//			i.setClass(RemoteControlActivity.this, RemoteControlService.class);
//			mContext.startService(i);
//		}else if(v == stopServiceButton){
//			Intent i  = new Intent();
//			i.setClass(RemoteControlActivity.this, RemoteControlService.class);
//			mContext.stopService(i);
//		}else if(v == bindServiceButton){
//			Intent i  = new Intent();
//			i.setClass(RemoteControlActivity.this, RemoteControlService.class);
//			mContext.bindService(i, mServiceConnection, BIND_AUTO_CREATE);
//		}
	if(v==setting_bt)
	{
		rcp.save(url.getText().toString());
		
	}
	else if(v == stopComputerButton){
			execCmd("shutdown -t 15 -s -f");
	}
	else if(v == send_bt){
		
        Log.d("Net.Utils","succ send packet ok"); 

		try {
			new SendDataTask(this).execute();
			//new getDataTask().execute();
		//	findServerIpAddress();
	//		sendData();
//			int port = 7777; byte[] arb = new byte[] {'h','e','l','l','o'};
//
//			InetAddress inetAddress = InetAddress.getByName("230.0.0.1");
//
//			DatagramPacket datagramPacket = new DatagramPacket(arb, arb.length, inetAddress, port);
//
//			MulticastSocket multicastSocket = new MulticastSocket();
//			multicastSocket.setTimeToLive(1);
//
//			multicastSocket.send(datagramPacket);

			} catch (Exception exception) {
	            Log.d("test",">>>exce"); 

			exception.printStackTrace();

			}
		Log.d("Net.Utils", "find ip ok."); 
        
        multicastLock.release();
		
	}
//		else{
//			mContext.unbindService(mServiceConnection);
//		}
	}
	
	 public void sendData() throws Exception
	 {
		    

		   String destAddressStr = "224.0.0.1";

        int destPortInt = 9998;

          int TTLTime = 1;

         InetAddress destAddress = InetAddress.getByName(destAddressStr);

         if(!destAddress.isMulticastAddress()){//检测该地址是否是多播地址

                  throw new Exception("地址不是多播地址");

         }

         int destPort = destPortInt;

         int TTL = TTLTime;

         MulticastSocket multiSocket =new MulticastSocket();

         multiSocket.setTimeToLive(TTL);

         byte[] sendMSG = "11#msg".getBytes();

         DatagramPacket dp = new DatagramPacket(sendMSG, sendMSG.length, destAddress  , destPort);

         
         multiSocket.send(dp);

         multiSocket.close();
	 }

}
class SendDataTask extends AsyncTask<Integer, Integer, String> {

	private Context context;
	SendDataTask(Context c)
	{
		super();
		this.context=c;
	}
	
	@Override
	protected String doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		   String destAddressStr = "224.0.0.1";

	        int destPortInt = 9998;

	          int TTLTime = 1;

	         InetAddress destAddress;
			try {
				destAddress = InetAddress.getByName(destAddressStr);
			
	         if(!destAddress.isMulticastAddress()){//检测该地址是否是多播地址

	                  throw new Exception("地址不是多播地址");

	         }

	         int destPort = destPortInt;

	         int TTL = TTLTime;

	         MulticastSocket multiSocket =new MulticastSocket(9998);
	       multiSocket.setLoopbackMode(true);
	         multiSocket.setTimeToLive(TTL);
	         InetAddress group = InetAddress.getByName(destAddressStr);

	       multiSocket.joinGroup(group);

	         byte[] sendMSG = "11#msg".getBytes();

	         DatagramPacket dp = new DatagramPacket(sendMSG, sendMSG.length, destAddress  , destPort);

	         multiSocket.send(dp);

	         
	         byte[] receiveData=new byte[256]; 
	         DatagramPacket  packet=new DatagramPacket(receiveData, receiveData.length); 
	         multiSocket.receive(packet); 
	            
	         String packetIpAddress=packet.getAddress().toString(); 
	         
	         packetIpAddress=packetIpAddress.substring(1, packetIpAddress.length());
	         	
	         // ((RemoteControlActivity) context).updateStatus(packetIpAddress);
	    
	         multiSocket.close();
	         return packetIpAddress;
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "failure";
		
	}

    protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(String r) {
    	((RemoteControlActivity) context).updateStatus(r);
    	
        //showDialog("Downloaded " + result + " bytes");
    }

}


