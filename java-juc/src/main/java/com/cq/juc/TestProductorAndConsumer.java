package com.cq.juc;
/**
 * 生产者和消费者案例
 * @author wh
 * @date  2018年2月18日 
 *
 */
public class TestProductorAndConsumer {
     public static void main(String[] args) {
    	 Clerk clerk = new Clerk();
    	 
    	 Productor pr = new Productor(clerk);
    	 Consumer cu = new Consumer(clerk);
    	 
    	 new Thread(pr,"生产者A").start();
    	 new Thread(cu,"消费者B").start();
 		 new Thread(pr, "生产者 C").start();
 		 new Thread(cu, "消费者 D").start();
     }
}
//店员
class Clerk {
    private int product = 0;
    
    public synchronized void get() {//进货
    	while (product>=1) { //为了避免虚假唤醒问题，应该总是使用在循环中
    		System.out.println("产品已满！");
    		try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

    	} 
    	System.out.println(Thread.currentThread().getName()+" : "+ ++product);
    	this.notifyAll();
    }
	
	public synchronized void sale() {//卖货
		while (product<=0) {
			System.out.println("缺货！");
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} 
	    System.out.println(Thread.currentThread().getName()+" : "+ --product);
		this.notifyAll();
	}
}

//生产者
class Productor implements Runnable{
	private Clerk clerk;
	
	public Productor(Clerk clerk) {
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
class Consumer implements Runnable {
    
	private Clerk clerk;
	
	public Consumer(Clerk clerk) {
		this.clerk = clerk;
	}
	
	public void run() {

      for (int i=0; i<5; i++ ) {
    	  clerk.sale();
      }
		
	}
	
}