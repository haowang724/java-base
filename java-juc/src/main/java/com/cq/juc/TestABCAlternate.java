package com.cq.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 编写一个程序，开启 3 个线程，这三个线程的 ID 分别为 A、B、C，每个线程将自己的 ID 在屏幕上打印 10 遍，要求输出的结果必须按顺序显示。
 *	如：ABCABCABC…… 依次递归
 * @author wh
 * @date  2018年2月18日  
 *
 */
public class TestABCAlternate {
    public static void main(String[] args ) {
        final AlternateDemo ad = new AlternateDemo();
    	new Thread(new Runnable(){

			public void run() {
				for (int i = 1 ; i <=10 ; i++) {
					ad.loopA(i);
				}
				
			}}, "A").start();
    	new Thread(new Runnable(){

			public void run() {
				for (int i = 1 ; i <=10 ; i++) {
					ad.loopB(i);
				}
				
			}}, "B").start();
    	new Thread(new Runnable(){

			public void run() {
				for (int i = 1 ; i <=10 ; i++) {
					ad.loopC(i);
				}
				
			}}, "C").start();
    }
}
class AlternateDemo {
    
   private int number = 1; 	//当前执行线程的标记
	
   private Lock lock = new ReentrantLock();
   
   private Condition condition1 = lock.newCondition();
   private Condition condition2 = lock.newCondition();
   private Condition condition3 = lock.newCondition();
   /**
    * 
    * @author wh
    * @date 2018年2月18日 
    * @param totalLoop  执行第几轮打印
    */
   public void loopA(int totalLoop) {
	  lock.lock();
	  try {
		  if (number!=1) {
			  try {
				condition1.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		  }
		  for (int i=1 ; i<=1 ; i++) {
			  System.out.println(Thread.currentThread().getName()+"\t "+ i +"\t" +totalLoop);
		  }
		  number = 2;
		  condition2.signal();
	  } finally {
		  lock.unlock();
	  }
	  
   }
   
   /**
    * 
    * @author wh
    * @date 2018年2月18日 
    * @param totalLoop  执行第几轮打印
    */
   public void loopB(int totalLoop) {
	  lock.lock();
	  try {
		  if (number!=2) {
			  try {
				condition2.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		  }
		  for (int i=1 ; i<=1 ; i++) {
			  System.out.println(Thread.currentThread().getName()+"\t "+ i +"\t" +totalLoop);
		  }
		  number = 3;
		  condition3.signal();
	  } finally {
		  lock.unlock();
	  }
	  
   }
   
   /**
    * 
    * @author wh
    * @date 2018年2月18日 
    * @param totalLoop  执行第几轮打印
    */
   public void loopC(int totalLoop) {
	  lock.lock();
	  try {
		  if (number!=3) {
			  try {
				condition3.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		  }
		  for (int i=1 ; i<=1 ; i++) {
			  System.out.println(Thread.currentThread().getName()+"\t "+ i +"\t" +totalLoop);
		  }
		  number = 1;
		  condition1.signal();
	  } finally {
		  lock.unlock();
	  }
	  
   }
}