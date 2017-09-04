package com.hjnerp.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NameableThreadFactory implements ThreadFactory
{
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private final ThreadGroup group;
	private final String namePrefix;
	private final boolean daemon;
	private final int priority;
	
	public NameableThreadFactory(String namePrefix)
	{
		this(namePrefix, false, Thread.NORM_PRIORITY);
	}
	
	public NameableThreadFactory(String namePrefix, boolean daemon)
	{
		this(namePrefix, daemon, Thread.NORM_PRIORITY);
	}
	
	public NameableThreadFactory(String namePrefix, boolean daemon, int priority)
	{
		SecurityManager s = System.getSecurityManager();
		group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		this.namePrefix = namePrefix + "-";
		this.daemon = daemon;
		this.priority = priority;
	}

	public Thread newThread(Runnable r)
	{
		Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
		t.setDaemon(daemon);
		t.setPriority(priority);
		return t;
	}
}