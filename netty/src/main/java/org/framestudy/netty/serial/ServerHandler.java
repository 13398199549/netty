package org.framestudy.netty.serial;

import java.io.File;
import java.io.FileOutputStream;

import org.framestudy.netty.utils.GzipUtils;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ServerHandler extends ChannelHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(" server channel active... ");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		Req req = (Req) msg;
		System.out.println("Server : " + req);

		
		
		//------如果不做附件传输，可以不添加下面这段代码------
		byte[] attachment = GzipUtils.ungzip(req.getAttachment());
		String path = System.getProperty("user.dir") + File.separatorChar + "receive" + File.separatorChar + req.getFileName();
		FileOutputStream fos = new FileOutputStream(path);
		fos.write(attachment);
		fos.close();
		//--------
		
		
		

		Resp resp = new Resp();
		resp.setId(req.getId());
		resp.setName("resp" + req.getName());
		resp.setResponseMessage("响应内容 ：" + req.getRequestMessage());

		ctx.writeAndFlush(resp);// .addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println(" server channel readcomplete... ");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

	}

}
