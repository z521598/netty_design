package com.baidu.netty.client;

import java.net.InetAddress;
import java.net.UnknownHostException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
	public static void main(String[] args) throws UnknownHostException {
		NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(nioEventLoopGroup)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer() {
					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline().addLast("in1", new TestInChannelHandler("in1"))
								.addLast("flush", new ChannelInboundHandlerAdapter() {
									@Override
									public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
										System.out.println("write anf flush");
										ctx.writeAndFlush(msg);
									}
								})
								.addFirst("out2", new TestOutChannelHandler("out2"));
//						ch.pipeline().addLast("codec3", new TestCodecChannelHandler("codec3"));
					}
				});

		ChannelFuture future = bootstrap.connect(InetAddress.getLocalHost(), 80);
		future.syncUninterruptibly();
	}
}
