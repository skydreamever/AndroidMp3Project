package com.sun.mp3player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.sun.model.Mp3Info;
import com.sun.utils.FileUtils;

import android.app.ListActivity;
import android.os.Bundle;

public class LocalMp3Activity extends ListActivity{

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mp3info_item);
		FileUtils fileUtils = new FileUtils();
		List<Mp3Info> mp3Infos = fileUtils.getMp3Files("mp3/");
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		for(Iterator iterator = list.iterator();iterator.hasNext();){
			Mp3Info mp3Info = (Mp3Info)iterator.next();
			HashMap<String,String> mp3 = new HashMap<String,String>();
			mp3.put("mp3_name", mp3Info.getMp3Name());
			mp3.put("mp3_size",mp3Info.getMp3Size());
			list.add(mp3);
		}
		SimpleAdapter
	}
	
}
