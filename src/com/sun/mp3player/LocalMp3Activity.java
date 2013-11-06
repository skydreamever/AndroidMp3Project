package com.sun.mp3player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.sun.model.Mp3Info;
import com.sun.utils.FileUtils;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class LocalMp3Activity extends ListActivity{

	private List<Mp3Info> mp3Infos = null;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_mp3_list);
		showFiles();
	}

	@Override
	protected void onResume() {
		showFiles();
		super.onResume();
	}
	
	
	private void showFiles(){
		FileUtils fileUtils = new FileUtils();
		mp3Infos = fileUtils.getMp3Files("mp3/");
		if(mp3Infos==null)
			return;
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		for(Iterator iterator = mp3Infos.iterator();iterator.hasNext();){
			Mp3Info mp3Info = (Mp3Info)iterator.next();
			HashMap<String,String> mp3 = new HashMap<String,String>();
			mp3.put("mp3_name", mp3Info.getMp3Name());
			mp3.put("mp3_size",mp3Info.getMp3Size());
			list.add(mp3);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(this,list,R.layout.mp3info_item,new String[]{"mp3_name","mp3_size"},new int[]{R.id.mp3_name,R.id.mp3_size});
		setListAdapter(simpleAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		
		if(mp3Infos!=null){
			Mp3Info mp3Info = mp3Infos.get(position);
			System.out.println(mp3Info);
			Intent intent = new Intent();
			intent.putExtra("mp3Info", mp3Info);
			intent.setClass(this, PlayerActivity.class);
			startActivity(intent);
		}
		super.onListItemClick(l, v, position, id);
	}
	
	
	
}
