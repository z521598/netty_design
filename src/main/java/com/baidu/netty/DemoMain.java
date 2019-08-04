package com.baidu.netty;

import com.baidu.netty.bytebuf.ByteBufTestChannelHandler;
import com.baidu.netty.fire.InBoundHandler1;
import com.baidu.netty.fire.InBoundHandler2;
import com.baidu.netty.fire.OutBoundHandler1;
import com.baidu.netty.fire.OutBoundHandler2;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DemoMain {

	public static void main(String[] args) throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup(1);
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					protected void initChannel(SocketChannel socketChannel) throws Exception {
						socketChannel.pipeline().addLast("1", new ByteBufTestChannelHandler());
						socketChannel.pipeline().addLast("2", new ByteBufTestChannelHandler());
						socketChannel.pipeline().addLast("3", new ByteBufTestChannelHandler());
					}
				});
		ChannelFuture f = bootstrap.bind(80).sync();
		f.channel().closeFuture().syncUninterruptibly();

	}

}
