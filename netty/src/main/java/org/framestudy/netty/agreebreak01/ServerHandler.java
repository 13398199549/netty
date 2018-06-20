package org.framestudy.netty.agreebreak01;

import io.netty.buffer.Unpooled;
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
		String request = (String)msg;
		System.out.println("Server :" + request);
		String response = "服务器响应：" + request + "$_";
		ctx.writeAndFlush(Unpooled.copiedBuffer
				(response.getBytes()));
		//完成短连接与长连接的转换
//		.addListener(ChannelFutureListener.CLOSE);
	}
	
	/**
	 * 从通道中将数据读取完毕后的方法
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		
	}
	
	/**
	 * 异常抛出后的方法
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) throws Exception {
		ctx.close();
	}




}
