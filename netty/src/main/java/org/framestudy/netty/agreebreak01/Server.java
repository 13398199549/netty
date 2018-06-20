package org.framestudy.netty.agreebreak01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class Server {

	public static void main(String[] args) throws Exception{
		//创建用于负责接收客户端数据的连接组
		EventLoopGroup pGroup = new NioEventLoopGroup();
		//创建用于负责进行数据传输的连接组
		EventLoopGroup cGroup = new NioEventLoopGroup();
		
		//创建服务端辅助类
		ServerBootstrap b = new ServerBootstrap();
		b.group(pGroup, cGroup)
		 .channel(NioServerSocketChannel.class)//采用NIO方式进行创建服务端程序
		 .option(ChannelOption.SO_BACKLOG, 1024)//定义连接处理缓冲队列
		 .option(ChannelOption.SO_SNDBUF, 32*1024)//定义数据发送缓冲区大小
		 .option(ChannelOption.SO_RCVBUF, 32*1024)//定义数据接收缓冲区大小
		 .option(ChannelOption.SO_KEEPALIVE, true)//保持连接处于激活状态
		 .childHandler(new ChannelInitializer<SocketChannel>() {
			
			 @Override
			protected void initChannel(SocketChannel sc) throws Exception {
				//设置特殊分隔符，并使用工具类，将String类型的$_转成ByeteBuf类型的数据
				ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
				
				/* DelimiterBasedFrameDecoder
				 * 作用主要是从特殊分隔符处，将报文进行拆分
				 * 2个参数：
				 * 第一个参数是指特殊分割符的最大长度
				 * 第二个参数是指特殊分隔符
				*/
				
				sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,
						buf));
				//设置字符串形式的解码，将ByteBuf直接转换成String类型的数据
				sc.pipeline().addLast(new StringDecoder());
				sc.pipeline().addLast(new ServerHandler());
			}
		});
		
		//4 绑定连接（同时还可以开发多个端口）
		ChannelFuture cf = b.bind(8765).sync();
		
		//等待服务器监听端口关闭
		cf.channel().closeFuture().sync();
		pGroup.shutdownGracefully();
		cGroup.shutdownGracefully();
	}
	
}
