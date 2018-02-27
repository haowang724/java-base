package com.cq.juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 一、用于解决多线程安全问题方式：
 * synchronized：隐式锁
 * 1、同步代码块
 * 
 * 2、同步方法
 * 
 * jdk1.5后
 * 3、同步锁Lock
 * 注意：这是一个显式锁，需要通过lock()方法上锁，通过unLock方法释放锁
 * @author wh
 * @date  2018年2月18日 上午10:40:19 
 *
 */
public class TestLock {
	
	public static void main (String[] args) {
       Ticket t = new Ticket();
     
       new Thread(t,"一号窗口").start();
       new Thread(t,"二号窗口").start();
       new Thread(t,"三号窗口").start();
	}
}
class Ticket implements Runnable{
    
	private int tick = 100;
	
	private Lock lock = new ReentrantLock();
	
	public void run() {
		while (true) {
			lock.lock();			
			try {
				 if (tick>0) {
					 try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 System.out.println(Thread.currentThread().getName()+" 完成售票，余票为:"+ --tick);
				 }
			} finally {
				lock.unlock();
			}
		}
		
	}
	
}