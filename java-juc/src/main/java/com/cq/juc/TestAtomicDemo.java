package com.cq.juc;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 一、i++的原子性问题：i++的操作实际上分为三个步骤“读-改-写”
 *  int i = 10;
 *  i = i++;//10
 *  
 *  int temp = i;
 *  int = i+1;
 *  i = temp;
 * 二、原子变量：在java.util.concurrent.atomic 包下提供了一些原子变量。
 *  1、volatile 保证内存可见性
 *  2、CAS（Compare-And-Swap）算法保证数据变量的原子性
 *     CAS算法是硬件对于并发操作的支持
 *     CAS包含三个操作数：
 *     ①内存值 V
 *     ②预估值 A
 *     ③更新值 B
 *     当且仅当V==A是，V=B；否则，不会执行任何操作。
 *  3、缺点 
 *   会产生ABA问题，即如果初始值为A，一个线程先把值改为了B，又把值改回A，另一个线程会认为其没有变化。（解决办法，添加版本号）
 *   CAS自旋，可能会消耗很多资源  
 * @author wh
 * @date  2018年2月10日 上午11:17:40 
 *
 */
public class TestAtomicDemo {
	
   public static void main(String[] args) {
	   int j=10;
	   j=j++;
	   System.out.println("-----------"+j);
	   AtomicDemo ad = new AtomicDemo();
	   
	   for(int i=0 ; i<10 ; i++){
		   new Thread(ad).start();
	   }
   }
}
class AtomicDemo implements Runnable {
    
//	private volatile int serialNumber = 0;
	private AtomicInteger serialNumber = new AtomicInteger(0);
	
	public void run() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(getSerialNumber());
		
	}
	
	public int getSerialNumber() {
		return serialNumber.incrementAndGet();
	}
	
}
