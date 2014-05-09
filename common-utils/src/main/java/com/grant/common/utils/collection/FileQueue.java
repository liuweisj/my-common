package com.grant.common.utils.collection;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class FileQueue {
	
	private ReentrantLock lock;
	private ReentrantLock fileReadLock;
	private FileChannel head;
	private FileChannel data;
	
	private ByteBuffer headBuf;
	
	private ArrayBlockingQueue<Object> list;
	private ArrayBlockingQueue<Object> cache;//存缓存
	
	private Condition fileDataEmptyCondition;
	
	private int queuesize = 1024;
	private int cachesize = 1024;
	
	private String name;
	
	static private Map<String, FileQueue> map = new HashMap<String, FileQueue>();
	

	static public FileQueue getInstance() throws Exception{
		String name = "defaultQueue";
		return getInstance(name);
	}
	
	static public FileQueue getInstance(String name) throws Exception{
		FileQueue tmp = map.get(name);
		if(tmp!=null)return tmp;
		map.put(name,new FileQueue(name));
		return map.get(name);
	}
	
	private FileQueue(String name) throws Exception {
		System.out.println("instance...");
		if(map.containsKey(name)){
			throw new Exception("file queue exists.");
		}
		RandomAccessFile headFile = new RandomAccessFile(name+".fq", "rw");
		RandomAccessFile dataFile = new RandomAccessFile(name+".fq", "rw");
		this.list = new ArrayBlockingQueue<Object>(queuesize);
		this.cache = new ArrayBlockingQueue<Object>(cachesize);
		this.lock = new ReentrantLock();
		
		init(headFile, dataFile);
	}
	
	
	
	protected void init(RandomAccessFile headFile,RandomAccessFile dataFile) throws IOException {
		System.out.println("init...");
		head = headFile.getChannel();
		data = dataFile.getChannel();
		
		head.lock(0, 8, false);
		long p = data.size();
		if(p<8)p=8;
		data.position(p);
		
		headBuf = ByteBuffer.allocate(8);
		lock = new ReentrantLock(true);
		
		fileReadLock = new ReentrantLock();
		fileDataEmptyCondition = fileReadLock.newCondition();
		
		System.out.println("instance ReqdToQueue...");
		new ReadToQueue();
		
		System.out.println("init...end");
	}
	
	protected void changePosition(Long position) throws IOException, InterruptedException{
		lock.lockInterruptibly();
		try{
			headBuf.clear();
			headBuf.putLong(position);
			headBuf.flip();
			head.write(headBuf,0);
		}finally{
			lock.unlock();
		}
	}
	
	protected Long getPosition() throws InterruptedException, IOException{
		lock.lockInterruptibly();
		Long speek = 0L;
		try{
			headBuf.clear();
			int c = head.read(headBuf,0);
			if(c!=-1){
				headBuf.flip();
				speek = headBuf.getLong();
			}
		}finally{
			lock.unlock();
		}
		return speek;
	}
	
	/**
	 * 
	 * @description:推到队队列文件
	 * @create     :Mar 25, 2013
	 * @author     :liuwei
	 * @return     :void
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void push(byte[] b) throws IOException, InterruptedException{
		fileReadLock.lockInterruptibly();
		try {
			fileDataEmptyCondition.signal();
		}finally{
			fileReadLock.unlock();
		}
		writeData(b);
	}
	
	private class ReadToQueue extends Thread{
		
		ReadToQueue() {
			this.setDaemon(true);//设置为守护线程
			System.out.println("ReadToQueue start...");
			start();
			System.out.println("ReadToQueue start...end");
		}
		
		private long position = 8;

		@Override
		public void run() {
			System.out.println("ReadToQueue run..");
			try {
				position = getPosition();
				if(position==0)position = 8;
				readToQueue();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		private synchronized void readToQueue() throws IOException, InterruptedException{
			while(true){
				byte[] data = readData();
				fileReadLock.lockInterruptibly();
				try{
					if(data==null){
						fileDataEmptyCondition.await();
						continue;
					}
				}finally{
					fileReadLock.unlock();
				}
				list.put(new DataEntity(data,position));
				position+=data.length+4;
			}
		}
		
		private byte[] readData() throws IOException{
			long p = position;
			ByteBuffer buf = ByteBuffer.allocate(4);//
			int c = data.read(buf,position);//读取数据长度
			if(c<=0)return null;
			
			p+=c;
			buf.flip();
			int len = buf.getInt();
			buf = ByteBuffer.allocate(len);
			int dc = data.read(buf,p);
			if(dc<len){
				throw new IOException("read data error. data len : "+len+" readLen:"+dc);
			}
			
			buf.flip();
			byte[] data = new byte[dc];
			buf.get(data);
			
			return data;
		}
	}
	
	class DataEntity{
		public DataEntity(byte[] data,long position) {
			this.data = data;
			this.position = position;
		}
		private byte[] data;
		private long position;
		public byte[] getData(){return data;}
		public long getPosition(){return position;}
		
		@Override
		public String toString() {
			return new String(data);
		}
	}
	
	public void writeData(byte[] b) throws IOException{
		ByteBuffer buf = ByteBuffer.allocate(b.length+4);
		buf.putInt(b.length);
		buf.put(b);
		buf.flip();
		int r = data.write(buf);
		System.out.println("write:"+r);
	}
	
	

	/**
	 * 
	 * @description:需要显式调用callback 才算是消费了消息
	 * @create     :Apr 7, 2013
	 * @author     :liuwei
	 * @return
	 * @throws InterruptedException
	 * @return     :Object
	 */
	public Object pull() throws InterruptedException{
		Object obj = list.take();
		cache.put(obj);
		return obj;
	}
	
	/**
	 * 
	 * @description:pull完之后调用该方法,以确认消费
	 * @create     :Apr 7, 2013
	 * @author     :liuwei
	 * @param obj 调用pull()所返回的对象
	 * @throws IOException
	 * @throws InterruptedException
	 * @return     :void
	 */
	public void callback(Object obj) throws IOException, InterruptedException{
		cache.remove(obj);
		System.out.println("call back.."+getPosition());
		DataEntity ent = (DataEntity)obj;
		changePosition(ent.getPosition()+ent.getData().length+4);
		System.out.println("change position :"+getPosition());
	}
	
	public static void main(String[] args) throws Exception {
		final FileQueue fm = FileQueue.getInstance();
		final Random ran = new Random();
		
		new Thread(){
			public void run() {
				for (int i = 0; i < 0; i++) {
					try {
						fm.writeData(("test123456:"+Math.random()).getBytes());
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						System.out.println("write success, speek:"+fm.getPosition());
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
		
		new Thread(){
			public void run() {
				for (int i = 0; i < 100; i++) {
					try {
//						fm.changePosition(ran.nextLong());
						System.out.println("p:"+fm.getPosition());
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			};
		}.start();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
//	public static void main(String[] args) throws IOException {
//		RandomAccessFile file = new RandomAccessFile("D:/tmp/ran.txt", "rw");
//		FileChannel channel = file.getChannel();
//		FileChannel channel2 = file.getChannel();
//		ByteBuffer buf = ByteBuffer.allocate(8);
//		int c = channel.read(buf);
//		channel.tryLock(0,8,false);
//		if(c==-1){
//			buf.clear();
//			buf.putLong(0L);
//			buf.flip();
//			System.out.println("write :"+channel.write(buf));
//		}else{
//			buf.flip();
//			Long speek = buf.getLong();
//			System.out.println(speek);
//		}
//		System.out.println(c);
//	
//	}
	
}
