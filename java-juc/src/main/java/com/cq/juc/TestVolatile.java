package com.cq.juc;
/**
 *  volatile 关键字：
 *   当多个线程操作共享数据时，可以保存内存中数据的可见性
 *   相较于synchronized是一种较为轻量级的同步策略
 *  注意：
 *   1、volatile不具备“互斥性”
 *   2、volatile不具备“原子性”
 * @author wh
 * @date  2018年2月10日 上午11:09:00 
 *
 */
public class TestVolatile {
    public static void main(String[] args) {
    	ThreadDemo td = new ThreadDemo();
    	new Thread(td).start();
    	
    	while(true) {
    		if (td.isFlag()) {
    			System.out.println("---------------------");
    			break;
    		}
    	}
    }
}
class ThreadDemo implements Runnable {
    
	private volatile boolean flag = false;
	
	public void run() {
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		flag = true;
		
		System.out.println("flag========"+isFlag());
		
	}
	
	public boolean isFlag() {
		return flag;
	}
	
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}