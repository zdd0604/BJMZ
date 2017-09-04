package com.hjnerp.util.audio;

import java.util.Date;

import android.os.Message;

final class What implements Runnable {
	BmobRecordManager T;

	What(BmobRecordManager paramBmobRecordManager) {
		T = paramBmobRecordManager;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		 while (T.isRecording()) {
		  int j =   T.getMaxAmplitude() -2  ;
		  int i =(int) ((new Date().getTime() - T.startTime) / 1000);
		  
		  if (j >=7)
		  {
			  j=6;
		  }
		  if (j <1 )
		  {
			  j=0;
		  }
		  Message localMessage;
	      (localMessage = new Message()).arg1 = j;
	      localMessage.arg2 = i;
	      localMessage.what = 10;
	      this.T.R.sendMessage(localMessage);
	      try
	      {
	        Thread.sleep(100L);
	      }
	      catch (InterruptedException localInterruptedException)
	      {
	        Thread.currentThread().interrupt();
	        return;
	      }
		 }
	}
}
