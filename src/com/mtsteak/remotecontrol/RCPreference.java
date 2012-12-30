package com.mtsteak.remotecontrol;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class RCPreference {
	private Context context;
	
	public RCPreference(Context context){
		this.context = context;
	}
	
	/**
	 * 保存参数
	 * @param ip 
	
	 */
	public void save(String ip){
		SharedPreferences preferences  = context.getSharedPreferences("rc_pref", Context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		edit.putString("ip", ip);
	
		edit.commit();
	}
	
	/**
	 * 
	 * @return 以前设定的参数
	 */
	public Map<String,String> getPreference(){
		SharedPreferences preferences  = context.getSharedPreferences("rc_pref", Context.MODE_PRIVATE);
		Map<String,String> map = new HashMap<String, String>();
		map.put("ip", preferences.getString("ip", ""));
		return map;
	}
}

