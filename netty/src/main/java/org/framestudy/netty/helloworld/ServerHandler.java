package org.framestudy.netty.helloworld;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
/**
 * 必须继承ChannelHandlerAdapter类
 * @author Administrator
 *
 */
public class ServerHandler extends ChannelHandlerAdapter {

	/**
	 * channelRead()该方法，主要用于从管道中，读取数据 
	 * msg 真正从客户端传递过来的数据
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
			ByteBuf buf = (ByteBuf) msg;
			byte[] req = new byte[buf.readableBytes()];
			buf.readBytes(req);
			//从客户端接收到的数据
			String body = new String(req, "utf-8");
			System.out.println("Server :" + body );
			String response = "进行返回给客户端的响应：" + body ;
			
			/*Unpooled.copiedBuffer(response.getBytes())
			将一个字节数组，转换成Buffer数组
			*/
			
			ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));
			
			/**
			 * 如果我们需要将连接设置为"短连接"，那么我们可以添加如下监听器
			 * 但是一般，我们都是保持"长连接"进行数据通信
			 */
			//.addListener(ChannelFutureListener.CLOSE);
	}
	/**
	 * 抛出了异常后的处理方法
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable t)
			throws Exception {
		ctx.close();
	}
	
	/**
	 * 从服务器通道中将数据读取完毕后的方法
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx)
			throws Exception {
		System.out.println("读完了");
		ctx.flush();
	}
	
	/**
	 * 服务器通道激活时的方法
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("server channel active... ");
	}

}
