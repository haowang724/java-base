package com.wh.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

/**
 * 一、使用NIO完成网络通信的三个核心：
 * 1、通道（Channel）：负责链接
 *   java.nio.channels.Channel接口
 *        |--SelectableChannel
 *           |--SocketChannel
 *           |--ServerSocketChannel
 *           |--DatagramChannel
 *           
 *           |--Pipe.SinkChannel
 *           |--Pipe.SourceChannel
 *           
 * 2、缓冲区（Buffer）；负责数据的存储
 * 
 * 3、选择器（Selector）：是SelectableChannel的多路复用器。用于监控SelectableChannel的IO状况
 * @author wh
 * @date  2018年2月6日 下午2:53:08 
 *
 */
public class TestBlockingNIO {
    
	/**
	 * 客户端
	 * @author wh
	 * @date 2018年2月8日 
	 * @throws IOException
	 */
	@Test
	public void client() throws IOException {
		//1、获取通道
		SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
		FileChannel fileChannel = FileChannel.open(Paths.get("D:/niotest/1.png"), StandardOpenOption.READ);
		
		//2、分配指定大小的缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		//3、读取本地文件，向服务端发送数据
		while (fileChannel.read(buf) != -1) {
			buf.flip();
			sChannel.write(buf);
			buf.clear();
		}
		//4、关闭通道
		sChannel.close();
		fileChannel.close();
	}
	
	/**
	 * 服务端
	 * @author wh
	 * @throws IOException 
	 * @date 2018年2月8日
	 */
	@Test
	public void server() throws IOException {
		//1、获取通道
		ServerSocketChannel ssChannel = ServerSocketChannel.open();getClass();
		FileChannel outChannel =  FileChannel.open(Paths.get("D:/niotest/2.png"), StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		//2、绑定连接
		ssChannel.bind(new InetSocketAddress(9898));
		//3、获取客户端的通道
		SocketChannel sChannel = ssChannel.accept();		
		//4、分配指定大小的缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);
 		//5、接收数据，写入磁盘
		while (sChannel.read(buf) != -1) {
			buf.flip();
			outChannel.write(buf);
			buf.clear();
		}
		//6、关闭连接
		ssChannel.close();
		sChannel.close();
		outChannel.close();
	}
}
