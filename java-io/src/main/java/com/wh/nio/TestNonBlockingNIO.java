package com.wh.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import org.junit.Test;

/**
 * 非阻塞NIO   
 * 一、使用NIO完成网络通信的三个核心：
 * 1、通道（Channel）：负责链接
 *   java.nio.channels.Channel接口
 *        |--SelectableChannel
 *           |--SocketChannel  TCP
 *           |--ServerSocketChannel  TCP
 *           |--DatagramChannel   UDP
 *           
 *           |--Pipe.SinkChannel
 *           |--Pipe.SourceChannel
 *           
 * 2、缓冲区（Buffer）；负责数据的存储
 * 
 * 3、选择器（Selector）：是SelectableChannel的多路复用器。用于监控SelectableChannel的IO状况
 * @author wh
 * @date  2018年2月8日 上午11:10:16 
 *
 */
public class TestNonBlockingNIO {
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
		//2、切换为非阻塞模式
		sChannel.configureBlocking(false);
		//3、分配指定大小的 缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		Scanner scan = new Scanner(System.in);
		//4、向服务端发送数据
		while (scan.hasNext()) {
			buf.put((new Date().toString()+"\n"+scan.next()).getBytes());
			buf.flip();
			sChannel.write(buf);
			buf.clear();
		}
		//5、关闭通道
		sChannel.close();
	}
	/**
	 * 服务端
	 * @author wh
	 * @date 2018年2月8日 
	 * @throws IOException
	 */
	@Test
	public void server() throws IOException {
		//1、获取通道
		ServerSocketChannel ssChannel = ServerSocketChannel.open();
		//2、绑定端口
		ssChannel.bind(new InetSocketAddress(9898));
		//3、切换为非阻塞模式
		ssChannel.configureBlocking(false);
		//4、获取选择器
		Selector selector = Selector.open();
		//5、将通道注册到选择器上，并指定“监听接收事件”
		ssChannel.register(selector, SelectionKey.OP_ACCEPT);
		//6、轮询式的获取选择器上已经“准备就绪”的事件
		while (selector.select() > 0 ) {
			//7、获取选择器上所有注册的“选择键（已就绪的监听事件）”
			Iterator<SelectionKey> it =  selector.selectedKeys().iterator();
			while (it.hasNext()) {
				//8、获取准备“就绪”事件
				SelectionKey sk = it.next();
				//9、判断就绪事件类型
				if (sk.isAcceptable()) {
					//10、若“接收就绪”，就获取客户端连接
					SocketChannel sChannel = ssChannel.accept();
					//11、切换为非阻塞模式
					sChannel.configureBlocking(false);
					//12、将通道注册到选择器上
					sChannel.register(selector, SelectionKey.OP_READ);
				} else if (sk.isReadable()) {
					//13、当前选择器上“读就绪”的通道
					SocketChannel sChannel = (SocketChannel)sk.channel();
					//14、分配指定大小的缓冲区 ，接收数据
					ByteBuffer buf = ByteBuffer.allocate(1024);
					int len = 0;
					while ((len = sChannel.read(buf)) > 0 ) {
						buf.flip();
						System.out.println(new String(buf.array(),0,len));
						buf.clear();
					}
					
				}
				//15、取消选择键SelectionKey
				it.remove();
			}
		}
		
	}
}
