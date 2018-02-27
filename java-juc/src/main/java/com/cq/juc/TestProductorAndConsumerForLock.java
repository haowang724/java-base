package com.cq.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者和消费者案例
 * @author wh
 * @date  2018年2月18日 下午9:16:31 
 *
 */
public class TestProductorAndConsumerForLock {

	 public static void main(String[] args) {
    	 Clerk1 clerk = new Clerk1();
    	 
    	 Productor1 pr = new Productor1(clerk);
    	 Consumer1 cu = new Consumer1(clerk);
    	 
    	 new Thread(pr,"生产者A").start();
    	 new Thread(cu,"消费者B").start();
 		 new Thread(pr, "生产者 C").start();
 		// new Thread(cu, "消费者 D").start();
     }
}
//店员
class Clerk1 {
    private int product = 0;
    
	private Lock lock = new ReentrantLock();
	
	private Condition condition = lock.newCondition();
    
    public void get() {//进货
    	lock.lock();
    	try {
	    	while (product>=1) { //为了避免虚假唤醒问题，应该总是使用在循环中
	    		System.out.println("产品已满！");
	    		try {
					condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	
	    	} 
	    	System.out.println(Thread.currentThread().getName()+" : "+ ++product);
	    	condition.signalAll();
    	} finally {
			lock.unlock();
		}
    }
	
	public  void sale() {//卖货'
		lock.lock();
		try {
			while (product<=0) {
				System.out.println("缺货！");
				try {
					condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} 
		    System.out.println(Thread.currentThread().getName()+" : "+ --product);
			condition.signalAll();
		} finally {
			lock.unlock();
		}
	}
}

//生产者
class Productor1 implements Runnable{
	private Clerk1 clerk;
	
	public Productor1(Clerk1 clerk) {
		this.clerk = clerk;
	}

	public void run() {	
		for (int i = 0; i<5 ; i++) {	
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			clerk.get();
		}
	}	
}
//消费者
class Consumer1 implements Runnable {
    
	private Clerk1 clerk;
	
	public Consumer1(Clerk1 clerk) {
		this.clerk = clerk;
	}
	
	public void run() {

      for (int i=0; i<5; i++ ) {
    	  clerk.sale();
      }
	}
}	
