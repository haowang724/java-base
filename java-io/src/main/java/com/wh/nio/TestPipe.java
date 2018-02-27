package com.wh.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

import org.junit.Test;

/**
 * 管道
 * @author wh
 * @date  2018年2月8日 下午2:29:10 
 *
 */
public class TestPipe {
    /**
     * 发送
     * @author wh
     * @date 2018年2月8日 
     * @throws IOException
     */
	@Test
	public void send() throws IOException {
		//1、获取管道
		Pipe pipe = Pipe.open();
		//2、分配指定大小缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);
		//3、将缓冲区的数据写入管道
		Pipe.SinkChannel sink = pipe.sink();
		buf.put("测试管道----".getBytes());
		buf.flip();
		sink.write(buf);
		//4、将管道中的数据写入缓冲区
		Pipe.SourceChannel source = pipe.source();
		buf.flip();
		int len = source.read(buf);
		System.out.println(new String(buf.array(),0,len));
		
		//5、关闭通道
		source.close();		
		sink.close();
		
	}
}
