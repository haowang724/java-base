package com.cq.juc;
/**
 * 模拟CAS算法
 * @author wh
 * @date  2018年2月12日 上午9:59:55 
 *
 */
public class TestCompareAndSwap {
   public static void main(String[] args) {
	   final CompareAndSwap cas = new CompareAndSwap();
	   for (int i=0 ; i<10 ; i++) {
		   new Thread(new Runnable(){

			public void run() {
				int expectedValue = cas.get();
				int newValue = (int)(Math.random()*101);
				//System.out.println("----"+newValue);
				boolean b = cas.compareAndSet(expectedValue, newValue);
				System.out.println(b);
			}
			}).start();
	   }
   }
}
class CompareAndSwap{
	private int value;
	
	public synchronized int get () {
		return value;
	}
	
	public synchronized int compareAndSwap(int expectedValue , int newValue) {
		int oldValue = value;
		
		if (oldValue == expectedValue) {
			this.value = newValue;
		}
		return oldValue;
	}
	
	public synchronized boolean compareAndSet(int expectedValue , int newValue) {
		return expectedValue == compareAndSwap(expectedValue,newValue);
	}
}