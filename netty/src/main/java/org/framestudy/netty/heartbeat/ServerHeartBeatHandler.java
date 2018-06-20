
package org.framestudy.netty.heartbeat;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ServerHeartBeatHandler extends ChannelHandlerAdapter {

	private static final String SUCCESS_KEY = "auth_success_key";
	/**
	 * 认证这个地方，请自己按照自己的业务，重新设计
	 * @param ctx
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	private boolean auth(ChannelHandlerContext ctx, Object msg) throws Exception {
		// System.out.println(msg);
		String body = (String) msg;
		if (body != null && !"".equals(body)) {
			ctx.writeAndFlush(SUCCESS_KEY);
			return true;
		} else {
			ctx.writeAndFlush("Server ：auth failure !").addListener(ChannelFutureListener.CLOSE);
			return false;
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof String) {
			auth(ctx, msg);
		} else if (msg instanceof RequestInfo) {
			RequestInfo info = (RequestInfo) msg;
			System.out.println("Server ：" + info);
			
			ctx.writeAndFlush("Server ：info received!");
		} else {
			ctx.writeAndFlush("Server ：connect failure!").addListener(ChannelFutureListener.CLOSE);
		}
	}

}
