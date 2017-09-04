package com.hjnerp.util;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AttachmentFileReader
{
	public static final void read(Context context, File file)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		
		String path = file.getAbsolutePath();
		String fileExpanded = path.substring(path.lastIndexOf(".") + 1);
		if ("doc".equalsIgnoreCase(fileExpanded) || "docx".equalsIgnoreCase(fileExpanded))
			intent.setDataAndType(uri, "application/msword");
		else if ("xls".equalsIgnoreCase(fileExpanded) || "xlsx".equalsIgnoreCase(fileExpanded))
			intent.setDataAndType(uri, "application/vnd.ms-excel");
		else if ("ppt".equalsIgnoreCase(fileExpanded) || "pptx".equalsIgnoreCase(fileExpanded))
			intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
		else if ("pdf".equalsIgnoreCase(fileExpanded))
			intent.setDataAndType(uri, "application/pdf");
		else if ("rmvb".equalsIgnoreCase(fileExpanded))
			intent.setDataAndType(uri, "application/vnd.rn-realmedia-vbr");
		else if ("rm".equalsIgnoreCase(fileExpanded))
			intent.setDataAndType(uri, "application/vnd.rn-realmedia");
		else if ("wav".equalsIgnoreCase(fileExpanded))
			intent.setDataAndType(uri, "audio/x-wav");
		else if ("mp3".equalsIgnoreCase(fileExpanded))
			intent.setDataAndType(uri, "audio/x-mpeg");
		else if ("mp4".equalsIgnoreCase(fileExpanded))
			intent.setDataAndType(uri, "video/mp4");
		else if ("ogg".equalsIgnoreCase(fileExpanded))
			intent.setDataAndType(uri, "application/ogg");
		else if ("txt".equalsIgnoreCase(fileExpanded))
			intent.setDataAndType(uri, "text/plain");
		else if ("html".equalsIgnoreCase(fileExpanded) || "htm".equalsIgnoreCase(fileExpanded))
			intent.setDataAndType(uri, "text/html");
		else if ("xml".equalsIgnoreCase(fileExpanded))
			intent.setDataAndType(uri, "application/xml");
		else if ("js".equalsIgnoreCase(fileExpanded))
			intent.setDataAndType(uri, "text/javascript");
		else if ("png".equalsIgnoreCase(fileExpanded))
			intent.setDataAndType(uri, "image/png");
		else if ("jpeg".equalsIgnoreCase(fileExpanded))
			intent.setDataAndType(uri, "image/jpeg");
		else if ("jpg".equalsIgnoreCase(fileExpanded))
			intent.setDataAndType(uri, "image/jpeg");
		else if ("bmp".equalsIgnoreCase(fileExpanded))
			intent.setDataAndType(uri, "image/bmp");
		else if ("gif".equalsIgnoreCase(fileExpanded))
			intent.setDataAndType(uri, "image/gif");
		context.startActivity(intent);
	}
}  
