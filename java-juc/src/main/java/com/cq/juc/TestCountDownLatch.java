package com.cq.juc;

import java.util.concurrent.CountDownLatch;

/**
 * CountDowmLatch：闭锁，在完成某些运算时，只有其他所有线程的运算全部完成，当前运算才继续执行
 * @author wh
 * @date  2018年2月12日 下午4:21:10 
 *
 */
public class TestCountDownLatch {
    public static void main(String[] args) {
    	long start = System.currentTimeMillis();
    	
    	CountDownLatch latch = new CountDownLatch(5);
    	LatchDemo ld = new LatchDemo(latch);
    	for (int i = 0 ; i<5 ; i++) {
    	   new Thread(ld).start();
   	}
    	try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}   
    	long end = System.currentTimeMillis();
    	
    	System.out.println("耗费时间："+(end-start));
    }
}
class LatchDemo implements Runnable{
    
	private CountDownLatch latch;
	
	public LatchDemo(CountDownLatch latch) {
		this.latch = latch;
	}
	
	public void run() {
		try {
			for (int i=0 ; i <500000 ; i++) {
			    if (i%2==0) {
			    	System.out.println(i);
			    }
			}
		} finally {
			latch.countDown();
		}
		
	}
	
}