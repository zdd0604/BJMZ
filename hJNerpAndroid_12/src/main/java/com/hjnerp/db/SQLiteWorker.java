package com.hjnerp.db;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.hjnerp.util.NameableThreadFactory;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * All sql should be executed by this singleton class, all database operate on foreground just post, and you can handle others,
 * it's work asynchronous, on sql complete, it will call you on main thread.
 * you need call start() method to init work, when stop() called, the remain task will discard.
 * @author John Kenrinus Lee 2014-06-01
 */
public final class SQLiteWorker
{
	private static final String TAG = SQLiteWorker.class.getSimpleName();
	
	private Handler mainHandler;
	private BlockingQueue<SQLable> dmlQueue;
	private DMLLooper dmlLooper;
	private ExecutorService dqlService;
	private boolean running;
	
	private static final class SQLiteWorkerHolder
	{
		static final SQLiteWorker instance = new SQLiteWorker();
	}
	
	public static final SQLiteWorker getSharedInstance()
	{
		return SQLiteWorkerHolder.instance;
	}
	
	private SQLiteWorker()
	{
	}
	
	/** before use service, you should call this to init*/
	public synchronized SQLiteWorker start()
	{
		if(running) return this;
		running = true;
		mainHandler = new Handler(Looper.getMainLooper());
		dmlQueue = new LinkedBlockingQueue<SQLable>();
		dmlLooper = new DMLLooper();
		dqlService = Executors.newCachedThreadPool(new NameableThreadFactory("SQLiteWorker-DQL-Thread"));
		return this;
	}
	
	/** when you won't use the service any more, you should call this to destroy service*/
	public synchronized SQLiteWorker stop()
	{
		running = false;
		dqlService.shutdown();
		dqlService = null;
		dmlQueue = null;
		dmlLooper = null;
		return this;
	}
	
	/** post a DML task*/
	public synchronized void postDML(SQLable sql)
	{
		if(checkState())
		{
			dmlQueue.offer(sql);
			if(!dmlLooper.isAlive())
				dmlLooper.start();
		}
	}
	
	/** post a DQL task*/
	public synchronized void postDQL(SQLable sql)
	{
		if(checkState())
			dqlService.execute(new ExecuteDQL(sql));
	}
	
	private synchronized boolean checkState()
	{
		if(!running)
		{
			Log.e(TAG, "illegel state: please call start() method before call this");
			return false;
		}
		return true;
	}
	
	//for database multi-read, no catch exception
	final class ExecuteDQL implements Runnable
	{
		SQLable sql;
		
		public ExecuteDQL(SQLable sql)
		{
			this.sql = sql;
		}
		
		@Override
		public void run()
		{
			try
			{
				final Object event = sql.call();
				mainHandler.post(new Runnable()
				{
					@Override
					public void run()
					{
						sql.onCompleted(event);
					}
				});
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	//for one database write use a queue on loop, no catch exception
	final class DMLLooper extends Thread
	{
		public DMLLooper()
		{
			setName("SQLiteWorker-DMLLooper-Thread");
		}
		@Override
		public void run()
		{
			while(running)
			{
				try
				{
					final SQLable sql = dmlQueue.take();
					if(sql != null)
					{
						final Object event = sql.call();
						mainHandler.post(new Runnable()
						{
							@Override
							public void run()
							{
								sql.onCompleted(event);
							}
						});
					}
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
					break;
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * describe a sql task, if possible, you should use AbstractSQLable
	 * @author John Kenrinus Lee 2014-06-01
	 */
	public interface SQLable
	{
		/** you code sql in this method, it's not run on main thread, you can return a Cursor or Model pojo*/
		public Object call();
		
		/** run on main thread when call() completed, the param of event is the return value of call()*/
		public void onCompleted(Object event);
	}
	
	/**
	 * abstract class which implement SQLable, it make default implements with onCompleted(), and allow publish intermediate products 
	 * @author John Kenrinus Lee 
	 * @date Jun 3, 2014
	 */
	public static abstract class AbstractSQLable implements SQLable
	{
		/** implement the call() method in SQLable*/
		public abstract Object doAysncSQL();
		
		/** when the doAysncSQL method throw a exception, event is Throwable, else it is the return value from doAysncSQL*/
		public void onCompleted(Object event) {}
		
		/** when call publish() in call() method to publish intermediate products, this method will be called on main thread*/
		public void onPublish(Object event) {}
		
		/** not on main thread, in call(), you can call this to publish intermediate products*/
		protected final void publish(final Object event)
		{
			SQLiteWorker.getSharedInstance().mainHandler.post(new Runnable()
			{
				@Override
				public void run()
				{
					onPublish(event);
				}
			});
		}
		
		/** you shouldn't override this method, use doAysncSQL instead*/
		@Deprecated
		public Object call()
		{
			try
			{
				return doAysncSQL();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return e;
			}
		}
	}
}
