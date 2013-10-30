package com.sun.service;

import com.sun.download.HttpDownloader;
import com.sun.model.Mp3Info;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DownloadService extends Service{

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Mp3Info mp3Info = (Mp3Info)intent.getSerializableExtra("mp3Info");
		//System.out.println("service---->"+mp3Info);
		DownloadThread downloadThread = new DownloadThread(mp3Info);
		Thread thread = new Thread(downloadThread);
		thread.start();
		return super.onStartCommand(intent, flags, startId);
	}
	
	class DownloadThread implements Runnable{
		private Mp3Info mp3Info = null;
		public DownloadThread(Mp3Info mp3Info){
			this.mp3Info = mp3Info;
		}
		public void run(){
			String mp3Url = "http://121.250.219.225/Mp3Resource/";
			HttpDownloader httpDownloader = new HttpDownloader();
			int result = httpDownloader.downFile(mp3Url, "mp3/", mp3Info.getMp3Name());
			String resultMessage = null;
			if(result==-1){
				resultMessage = "下载失败";
			}
			else if(result==0){
				resultMessage="文件已存在";
			}
			else if(result==1){
				resultMessage="下载成功";
			}
			
			
			
		}
	}

	
	
	
	
}
