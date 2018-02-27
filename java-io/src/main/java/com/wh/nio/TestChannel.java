package com.wh.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;

import org.junit.Test;

/**
 * 一、通道（Channel）：用于源节点与目标节点的连接。在Java NIO中负责缓冲区的数据传输。Channel本身不存储数据，需要配合缓冲区进行传输。
 * 
 * 二、通道的主要实现类：
 *    java.nio.channels.Channel接口：
 *    FileChannel、SocketChannel、ServerSocketChannel、DatagramChannel
 * 三、 获取通道
 *   1、Java针对支持通道的类提供了getChannel()方法
 *   本地IO：
 *      FileInputStream、FileOuputStream
 *      RandomAccessFile
 *   网络IO：
 *      Socekt、ServerSocekt、DatagramSocekt
 *   2、在JDK1.7中的NIO2针对各通道提供了 静态方法open()
 *   3、在JDK1.7中的NIO2的Files工具类的newBtyeChannel()
 * 四、通道之间的数据传输
 *  transferFrom()
 *  transferTo()
 * 五、分散(Scatter)和聚集 (Gather)
 * 分散读取(Scattering Reads) : 将通道中的数据分散到多个缓冲区
 * 聚集写入(Gathering writes) : 将多个缓冲区的数据聚集到通道中
 * 
 * 六、字符集
 * 编码：字符串-->字节数组
 * 解码：字节数组-->字符串
 * 
 * @author wh
 * @date  2018年2月5日 
 *
 */
public class TestChannel {
   
