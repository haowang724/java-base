package com.wh.nio;

import java.nio.ByteBuffer;

import org.junit.Test;

/**
 * 一、缓冲区（Buffer）：在Java NIO中负责数据的存取。缓冲区就是数组。用于存储不同数据类型的数据
 * 根据数据类型不同（boolean除外），提供了相应类型的缓冲区：
 * ByteBuffer、CharBuffer、ShortBuffer、IntBuffer、LongBuffer、FloatBuffer、DoubleBuffer，其中最常用的是ByteBuffer
 * 上述缓冲区的管理方式几乎一致，都是通过allocate()获取缓冲区
 * 
 * 二、缓冲区有两个核心方法
 * put() ：存入数据到缓冲区
 * get() ：获取缓冲区的数据
 * 
 * 三、缓冲区的四个核心属性
 * 1、capacity ：容量，表示缓冲区最大存储数据的容量，一旦声明不能改变
 * 2、limit ：界限，表示缓冲区可以操作的数据的大小。（limit后的数据不能进行读写）
 * 3、position ：位置，表示缓冲区正在操作数据的位置
 * 4、mark ： 标记，mark()用于记录当前position的位置，可以通过reset()恢复到mark的位置
 * 0 <= mark <= position <= limit <= capacity
 * 
 * 四、直接缓冲区和非直接缓冲区
 * 1、非直接缓冲区：通过allocate()方法分配缓冲区，将缓冲区建立在jvm内存中
 * 2、直接缓冲区：通过allocateDirect()方法分配缓冲区，将缓冲区建立在物理内存中，可以提高效率
 * @author wh
 * @date  2018年2月2日 下午7:23:35 
 *
 */
public class TestBuffer {
    
	@Test
	public void test1(){
		String str = "abcde";
		//1、分配一个指定大小的缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		System.out.println("----------------allocate()--------------");
		System.out.println(buf.position());
		System.out.println(buf.limit());
		System.out.println(buf.capacity());
		
		//2、利用put() 存入数据到缓冲区中
		buf.put(str.getBytes());		
		System.out.println("----------------put()--------------");
		System.out.println(buf.position());
		System.out.println(buf.limit());
		System.out.println(buf.capacity());
		
		//3、切换到读取数据模式
		buf.flip();
		System.out.println("----------------flip()--------------");
		System.out.println(buf.position());
		System.out.println(buf.limit());
		System.out.println(buf.capacity());
		
		//4、通过get()获取缓冲区数据
		System.out.println("----------------get()--------------");
		byte[] dst = new byte[buf.limit()];
		buf.get(dst);
		System.out.println("----------------str--------------:"+new String(dst, 0 , dst.length));
		System.out.println(buf.position());
		System.out.println(buf.limit());
		System.out.println(buf.capacity());
		
        //重新设置 limit put数据中  重新设置position，读取数据
		String str1 = "123";
        buf.limit(1024);
		buf.put(str1.getBytes());
		System.out.println(buf.position());
		System.out.println(buf.limit());
		System.out.println(buf.capacity());
		byte[] dst1 = new byte[8];
		buf.position(0);
		buf.get(dst1);
		System.out.println("----------------str1--------------:"+new String(dst1, 0 , dst1.length));
		System.out.println(buf.position());
		System.out.println(buf.limit());
		System.out.println(buf.capacity());
	}
}
