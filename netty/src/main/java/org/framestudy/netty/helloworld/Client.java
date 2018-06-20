package org.framestudy.netty.helloworld;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

	public static void main(String[] args) throws Exception {
		
		ChannelFuture cf1 = null;
		//服务器端，才需要定义用于处理服务器端接收客户端连接的线程组
		//客户端，只有1个连接，不需要定义，只需要定义一个读写线程组即可
		EventLoopGroup group = null;

		try {
			group = new NioEventLoopGroup();
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel sc) throws Exception {
					sc.pipeline().addLast(new ClientHandler());
				}
			});

			cf1 = b.connect("127.0.0.1", 8765).sync();
			// ChannelFuture cf2 = b.connect("127.0.0.1", 8764).sync();
			
			
			// 发送消息
			Thread.sleep(1000);
			cf1.channel().writeAndFlush(Unpooled.copiedBuffer("777".getBytes()));
			Thread.sleep(1000);
			cf1.channel().writeAndFlush(Unpooled.copiedBuffer("666".getBytes()));
			Thread.sleep(1000);
			cf1.channel().writeAndFlush(Unpooled.copiedBuffer("888".getBytes()));
			
			// cf2.channel().writeAndFlush(Unpooled.copiedBuffer("888".getBytes()));
			//Thread.sleep(1000);
			// cf2.channel().writeAndFlush(Unpooled.copiedBuffer("666".getBytes()));

			
			/*
			 * 写数据的方法，也不只有writeAndFlush();
			 * 也可以采用如下的方式,但是容易出现“粘包”问题，
			 * 解决方案，需要参考：agreebreak01或者02
			
			cf1.channel().write(Unpooled.copiedBuffer("777".getBytes()));
			cf1.channel().write(Unpooled.copiedBuffer("888".getBytes()));
			cf1.channel().write(Unpooled.copiedBuffer("999".getBytes()));
			cf1.channel().flush();
			*/
			
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			// TODO: handle finally clause
			cf1.channel().closeFuture().sync();
			// cf2.channel().closeFuture().sync();
			group.shutdownGracefully();
		}
	}
}