	//利用通道完成文件的复制（非直接缓冲区）
	@Test
	public void test1(){//684
		long start = System.currentTimeMillis();
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		//1、获取通道
		FileChannel inChannel =  null;;
		FileChannel outChannel = null;
		try {
			fis = new FileInputStream("D:/food.pdf");
			fos = new FileOutputStream("D:/test1.pdf");
			
			inChannel = fis.getChannel();
			outChannel = fos.getChannel();
			//2、分配指定大小的缓冲区
			ByteBuffer buf = ByteBuffer.allocate(1024);
			//3、将通道中的数据存入缓冲区
			while (inChannel.read(buf)!=-1) {
				buf.flip();//切换读取数据的模式
				//4、将缓冲区的数据写入通道中
				outChannel.write(buf);
				buf.clear();//清空缓冲区
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outChannel!=null) {
				try {
					outChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (inChannel!=null) {
				try {
					inChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos!=null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis!=null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("总共耗时："+(end-start));
		
	}
	/**
	 * 传统的BIO复制文件
	 * @author wh
	 * @date 2018年2月5日 
	 */
	@Test
	public void test2() {//469
		long start = System.currentTimeMillis();
		File fileSrc = new File("D:/food.pdf");
		File fileDst = new File("D:/test2.pdf");
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			 fis = new FileInputStream(fileSrc);
			 fos = new FileOutputStream(fileDst);
			 byte[] b = new byte[1024];
			 int n = 0;
			 while ((n=fis.read(b))!=-1) {
				fos.write(b, 0, n);
			 }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis!=null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos!=null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("总共耗时："+(end-start));
	}
	
	/**
	 * 利用通道完成文件的复制（直接缓冲区，内存映射文件）
	 * 开关耗费资源，大数据量的时候使用较好
	 * @author wh
	 * @throws IOException 
	 * @date 2018年2月5日 
	 */
	@Test
	public void test3() throws IOException {//1000
		long start = System.currentTimeMillis();
		
		FileChannel inChannel = FileChannel.open(Paths.get("D:/food.pdf"), StandardOpenOption.READ);
		FileChannel outChannel = FileChannel.open(Paths.get("D:/test3.pdf"), StandardOpenOption.WRITE,StandardOpenOption.READ, StandardOpenOption.CREATE);
		
		//内存映射文件
		MappedByteBuffer inMapperBuf = inChannel.map(MapMode.READ_ONLY, 0, inChannel.size());
		MappedByteBuffer outMapperBuf = outChannel.map(MapMode.READ_WRITE, 0, inChannel.size());
		
		//直接对缓冲区进行数据的读写操作
		byte[] dst = new byte[inMapperBuf.limit()];
		inMapperBuf.get(dst);
		outMapperBuf.put(dst);
		
		inChannel.close();
		outChannel.close();
		
		long end = System.currentTimeMillis();
		System.out.println("总共耗时："+(end-start));
	}
	
	/**
	 * 通道之间的数据传输（直接缓冲区）
	 * @author wh
	 * @throws IOException 
	 * @date 2018年2月6日 
	 */
	@Test
	public void test4() throws IOException {//813
		long start = System.currentTimeMillis();
		
		FileChannel inChannel = FileChannel.open(Paths.get("D:/food.pdf"), StandardOpenOption.READ);
		FileChannel outChannel = FileChannel.open(Paths.get("D:/test4.pdf"), StandardOpenOption.WRITE,StandardOpenOption.READ,StandardOpenOption.CREATE);
		inChannel.transferTo(0, inChannel.size(), outChannel);
		outChannel.transferFrom(inChannel, 0, inChannel.size());
		inChannel.close();
		outChannel.close();
		
		long end = System.currentTimeMillis();
		System.out.println("总共耗时："+(end-start));
	}
	
	/**
	 * 分散和聚集
	 * 分散读取时缓冲区会按数组一个一个放入
	 * @author wh
	 * @throws Exception 
	 * @date 2018年2月6日
	 */
	@Test
	public void test5() throws Exception {
		RandomAccessFile raf = new RandomAccessFile("D:/1.txt", "rw");
		//1、获取通道
		FileChannel channel = raf.getChannel();
		
		//2、分配指定大小的缓冲区
		ByteBuffer buf1 = ByteBuffer.allocate(100);
		ByteBuffer buf2 = ByteBuffer.allocate(1024);
		
		//3、分散读取
		ByteBuffer[] bufs = {buf1,buf2};
		channel.read(bufs);
		for (ByteBuffer byteBuffer : bufs) {
			byteBuffer.flip();
		}
		System.out.println(new String(bufs[0].array(),0,bufs[0].limit()).length());//100
		System.out.println("-------------");
		System.out.println(new String(bufs[1].array(),0,bufs[1].limit()).length());//124
		
		//4、聚集写入
		RandomAccessFile rafw = new RandomAccessFile("D:/test5.txt", "rw");
		FileChannel channelw = rafw.getChannel();
		channelw.write(bufs);
	}
	
	/**
	 * 查看所有的编码集
	 * @author wh
	 * @date 2018年2月6日
	 */
	@Test
	public void test6() {
		Map<String,Charset> map = Charset.availableCharsets();
		for (Map.Entry<String, Charset> entry : map.entrySet()) { 
			  System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
		}
	}
	
	/**
	 * 字符集
	 * @author wh
	 * @throws Exception 
	 * @date 2018年2月6日
	 */
	@Test
	public void test7() throws Exception {
		Charset chs1 = Charset.forName("GBK");
		
		//获取编码器
		CharsetEncoder ce = chs1.newEncoder();
		//获取解码器
		CharsetDecoder cd = chs1.newDecoder();
		
		CharBuffer cbuf = CharBuffer.allocate(1024);
		cbuf.put("测试编码");
		cbuf.flip();
		//编码
		ByteBuffer buf = ce.encode(cbuf);
		for (int i=0; i<8;i++) {
			System.out.println(i+"--------"+buf.get());
		}
		//解码
		buf.flip();
		CharBuffer cbuf2 = cd.decode(buf);
		System.out.println(cbuf2.toString());
		System.out.println("--------");
		
		Charset chs2 = Charset.forName("GBK");
		buf.flip();
		CharBuffer cbuf3 = chs2.decode(buf);
		System.out.println(cbuf3.toString());
	}
}
