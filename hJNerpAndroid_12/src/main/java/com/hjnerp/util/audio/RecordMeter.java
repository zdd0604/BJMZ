package com.hjnerp.util.audio;


import android.os.Handler;  
import android.os.Message;  

final class RecordMeter implements Handler.Callback
{
	private  BmobRecordManager T; 
  RecordMeter(BmobRecordManager paramBmobRecordManager)
  {
	  T = paramBmobRecordManager;
  }

  public final boolean handleMessage(Message paramMessage)
  {
    if (paramMessage.what == 10)
    {
      int i = paramMessage.arg1;  
      int j =  paramMessage.arg2;
      
      if (this.T.O != null)
      {
        this.T.O.onVolumnChanged(i);
        if ( j >= T.MAX_RECORD_TIME)
          this.T.O.onTimeChanged(j, T.getRecordFileName());
      }
      return true;
    }
    return false;
  }
}