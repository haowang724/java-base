package com.cq.juc;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
public class TestScheduledThreadPool {
    @SuppressWarnings("unchecked")
	public static void main(String[] args) throws InterruptedException, ExecutionException {
    	ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);
    	
        for (int i=0 ; i<5 ; i++) {  	
          Future<Integer> result =  	pool.schedule(new Callable(){
        		public Integer call() throws Exception {
        			int number = new Random().nextInt(100);
        			System.out.println(Thread.currentThread().getName()+ " : " + number);
        			return number;
        		}}, 1, TimeUnit.SECONDS);
          System.out.println( result.get());	
        }
    	pool.shutdown();
    }
}
