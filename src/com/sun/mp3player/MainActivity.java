package com.sun.mp3player;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;



public class MainActivity extends TabActivity{

	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		TabHost tabHost = getTabHost();
		
		Intent remoteIntent = new Intent();
		remoteIntent.setClass(this, Mp3ListActivity.class);
		TabHost.TabSpec remoteSpec = tabHost.newTabSpec("Remote");
		Resources res = getResources();
		remoteSpec.setIndicator("Remote",res.getDrawable(android.R.drawable.stat_sys_download));
		remoteSpec.setContent(remoteIntent);
		tabHost.addTab(remoteSpec); 
		
		Intent localIntent = new Intent();
		localIntent.setClass(this, LocalMp3Activity.class);
		TabHost.TabSpec localSpec = tabHost.newTabSpec("Local");
		localSpec.setIndicator("Local",res.getDrawable(android.R.drawable.stat_sys_download));
		localSpec.setContent(localIntent);
		tabHost.addTab(localSpec);
	}
	
	
	
}
