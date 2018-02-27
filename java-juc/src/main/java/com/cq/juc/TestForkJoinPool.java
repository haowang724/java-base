package com.cq.juc;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * 1、Fork/Join框架：在必要的情况下，将一个大任务，进行拆分（fork） 成若干个子任务（拆到不能再拆，这里就是指我们制定的拆分的临界值），再将一个个小任务的结果进行join汇总。
 * 
 * 2、Fork/Join与传统线程池的区别:
 *     Fork/Join采用“工作窃取模式”，当执行新的任务时他可以将其拆分成更小的任务执行，并将小任务加到线程队列中，然后再从一个随即线程中偷一个并把它加入自己的队列中。
 *     就比如两个CPU上有不同的任务，这时候A已经执行完，B还有任务等待执行，这时候A就会将B队尾的任务偷过来，加入自己的队列中，对于传统的线程，ForkJoin更有效的利用的CPU资源！
 *         
 * 3、ForkJoin的实现：实现这个框架需要继承RecursiveTask 或者 RecursiveAction ，RecursiveTask是有返回值的，相反Action则没有
 * @author wh
 * @date  2018年2月26日  
 *
 */
public class TestForkJoinPool {
    public static void main(String[] args) {
    	// 创建包含Runtime.getRuntime().availableProcessors()返回值作为个数的并行线程的ForkJoinPool  
    	ForkJoinPool pool = new ForkJoinPool();
    	// 提交可分解的PrintTask任务  
   // 	pool.invoke(new PrintTask(0,200));
    	Integer result = pool.invoke(new SumTest(0,100));
    	//阻塞当前线程直到 ForkJoinPool 中所有的任务都执行结束 
    	pool.awaitQuiescence(2, TimeUnit.SECONDS);
    	
    	System.out.println(result);
        // 关闭线程池  
    	pool.shutdown();
    }
}

class PrintTask extends RecursiveAction {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2910053604830745761L;

	//每个“小任务”的最大打印数
	private static final int MAX = 50;
	
	private int start;
	
	private int end;
	
	public PrintTask(int start , int end) {
		this.start = start;
		this.end = end;
	}
	
	@Override
	protected void compute() {
		//当end-start的值小于MAX是，直接打印
		if ((end-start) <=MAX) {
			for (int i=start ; i<end ; i++) {
				System.out.println(Thread.currentThread().getName()+"的i值："+i);
			}
		} else {
			int middle = (start+end)/2;
			PrintTask left = new PrintTask(start ,middle);
			PrintTask right = new PrintTask(middle, end);
			left.fork();
			right.fork();
		}
		
	}
	
}

class SumTest extends RecursiveTask<Integer> {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -4441289142443374520L;

	private static final int THURSHOLD = 10;
	
	private int start;
	
	private int end;
	
	public SumTest(int start, int end) {
		this.start = start;
		this.end = end;
	}
	
	@Override
	protected Integer compute() {
		
		if ((end-start)<=THURSHOLD) {
			int sum = 0;
			for (int i=start ; i<=end ; i++) {
				sum += i;
			}
			return sum;
		} else {
			int middle = (start+end)/2;
			SumTest left = new SumTest(start,middle);
			SumTest right = new SumTest(middle+1,end);
			left.fork();
			right.fork();
			return left.join()+right.join();
		}
	}
	
}
