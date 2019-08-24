package com.baidu.netty.my;

import java.util.List;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ServerMain {
	public static void main(String[] args) {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(new NioEventLoopGroup())
				.channel(NioServerSocketChannel.class)
				.childOption(ChannelOption.TCP_NODELAY, true)
				.childHandler(new ChannelInitializer<Channel>() {
					// 1 -> 2 -> 3
					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline()
								.addLast(new StringEncoder())
								.addLast(new ChannelOutboundHandlerAdapter() {
									@Override
									public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
										System.out.println("write");
										System.out.println(msg);
										ctx.write((String) msg + (String) msg);
									}
								})
								//.addLast(new FixedLengthFrameDecoder(4))
								.addLast(new StringDecoder())
								.addLast(new ChannelInboundHandlerAdapter() {
									@Override
									public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
										System.out.println(msg);
										ctx.write(msg + "111");
									}

									@Override
									public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
										System.out.println("channelReadComplete");
										ctx.flush();
									}
								});
					}
				});
		serverBootstrap.bind(81);

	}
}
