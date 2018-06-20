package org.framestudy.netty.helloworld;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {

	public static void main(String[] args) throws Exception {
		
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
			
			/*ChannelOption.SO_BACKLOG 
			     初始化服务端可连接队列，
		　　　　服务端处理客户端连接请求是顺序处理的，
			     所以同一时间只能处理一个客户端连接，
		              多个客户端来的时候，服务端将不能处理的客户端连接请求
		              放在队列中等待处理，backlog参数指定了队列的大小
			*/
			
		    .option(ChannelOption.SO_BACKLOG, 1024)		//设置tcp缓冲区
			.option(ChannelOption.SO_SNDBUF, 32*1024)	//设置发送缓冲大小
			.option(ChannelOption.SO_RCVBUF, 32*1024)	//这是接收缓冲大小
			.option(ChannelOption.SO_KEEPALIVE, true)	//保持连接
			
			//SocketChannel 代表的就是每一个独立的Socket连接通道
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel sc) throws Exception {
					//3 在这里配置具体数据接收方法的处理
					//ServerHandler 业务逻辑处理类
					//addLast()是指一个一个的追加
					sc.pipeline().addLast(new ServerHandler());
					//还可以同时追加多个业务逻辑处理类
//					sc.pipeline().addLast(new ServerHandler2());
				}
			});
			
			//4 进行端口绑定 ，并完成异步阻塞（对监听端口线程，进行阻塞）
			cf1 = b.bind(8765).sync();
			//ChannelFuture cf2 = b.bind(8764).sync();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			//以下的步骤，应该放置在finally内部
			//5 等待关闭
			cf1.channel().closeFuture().sync();
			//cf2.channel().closeFuture().sync();
			pGroup.shutdownGracefully();
			cGroup.shutdownGracefully();
		}
		
	}
}
