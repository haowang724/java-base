package com.cq.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 一、线程池：提供一个线程队列，队列中保存着所有等待状态的线程。避免了创建和销毁的额外开销，提高了响应速度。
 * 
 * 二、线程池的体系结构：
 *   java.util.concurrent.Executor:负责线程的使用和调度的根接口
 *     |--**ExecutorService 子接口：线程池的主要接口
 *         |--ThreadPoolExecutor 线程池的实现类
 *         |--ScheduledExecutorService 子接口： 负责线程的调度
 *            |--ShceduledThreadPoolExecutor 继承ThreadPoolExecutor，实现ScheduledExecutorService
 * 
 * 三、工具类：Executors   
 * ExecutorService newFixdThreadPool() : 创建固定大小的线程池
 * ExecutorService newCatchedThreadPool() : 缓存线程池，线程池数量不固定，可以根据需求自动的更改数量
 * ExecutorService newSingleThreadPool() : 创建单个线程池。线程池中只有一个线程        
 * 
 * ScheduledExecutorService new ScheduledThreadPool() : 创建固定大小的线程池，可以延时或定时执行任务
 * @author wh
 * @date  2018年2月26日  
 *
 */
public class TestThreadPool {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args ) throws Exception{
		//1、创建线程池
		   ExecutorService pool = Executors.newFixedThreadPool(5);
		   
		   List<Future<Integer>> list = new ArrayList<Future<Integer>>();
		   
		   for (int i=0 ; i<10 ; i++) {
			   Future<Integer> future = pool.submit(new Callable(){

				public Integer call() throws Exception {
					int sum = 0;
					for (int i=1 ; i<=100 ; i++) {
						sum += i;
					}
					return sum;
				}
			   });
			   list.add(future);
		   }
		   
	     for (Future<Integer> future : list) {
			System.out.println(future.get());
		}
		/*   ThreadPoolDemo tpd = new ThreadPoolDemo();
		   //2、为线程池中的线程分配任务
		   for (int i=0 ; i<10 ; i++) {
			   pool.submit(tpd);
		   }
		   //3、关闭线程池
		   pool.shutdown();*/
	}  
}
class ThreadPoolDemo implements Runnable{
    private int i = 0;
	public void run() {
		while(i<=100) {
			System.out.println(Thread.currentThread().getName()+ " : "+ i++);
		}
		
	}
	
}