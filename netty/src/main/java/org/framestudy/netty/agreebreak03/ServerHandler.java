package org.framestudy.netty.agreebreak03;

import java.io.File;
import java.io.FileOutputStream;

import org.framestudy.netty.utils.GzipUtils;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
/**
 * 服务器消息处理类
 * @author Administrator
 *
 */
public class ServerHandler extends ChannelHandlerAdapter {
	/**
	 * 通道激活后的方法
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(" server channel active... ");
	}
	/**
	 * 从通道中读取数据的方法
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Req req = (Req) msg; 
		System.out.println("Server : " + req);
		//------如果不做附件传输，可以不添加下面这段代码------
		byte[] attachment = GzipUtils.ungzip(req.getAttachment());
		String path = System.getProperty("user.dir") + File.separatorChar + "receive" + File.separatorChar + req.getFileName();
		FileOutputStream fos = new FileOutputStream(path);
		fos.write(attachment);//向文件中写入数据
		fos.close();
		//--------接到客户端消息后，向服务器发送响应---------
		Resp resp = new Resp();
		resp.setId(req.getId());
		resp.setName("resp" + req.getName());
		resp.setResponseMessage("响应内容 ：" + req.getRequestMessage());
		ctx.writeAndFlush(resp);
		//完成短连接与长连接的转换
		// .addListener(ChannelFutureListener.CLOSE);
	}
	
	/**
	 * 从通道中将数据读取完毕后的方法
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("server channel readcomplete... ");
	}
	
	/**
	 * 异常抛出后的方法
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

	}

}
