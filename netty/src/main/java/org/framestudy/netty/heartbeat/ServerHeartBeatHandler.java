
package org.framestudy.netty.heartbeat;

import org.framestudy.netty.utils.StringUtils;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 服务端心跳维护处理类
 * @author Administrator
 *
 */
public class ServerHeartBeatHandler extends ChannelHandlerAdapter {

	private static final String SUCCESS_KEY = "auth";
	/**
	 * 认证这个地方，请自己按照自己的业务，重新设计
	 * @param ctx
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	private boolean auth(ChannelHandlerContext ctx, Object msg) throws Exception {
		String body = (String) msg;
		//IP地址不为空，既为认证信息
		if (!StringUtils.isEmpty(body)) {
			ctx.writeAndFlush(SUCCESS_KEY);
		} else {
			return false;
		}
		return true;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof String) {
			auth(ctx, msg);
		} else if (msg instanceof HeartInfo) {
			//解析心跳数据
			HeartInfo info = (HeartInfo) msg;
			System.out.println("Server ：" + info);
			ctx.writeAndFlush("Server ：info received!");
		}
	}

}
