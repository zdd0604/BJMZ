package com.mznerp.util;

import java.util.List;

public interface Command
{
	public void action();
	
	public interface OnResultListener
	{
		public void onResult(boolean success);
	}
	
	public interface OnMultiResultListener
	{
		public void onResult(List<Boolean> successes);
	}
}
