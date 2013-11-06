package com.sun.mp3player;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Queue;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sun.lrc.LrcProcessor;
import com.sun.model.Mp3Info;
import com.sun.service.PlayerService;

public class PlayerActivity extends Activity{
	
	ImageButton beginButton = null;
	ImageButton pauseButton = null;
	ImageButton stopButton = null;
	TextView lrcTextView = null;
	MediaPlayer mediaPlayer = null;
	
	private ArrayList<Queue> queues = null;
//	private boolean isPlaying = false;
//	private boolean isPause = false;
//	private boolean isReleased = false;
	private long begin = 0;
	private long nextTimeMill = 0;
	private long currentTimeMill = 0;
//	private String message = null;
	private long pauseTimeMill = 0;
	
	
	
	private Mp3Info mp3Info = null;
	private UpdateTimeCallback updateTimeCallback = null;
	private Handler handler = null;
	//private Intent intent = null;
	
	private boolean isPlaying = false;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);
		Intent intent = getIntent();
		handler = new Handler();
		mp3Info = (Mp3Info)intent.getSerializableExtra("mp3Info");
//		System.out.println(mp3Info);
		beginButton = (ImageButton)findViewById(R.id.begin);
		pauseButton = (ImageButton)findViewById(R.id.pause);
		stopButton = (ImageButton)findViewById(R.id.stop);
		beginButton.setOnClickListener(new BeginButtonListener());
		pauseButton.setOnClickListener(new PauseButtonListener());
		stopButton.setOnClickListener(new StopButtonListener());
		lrcTextView = (TextView)findViewById(R.id.lrcText);
		lrcTextView.setText("歌词显示在这里");
		
	}
	
//	protected void onPause(){
//		super.onPause();
//		un
//	}
	
	private void prepareLrc(String lrcName){
		try{
			InputStream inputStream = new FileInputStream(Environment.getExternalStorageDirectory().getAbsoluteFile()+File.separator+"mp3/"+mp3Info.getLrcName());
			LrcProcessor lrcProcessor = new LrcProcessor();
			queues = lrcProcessor.process(inputStream);
			updateTimeCallback = new UpdateTimeCallback(queues);
			begin = 0;
			currentTimeMill = 0;
			nextTimeMill = 0;
			handler.postDelayed(updateTimeCallback, 5);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	class BeginButtonListener implements OnClickListener{
		
		public void onClick(View v){
			Intent intent = new Intent();
			intent.setClass(PlayerActivity.this, PlayerService.class);
			intent.putExtra("MSG", AppConstant.PlayMSG.PLAY_MSG);
			intent.putExtra("mp3Info", mp3Info);
			
//			System.out.println("testPre");
			startService(intent);
			prepareLrc(mp3Info.getLrcName());
			begin = System.currentTimeMillis()+1500L;
			isPlaying = true;
		}
	}
	
	class PauseButtonListener implements OnClickListener{
		
		public void onClick(View v){
			Intent intent = new Intent();
			intent.setClass(PlayerActivity.this, PlayerService.class);
			intent.putExtra("MSG", AppConstant.PlayMSG.PAUSE_MSG);
			intent.putExtra("mp3Info", mp3Info);
			startService(intent);
			if(isPlaying){
				handler.removeCallbacks(updateTimeCallback);
				pauseTimeMill = System.currentTimeMillis();
			}else{
				handler.postDelayed(updateTimeCallback, 5);
				begin = System.currentTimeMillis()-pauseTimeMill+begin;
			}
			isPlaying = !isPlaying;
			
		}
	}
	
	class StopButtonListener implements OnClickListener{
		
		public void onClick(View v){
			Intent intent = new Intent();
			intent.setClass(PlayerActivity.this, PlayerService.class);
			intent.putExtra("MSG", AppConstant.PlayMSG.STOP_MSG);
			intent.putExtra("mp3Info",mp3Info);
			startService(intent);
			handler.removeCallbacks(updateTimeCallback);
		}
	}
	
	public String getMp3Path(Mp3Info mp3Info){
		String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
		String path = SDCardRoot+File.separator+"mp3"+File.separator+mp3Info.getMp3Name();
		return path;
	}
	
	class UpdateTimeCallback implements Runnable{
		//ArrayList<Queue> queues = null;
		Queue times = null;
		Queue messages = null;
		public UpdateTimeCallback(ArrayList<Queue> queues){
			times = queues.get(0);
			messages = queues.get(1);
			
		}
		
		public void run(){
			long offset = System.currentTimeMillis()-begin;
//			System.out.println(offset);
			if(currentTimeMill == 0){
				nextTimeMill = (Long)times.poll(); 
				lrcTextView.setText((String)messages.poll());
			}
			
			if(offset>=nextTimeMill){
				System.out.println("offset--->"+offset);
				System.out.println("nextTimeMill--->"+nextTimeMill);
				nextTimeMill = (Long)times.poll();

				lrcTextView.setText((String)messages.poll());
			}
			
			currentTimeMill = currentTimeMill + 10;
			handler.postDelayed(updateTimeCallback, 10);
			
		}
	}
	
}
