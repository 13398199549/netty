package org.framestudy.netty.agreebreak01;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class Client {

	public static void main(String[] args) throws Exception {
		// 创建用于负责接收服务端数据的连接组
		EventLoopGroup group = new NioEventLoopGroup();

		// 创建客户端辅助类
		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				// 设置特殊分隔符，并使用工具类，将String类型的$_转成ByeteBuf类型的数据
				ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
				sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));
				sc.pipeline().addLast(new StringDecoder());
				sc.pipeline().addLast(new ClientHandler());// 添加客户端消息处理类
			}
		});

		// 通过IP与端口连接服务端
		ChannelFuture cf = b.connect("127.0.0.1", 8765).sync();

		// 向服务端写入数据
		cf.channel().writeAndFlush(Unpooled.
				wrappedBuffer("bbbb$_".getBytes()));
		cf.channel().writeAndFlush(Unpooled.
				wrappedBuffer("cccc$_".getBytes()));

		// 等待客户端端口关闭
		cf.channel().closeFuture().sync();
		group.shutdownGracefully();

	}
}
