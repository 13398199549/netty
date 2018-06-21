package org.framestudy.netty.heartbeat;

import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * 客户端心跳维护处理类
 * 
 * @author Administrator
 *
 */
public class ClienHeartBeatHandler extends ChannelHandlerAdapter {

	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	private ScheduledFuture<?> heartBeat;
	// 主动向服务器发送认证信息
	private InetAddress addr;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		addr = InetAddress.getLocalHost();
		String ip = addr.getHostAddress();
		// 通道建立起来之后，先握手，建立认证体系
		ctx.writeAndFlush(ip);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			if (msg instanceof String && "auth".equals(msg)) {
				// 握手成功，主动发送心跳消息（0，代表启动就开始，30代表间隔30秒钟）
				this.heartBeat = this.scheduler.scheduleWithFixedDelay(new HeartBeatTask(ctx), 0, 30, TimeUnit.SECONDS);
			}
			
			System.out.println(msg);
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

	/**
	 * 定义心跳任务线程
	 * 
	 * @author Administrator
	 *
	 */
	private class HeartBeatTask implements Runnable {
		private final ChannelHandlerContext ctx;
		
		public HeartBeatTask(final ChannelHandlerContext ctx) {
			this.ctx = ctx;
		}

		@Override
		public void run() {
			try {
				HeartInfo info = new HeartInfo();
				//发送心跳报文
				info.setHeartInfo("客户端心跳报文：T0");
				ctx.writeAndFlush(info);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		if (heartBeat != null) {
			heartBeat.cancel(true);
			heartBeat = null;
		}
		ctx.fireExceptionCaught(cause);
	}
}
