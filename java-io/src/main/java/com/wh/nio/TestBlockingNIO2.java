package com.wh.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

public class TestBlockingNIO2 {
    /**
     * 客户端
     * @author wh
     * @throws IOException 
     * @date 2018年2月8日
     */
	@Test
	public void client() throws IOException {
		SocketChannel sChannel  = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
		FileChannel inChannel = FileChannel.open(Paths.get("D:/niotest/1.png"), StandardOpenOption.READ);
		
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		while(inChannel.read(buf) != -1) {
			buf.flip();
			sChannel.write(buf);
			buf.clear();
		}
		
		//通知完成输出
		sChannel.shutdownOutput();
		
		
		int len = 0;
		while ((len =sChannel.read(buf)) != -1) {
			buf.flip();
			System.out.println("----------"+new String(buf.array(),0,len));
			buf.clear();
		}
		inChannel.close();
		sChannel.close();
	}
	/**
	 * 服务端
	 * @author wh
	 * @throws IOException 
	 * @date 2018年2月8日
	 */
	@Test
	public void server() throws IOException {
		ServerSocketChannel ssChannel = ServerSocketChannel.open();
		FileChannel outChannel = FileChannel.open(Paths.get("D:/niotest/3.png"), StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		
		ssChannel.bind(new InetSocketAddress(9898));
		
		SocketChannel sChannel = ssChannel.accept();
		
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		while (sChannel.read(buf) != -1) {
			buf.flip();
			outChannel.write(buf);
			buf.clear();
		}

		
		buf.put("服务端已经接收完数据了".getBytes());
		buf.flip();
		sChannel.write(buf);
		
		sChannel.close();
		outChannel.close();
		ssChannel.close();
	}
}

