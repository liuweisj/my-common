package com.grant.common.utils.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class MonitorThread {
	private Thread thread;
	public MonitorThread() {
		this.thread = new Thread(new Runnable() {
			@Override
			public void run() {
				
			}
		});
		
	}
	
	
	public static void main(String[] args) {
		
		final ReentrantLock lock = new ReentrantLock();
		
		Runnable run = new Runnable() {
			@Override
			public void run() {
				Condition condition = lock.newCondition();
				try {
					System.out.println("th-1:lockbefore");
					lock.lockInterruptibly();
					System.out.println("th-1:end");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				try {
					System.out.println("th-1:condition-wait-before");
					condition.await(2,TimeUnit.SECONDS);
					System.out.println("th-1:condition-wait-end");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				System.out.println("th-1:over");
			}
		};
		
		Runnable run2 = new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					System.out.println("th-2:lockbefore");
					lock.lockInterruptibly();
					System.out.println("th-2:end");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally{
					lock.unlock();
				}
				
				System.out.println("th-2:over");
			}
		};
		
		new Thread(run2).start();
		new Thread(run).start();
	}
}
