package com.grant.common.utils.collection;

import java.io.IOException;
import java.util.Random;

public class TestFileQueue {
	public static void main(String[] args) throws Exception {
		final FileQueue queue = FileQueue.getInstance();
		final int max = 3000;
		new Thread("th-process"){
			public void run() {
				System.out.println("start process...");
				while(true){
					try {
						Object obj = queue.pull();
						System.out.println("process :"+obj);
//						Thread.sleep(10);
						queue.callback(obj);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			};
		}.start();
		
		
		new Thread("th-add"){
			public void run() {
				System.out.println("th-add start..");
				Random ran = new Random();
				for (int j = 0; j < max; j++) {
					try {
						queue.push(("val-"+j).getBytes());
//						Thread.sleep(ran.nextInt(5000));
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				System.out.println("th-add closed.");
			};
		}.start();
		
	}
}
