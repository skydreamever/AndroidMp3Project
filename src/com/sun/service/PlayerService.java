package com.sun.service;

import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

import com.sun.model.Mp3Info;
import com.sun.mp3player.AppConstant;
import com.sun.mp3player.PlayerActivity;


public class PlayerService extends Service{

	private MediaPlayer mediaPlayer = null;
	
	private boolean isPlaying = false;
	private boolean isPause = false;
	private boolean isReleased = false;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Mp3Info mp3Info = (Mp3Info)intent.getSerializableExtra("mp3Info");
		int MSG = intent.getIntExtra("MSG", 0);
		if(mp3Info!=null){
			if(MSG==AppConstant.PlayMSG.PLAY_MSG){
				play(mp3Info);
			}
			
		}else{
			if(MSG==AppConstant.PlayMSG.PAUSE_MSG){
				pause();
			}
			else if(MSG==AppConstant.PlayMSG.STOP_MSG){
				stop();
			}
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void play(Mp3Info mp3Info){
		String path = getMp3Path(mp3Info);
		mediaPlayer = MediaPlayer.create(this, Uri.parse("file://"+path));
		mediaPlayer.setLooping(false);
		mediaPlayer.start();
		isPlaying = true;
	}
	
	private void pause(){
		if(mediaPlayer!=null){
			if(isPlaying){
				if(!isPause){
					mediaPlayer.pause();
					isPause=true;
				}
				else{
					mediaPlayer.start();
					isPause=false;
				}
				
			}
		}
	}
	
	private void stop(){
		if(mediaPlayer!=null){
			if(isPlaying){
				if(!isReleased){
					mediaPlayer.stop();
					mediaPlayer.release();
					isPlaying = false;
					isPause = false;
					isReleased = false;
				}
			}
		}
	}
	
	public String getMp3Path(Mp3Info mp3Info){
		String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
		String path = SDCardRoot+File.separator+"mp3"+File.separator+mp3Info.getMp3Name();
		return path;
	}
	
	
}
