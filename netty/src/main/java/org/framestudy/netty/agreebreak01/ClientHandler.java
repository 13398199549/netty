package org.framestudy.netty.agreebreak01;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
/**
 * 客户端消息处理类
 * @author Administrator
 *
 */
public class ClientHandler extends ChannelHandlerAdapter{
	
	/**
	 * 通道激活后的方法
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("client channel active... ");
	}
	
	/**
	 * 从通道中读取数据的方法
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			String response = (String)msg;
			System.out.println("Client: " + response);
		} finally {
			ReferenceCountUtil.release(msg);
		}
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
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

}
