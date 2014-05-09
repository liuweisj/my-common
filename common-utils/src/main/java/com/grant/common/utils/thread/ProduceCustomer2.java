package com.grant.common.utils.thread;

import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProduceCustomer2 {
	private int max;
	private LinkedList<Object> list;
	private Condition full;
	private Condition nul;
	private ReentrantLock lock;
	
	public ProduceCustomer2(int max) {
		list = new LinkedList<Object>();
		this.max = max;
		lock = new ReentrantLock();
		full = lock.newCondition();
		nul = lock.newCondition();
	}
	
	public ProduceCustomer2() {
		this(10);
	}
	
	public void add(Object obj) throws InterruptedException {
		lock.lock();
		try {
			if(list.size()==max){
				full.await();
				list.addFirst(obj);
			}else{
				list.addFirst(obj);
				nul.signal();
			}
		}finally{
			lock.unlock();
		}
	}
	
	public Object get() throws InterruptedException{
		lock.lock();
		Object rst = null;
		try{
			if(list.size()>0){
				rst = list.removeFirst();
				full.signal();
			}else{
				nul.await();
				rst = list.removeFirst();
			}
		}finally{
			lock.unlock();
		}
		
		return rst; 
	}
	
	public static void main(String[] args) throws InterruptedException {
		final ProduceCustomer2 p = new ProduceCustomer2();
		final int c = 20;
		Runnable run1 = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < c; i++) {
					try {
						Object obj = new Object();
						p.add(obj);
						System.out.println("produce:"+obj);
//						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		Runnable run2 = new Runnable() {
			@Override
			public void run() {
				for (;;) {
					try {
						System.out.println("customer :"+p.get());
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		new Thread(run1).start();
		new Thread(run2).start();
	}
}
