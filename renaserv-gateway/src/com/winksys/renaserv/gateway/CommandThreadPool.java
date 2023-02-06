package com.winksys.renaserv.gateway;

import java.util.LinkedList;

import org.apache.log4j.Logger;

public class CommandThreadPool {
	
	private static final Logger LOG = Logger.getLogger(CommandThreadPool.class);
	
	public static interface Task { 
		
		void execute() throws Throwable;
		
	}
	
	private static class TaskRunnable implements Runnable {

		
		private CommandThreadPool pool;

		public TaskRunnable(CommandThreadPool commandThreadPool) {
			this.pool = commandThreadPool;
		}

		@Override
		public void run() {
			
			while (true) {
				
				Task task = null;
				synchronized (pool.tasks) {
					if (pool.tasks.isEmpty()) {
						try {
							pool.tasks.wait(10000);
							continue;
						} catch (InterruptedException e) {
						}
					}
					task = pool.tasks.remove();
				}
				
				try { 
					LOG.debug("Executing " + task.toString());
					task.execute();
				} catch (Throwable e) {
					LOG.error("[Task]", e);
				}
								
			}
			
		}
		
	}

	private int nThreads;
	private LinkedList<Task> tasks = new LinkedList<Task>();

	public CommandThreadPool(int nThreads) {
		this.nThreads = nThreads;
	}
	
	public void init() {
		
		for(int i = 0; i < nThreads; i++) {
			new Thread(new TaskRunnable(this)).start();
		}
		
	}
	
	public void addTask(Task task) {
		synchronized (tasks) {
			tasks.add(task);
			tasks.notifyAll();
		}
	}
	
	public boolean isBusy() {
		return !tasks.isEmpty();
	}
	
}
