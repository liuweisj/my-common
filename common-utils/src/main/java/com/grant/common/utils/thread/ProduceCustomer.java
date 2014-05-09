package com.grant.common.utils.thread;

import java.util.LinkedList;

public class ProduceCustomer {
	private int max;
	private LinkedList<Object> list;
	
	public ProduceCustomer(int max) {
		list = new LinkedList<Object>();
		this.max = max;
	}
	
	public ProduceCustomer() {
		this(10);
	}
	
	public synchronized void add(Object obj) throws InterruptedException{
		if(list.size()==max){
			wait();
			list.addFirst(obj);
		}else{
			list.addFirst(obj);
		}
		notify();
	}
	
	public synchronized Object get() throws InterruptedException{
		Object rst = null;
		if(list.size()>0){
			rst = list.removeFirst();
		}else{
			wait();
			rst = list.removeFirst();
		}
		notify();
		
		return rst; 
	}
	
	public static void main(String[] args) throws InterruptedException {
		final ProduceCustomer p = new ProduceCustomer();
		final int c = 20;
		Runnable run1 = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < c; i++) {
					try {
						Object obj = new Object();
						p.add(obj);
						System.out.println("produce:"+obj);
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
						Thread.sleep(2000);
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
