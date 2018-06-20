
package org.framestudy.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;


public class Server {
    public void run(int port) throws Exception {
    	
    	EventLoopGroup group = new NioEventLoopGroup();
		try {
		    Bootstrap b = new Bootstrap();
		    b.group(group).channel(NioDatagramChannel.class)//指定使用UDP方式来完成通信
			    .option(ChannelOption.SO_BROADCAST, true)//使用广播模式进行通信
			    .handler(new ServerHandler());
		    b.bind(port).sync().channel().closeFuture().await();
		} finally {
		    group.shutdownGracefully();
		}
    }

    public static void main(String[] args) throws Exception {
		new Server().run(8765);
		new Server().run(8764);
    }
}
