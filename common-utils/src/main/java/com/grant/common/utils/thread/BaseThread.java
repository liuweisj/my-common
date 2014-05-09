package com.grant.common.utils.thread;
public abstract class BaseThread extends Thread{
	
	private ThreadEvent event;
	
	
	public BaseThread(ThreadEvent event) {
		this.event = event;
	}
	
	@Override
	 final public void run() {
		try {
			if(event!=null)event.onStart();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try {
			execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if(event!=null)event.onEnd();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public abstract void execute();
}
