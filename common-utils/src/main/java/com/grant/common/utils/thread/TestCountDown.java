package com.grant.common.utils.thread;

import java.util.concurrent.CountDownLatch;

public class TestCountDown {
	public static void main(String[] args) {
		int t = 10;
		final CountDownLatch countdown = new CountDownLatch(t);
		long start = System.currentTimeMillis();
		
		Runnable run = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 100; i++) {
					System.out.println("aa");
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				countdown.countDown();
			}
		};
		
		for (int i = 0; i < t; i++) {
			new Thread(run).start();
		}
		try {
			countdown.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("end:");
		System.out.println(System.currentTimeMillis()-start);
	}
}
