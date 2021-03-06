package org.framestudy.netty.agreebreak03;

import java.io.File;
import java.io.FileInputStream;

import org.framestudy.netty.utils.GzipUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
	
	public static void main(String[] args) throws Exception{
		
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group)
		 .channel(NioSocketChannel.class)
		 .handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				
				//设置Jboss Marshalling的2个编码器，完成二进制与对象之间的序列化操作
				sc.pipeline().addLast(MarshallingCodeCFactory.
						buildMarshallingDecoder());
				sc.pipeline().addLast(MarshallingCodeCFactory.
						buildMarshallingEncoder());
				sc.pipeline().addLast(new ClientHandler());
			}
		});
		
		ChannelFuture cf = b.connect("127.0.0.1", 8765).sync();
		
		for(int i = 1; i <= 6; i++ ){
			Req req = new Req();
			req.setId("" + i); req.setName("pro" + i);
			req.setRequestMessage("数据信息" + i);
			//------如果不做附件传输，可以不添加下面这段代码------
			req.setFileName("00"+i+".jpg");
			String path = System.getProperty("user.dir") + File.separatorChar + "sources" +  File.separatorChar + "00"+i+".jpg";
			File file = new File(path);
	        FileInputStream in = new FileInputStream(file);  
	        byte[] data = new byte[in.available()];  
	        in.read(data); in.close(); 
			req.setAttachment(GzipUtils.gzip(data));
			//-------向服务端发送req序列化对象-------
			cf.channel().writeAndFlush(req);
		}
		
		//等待连接关闭
		cf.channel().closeFuture().sync();
		group.shutdownGracefully();
	}
}
