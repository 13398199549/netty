package org.framestudy.netty.serial;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Server {

	public static void main(String[] args) throws Exception{
		ChannelFuture cf1 = null;
		//1 创建线两个程组 
		//一个是用于处理服务器端接收客户端连接的线程组
		//一个是进行网络通信的（网络读写的）线程组
		//实现连接维护，以及业务逻辑处理的分离
		EventLoopGroup pGroup = null;
		EventLoopGroup cGroup = null;
		try {
			pGroup = new NioEventLoopGroup();
			cGroup = new NioEventLoopGroup();
			
			//2 创建辅助工具类，用于服务器通道的一系列配置
			ServerBootstrap b = new ServerBootstrap();
			b.group(pGroup, cGroup)		//绑定俩个线程组
			.channel(NioServerSocketChannel.class)		//指定NIO的模式
		    .option(ChannelOption.SO_BACKLOG, 1024)		//设置tcp缓冲区
			.handler(new LoggingHandler(LogLevel.INFO))
			
			//SocketChannel 代表的就是每一个独立的Socket连接通道
			.childHandler(new ChannelInitializer<SocketChannel>() {
				 @Override
				 protected void initChannel(SocketChannel sc) throws Exception {
					sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
					sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
					sc.pipeline().addLast(new ServerHandler());
				}
			});
			
			//4 进行端口绑定 ，并完成异步阻塞
			cf1 = b.bind(8765).sync();
			
			
			cf1.channel().closeFuture().sync();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			pGroup.shutdownGracefully();
			cGroup.shutdownGracefully();
		}
	}
}
