package com.sun.service;

import com.sun.download.HttpDownloader;
import com.sun.model.Mp3Info;
import com.sun.mp3player.AppConstant.PlayMSG.URI;

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
			String mp3Url = URI.BASE_URL;
			HttpDownloader httpDownloader = new HttpDownloader();
			System.out.println(mp3Info);
			int result = httpDownloader.downFile(mp3Url, "mp3/", mp3Info.getMp3Name());
			result +=httpDownloader.downFile(mp3Url, "mp3/", mp3Info.getLrcName());
//			String resultMessage = null;
//			if(result==-1){
//				resultMessage = "下载失败";
//			}
//			else if(result==0){
//				resultMessage="文件已存在";
//			}
//			else if(result==1){
//				resultMessage="下载成功";
//			}
			
			
			
		}
	}

	
	
	
	
}
