package com.cq.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
/**
 * 一、创建执行线程的方式三：实现Callable接口。相较于实现Runnable接口的方式，方法有返回值，并且可以抛出异常。
 * 
 * 二、执行Callable的方式，需要FutureTask实现类的支持，用于接收运算结果。FutureTask是Future接口的实现类
 * @author wh
 * @date  2018年2月12日 下午4:41:24 
 *
 */
public class TestCallable {

	public static void main(String[] args) {
		CallDemo cd = new CallDemo();
		//1、执行Callable方式，需要FutureTask实现类的支持，用于接收运算结果。
		FutureTask<Integer> result = new FutureTask(cd);
		
		new Thread(result).start();
		
		//2、接收线程的运算结果
	    try {
			Integer sum = result.get();//FutureTask 可用于闭锁
			System.out.println(sum);
			System.out.println("-------------");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

}
class CallDemo implements Callable<Integer> {

	public Integer call() throws Exception {
        int sum = 0;
        for (int i=0 ; i<=100 ; i++) {
        	sum += i;
        }
		return sum;
	}
	
}