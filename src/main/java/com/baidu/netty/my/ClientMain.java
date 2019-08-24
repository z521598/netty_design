package com.baidu.netty.my;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

public class ClientMain {
	public static void main(String[] args) throws UnknownHostException {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(new NioEventLoopGroup(1))
				.channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
				.handler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline().addLast(new ChannelOutboundHandlerAdapter() {
							@Override
							public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
								String msgNew1 = ((ByteBuf) msg).toString(CharsetUtil.UTF_8);
//								System.out.println("recv" + msgNew1);
								ctx.write(Unpooled.copiedBuffer(msgNew1 + "90-=", CharsetUtil.UTF_8));
							}

							@Override
							public void read(ChannelHandlerContext ctx) throws Exception {
								System.out.println("read");
								super.read(ctx);
							}
						}).addLast(new ChannelOutboundHandlerAdapter() {
							@Override
							public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
								String msgNew2 = ((ByteBuf) msg).toString(CharsetUtil.UTF_8);
//								System.out.println("recv" + msgNew2);
								ctx.writeAndFlush(Unpooled.copiedBuffer(msgNew2 + "5678", CharsetUtil.UTF_8));
							}
						}).addLast(new ChannelInboundHandlerAdapter() {
							@Override
							public void channelActive(ChannelHandlerContext ctx) throws Exception {
								System.out.println("channelActive");
								ctx.write(Unpooled.copiedBuffer("1234", CharsetUtil.UTF_8));
							}

							@Override
							public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
								System.out.println("channelRead");
								System.out.println(((ByteBuf) msg).toString(CharsetUtil.UTF_8));
								super.channelRead(ctx, msg);
							}

							@Override
							public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
								ctx.flush();
							}
						});
					}
				});
		Channel channel = bootstrap.connect(InetAddress.getLocalHost(), 81).channel();
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("请输入");
			String line = scanner.nextLine();
			//发送请求
			channel.writeAndFlush(Unpooled.copiedBuffer(line, CharsetUtil.UTF_8));
		}
	}
}
