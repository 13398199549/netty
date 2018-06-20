package org.framestudy.netty.helloworld;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
/**
 * 
 * @author Administrator
 *
 */
public class ClientHandler extends ChannelHandlerAdapter{
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			ByteBuf buf = (ByteBuf) msg;
			
			byte[] req = new byte[buf.readableBytes()];
			buf.readBytes(req);
			//从服务器端，接到到的消息
			String body = new String(req, "utf-8");
			System.out.println("Client :" + body );
		} finally {
			//释放缓冲区间，ByteBuf采用的是一个引用计数器的方式，来完成
			//对数据进行缓冲,服务端不需要，因为他执行过WriteAndFlush()
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		ctx.close();
	}
	
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
	
	}

//	@Override
//	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//
//	}

}
