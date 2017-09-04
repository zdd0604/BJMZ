package com.hjnerp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.hjnerp.common.Constant;
import com.hjnerp.common.EapApplication;
import com.hjnerp.model.ChatHisBean;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoaderHelper {
	private static SimpleBitmapDisplayer bitmapDisplayer = new SimpleBitmapDisplayer();

	public static final ImageLoaderConfiguration getImageLoaderConfiguration() {
		ExecutorService es = Executors.newFixedThreadPool(4,
				new NameableThreadFactory("Image-Loader"));
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				EapApplication.getApplication())
				.memoryCacheExtraOptions(480, 800)
				// default = device screen dimensions
				.diskCacheExtraOptions(480, 800, null)
				.taskExecutor(es)
				.taskExecutorForCachedImages(es)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				.memoryCacheSizePercentage(13)
				.diskCache(
						new UnlimitedDiscCache(
								new File(Constant.CHAT_CACHE_DIR)))
				.diskCacheSize(50 * 1024 * 1024)
				.diskCacheFileCount(100)
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
				.imageDownloader(
						new BaseImageDownloader(EapApplication.getApplication()))
				.imageDecoder(new BaseImageDecoder(true))
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				// .writeDebugLogs()
				.build();
		return configuration;
	}

	public static final DisplayImageOptions getDisplayImageOptions() {

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(false).delayBeforeLoading(0)
				.cacheInMemory(true).cacheOnDisk(true)
				.preProcessor(new BitmapProcessor() {
					@Override
					public Bitmap process(Bitmap bitmap) {
						return bitmap;
					}
				}).postProcessor(new BitmapProcessor() {
					@Override
					public Bitmap process(Bitmap bitmap) {
						return bitmap;
					}
				}).extraForDownloader(null).considerExifParams(false)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.bitmapConfig(Bitmap.Config.ARGB_8888)
				.decodingOptions(new Options())
				// .displayer(new SimpleBitmapDisplayer())
				.displayer(bitmapDisplayer)
				.handler(new Handler(Looper.getMainLooper())).build();
		return options;
	}

	public static void displayImage(String imageName, String imageLocalUri,
			String uri, ImageView imageView) {
		File file = new File(imageLocalUri, imageName);
		if (file.exists()) {
			String urllocal = "file://" + imageLocalUri + imageName;
			ImageLoader.getInstance().displayImage(urllocal, imageView);
		} else {

			ImageLoader.getInstance().displayImage(uri, imageView,
					getDisplayImageOptions());
		}
	}

	public static void displayImage(String uri, ImageView imageView) {
		ImageLoader.getInstance().displayImage(uri, imageView,
				getDisplayImageOptions());
	}

	public static void displayImage(String uri, ImageView imageView,
			final ProgressBar pb) {

		ImageLoader.getInstance().displayImage(uri, imageView,
				getDisplayImageOptions(), new SimpleImageLoadingListener() {

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						super.onLoadingComplete(imageUri, view, loadedImage);
						pb.setVisibility(View.GONE);
					}
				});
	}

	public static void displayImageIco(String uri, final ImageView imageView,
			final ProgressBar pb) {

		ImageLoader.getInstance().displayImage(uri, imageView,
				getDisplayImageOptions(), new SimpleImageLoadingListener() {

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						super.onLoadingComplete(imageUri, view, loadedImage);
						imageView.setImageBitmap(loadedImage);
						pb.setVisibility(View.GONE);
					}
				});
	}

	public static void displayChatRightImage(final String imageName,
			String imageLocalUri, String uri, final ImageView imageView) {

		File file = new File(imageLocalUri, imageName);
		if (file.exists()) {
			String urllocal = "file://" + imageLocalUri + imageName;
			ImageLoader.getInstance().loadImage(urllocal,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							super.onLoadingComplete(imageUri, view, loadedImage);
							if (imageView.getTag() != null
									&& imageName
											.equalsIgnoreCase(((ChatHisBean) imageView
													.getTag()).getmsgIdFile())) {
								imageView.setImageBitmap(loadedImage);
							}
						}
					});
		} else {

			ImageLoader.getInstance().displayImage(uri, imageView,
					getDisplayImageOptions());
		}
	}

	public static void displayChatLeftImage(final String imageName,
			String imageLocalUri, String uri, final ImageView imageView) {

		File file = new File(imageLocalUri, imageName);
		if (file.exists()) {
			String urllocal = "file://" + imageLocalUri + imageName;
			ImageLoader.getInstance().loadImage(urllocal,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							super.onLoadingComplete(imageUri, view, loadedImage);
							if (imageView.getTag() != null
									&& imageName
											.equalsIgnoreCase(((ChatHisBean) imageView
													.getTag()).getmsgIdFile())) {
								imageView.setImageBitmap(loadedImage);
							}
						}
					});
		} else {

			ImageLoader.getInstance().loadImage(uri,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							super.onLoadingComplete(imageUri, view, loadedImage);
							if (imageView.getTag() != null
									&& imageName
											.equalsIgnoreCase(((ChatHisBean) imageView
													.getTag()).getmsgIdFile())) {
								imageView.setImageBitmap(loadedImage);
							}
						}
					});
		}
	}

}
