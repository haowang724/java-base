package com.wh.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import org.junit.Test;

/**
 * 非阻塞NIO  UDP
 * @author wh
 * @date  2018年2月8日 下午1:50:08 
 *
 */
public class TestNonBlockingNIO2 {
    /**
     * 客户端
     * @author wh
     * @throws IOException 
     * @date 2018年2月8日
     */
	@Test
	public void client() throws IOException {
		DatagramChannel dchannel = DatagramChannel.open();
		
		dchannel.configureBlocking(false);
		
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		Scanner scan = new Scanner(System.in);
		
		while (scan.hasNext()) {
			String str  = scan.next();
			buf.put((new Date().toString()+"\n"+str).getBytes());
			buf.flip();
			dchannel.send(buf, new InetSocketAddress("127.0.0.1", 9898));
			buf.clear();
		}
		dchannel.close();
		
	}
	/**
	 * 服务端
	 * @author wh
	 * @throws IOException 
	 * @date 2018年2月8日
	 */
	@Test
	public void Server() throws IOException {
		DatagramChannel dc = DatagramChannel.open();
		
		dc.bind(new InetSocketAddress(9898));
		
		dc.configureBlocking(false);
		
		Selector selector = Selector.open();
		
		dc.register(selector, SelectionKey.OP_READ);
		
		while (selector.select() > 0) {
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			while (it.hasNext()) {
				SelectionKey sk = it.next();
				if (sk.isReadable()) {
					ByteBuffer buf = ByteBuffer.allocate(1024);
					dc.receive(buf);
					buf.flip();
					System.out.println(new String(buf.array(),0,buf.limit()));
					buf.clear();
				}
			}
			it.remove();
		}
	
	}
}
