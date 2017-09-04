package com.hjnerp.util.audio;

public interface RecordControlListener {

	public abstract void startRecording(String paramString);

	public abstract void cancelRecording();

	public abstract int stopRecording();

	public abstract boolean isRecording();

	public abstract String getRecordFilePath(String paramString);
}
