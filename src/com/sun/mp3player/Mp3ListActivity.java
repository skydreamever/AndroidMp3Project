package com.sun.mp3player;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.sun.download.HttpDownloader;
import com.sun.model.Mp3Info;
import com.sun.service.DownloadService;
import com.sun.xml.Mp3ListContentHandler;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class Mp3ListActivity extends ListActivity {
	
	private static final int UPDATE = 1;
	private static final int ABOUT = 2;
	private String xml = null;
	private List<Mp3Info> mp3Infos = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mp3_list);
		updateListView();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		menu.add(0, UPDATE, 1, R.string.update_item);
		menu.add(0, ABOUT, 2, R.string.about);
		
		getMenuInflater().inflate(R.menu.mp3_list, menu);
		return true;
	}
	
	
	
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Mp3Info mp3Info = mp3Infos.get(position);
		Intent intent = new Intent();
		intent.putExtra("mp3Info", mp3Info);
		intent.setClass(this, DownloadService.class);
		startService(intent);
		
	}


	private void updateListView(){
		new Thread(downloadXML).start();
		while(xml==null);
		mp3Infos = parse(xml);
		SimpleAdapter simpleAdapter = buildSimpleAdapter(mp3Infos);
		setListAdapter(simpleAdapter);
		
	}

	private SimpleAdapter buildSimpleAdapter(List<Mp3Info> mp3Infos){
		
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		for(Iterator iterator = mp3Infos.iterator();iterator.hasNext();){
			Mp3Info mp3Info = (Mp3Info) iterator.next();
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("mp3_name",mp3Info.getMp3Name());
			map.put("mp3_size",mp3Info.getMp3Size());
			list.add(map);
		}
		
		SimpleAdapter simpleAdapter = new SimpleAdapter(Mp3ListActivity.this,list,R.layout.mp3info_item,new String[]{"mp3_name","mp3_size"},new int[]{R.id.mp3_name,R.id.mp3_size});
		return simpleAdapter;
	}

	Runnable downloadXML = new Runnable(){
		public void run(){
			xml = downloadXML("http://121.250.219.225/Mp3Resource/resources.xml");
			//System.out.println(xml);
			
		}
		
	};

	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		if(item.getItemId()==UPDATE){
			updateListView();
			
		}
		if(item.getItemId()==ABOUT){
			
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private String downloadXML(String urlStr){
		HttpDownloader httpDownloader =new HttpDownloader();
		String result = httpDownloader.download(urlStr);
		return result;
	}
	
	private List<Mp3Info> parse(String xmlStr){
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		List<Mp3Info> infos = null;
		try{
			XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
			infos = new ArrayList<Mp3Info>();
			Mp3ListContentHandler mp3ListContentHandler = new Mp3ListContentHandler(infos);
			xmlReader.setContentHandler(mp3ListContentHandler);
			xmlReader.parse(new InputSource(new StringReader(xmlStr)));
			/*for(Iterator iterator = infos.iterator();iterator.hasNext();){
				Mp3Info mp3Info = (Mp3Info)iterator.next();
				System.out.println(mp3Info);
			}*/
		}catch(Exception e){
			e.printStackTrace();
		}
		return infos;
	}
	
	
	
}
