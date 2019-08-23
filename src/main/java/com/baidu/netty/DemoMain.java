package com.baidu.netty;

import java.net.SocketAddress;

import com.baidu.netty.bytebuf.ByteBufTestChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

public class DemoMain {

	public static void main(String[] args) throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup(1);
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					protected void initChannel(SocketChannel socketChannel) throws Exception {
						socketChannel.pipeline().addLast("1", new ChannelInboundHandlerAdapter() {

							@Override
							public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
								ByteBuf byteBuf = (ByteBuf) msg;
								String byteStr = byteBuf.toString(CharsetUtil.UTF_8);
								System.out.println("====");
								System.out.println(byteStr);
								System.out.println("====");
								ctx.write(Unpooled.copiedBuffer(byteStr + "111", CharsetUtil.UTF_8));
								ctx.flush();
								System.out.println("writeAndFlush");
							}

							@Override
							public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
								System.out.println("1 channelReadComplete");
								super.channelReadComplete(ctx);
							}

							@Override
							public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
								System.out.println("1 channelRegistered");
							}

							@Override
							public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
								System.out.println("1 channelUnregistered");
							}

							@Override
							public void channelActive(ChannelHandlerContext ctx) throws Exception {
								System.out.println("1 channelActive");
//								ctx.write("msg");
							}

							@Override
							public void channelInactive(ChannelHandlerContext ctx) throws Exception {
								System.out.println("1 channelInactive");
							}
						});

						socketChannel.pipeline().addLast(new ChannelOutboundHandlerAdapter() {

							@Override
							public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
								super.exceptionCaught(ctx, cause);
							}

							@Override
							public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
								System.out.println("2 write");
								String msgStr = (String) msg;
								ctx.writeAndFlush(Unpooled.copiedBuffer(msgStr, CharsetUtil.UTF_8));
							}

							@Override
							public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
								System.out.println("2 bind");
								super.bind(ctx, localAddress, promise);
							}

							@Override
							public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
								System.out.println("2 connect");
								super.connect(ctx, remoteAddress, localAddress, promise);
							}

							@Override
							public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
								System.out.println("2 disconnect");
								super.disconnect(ctx, promise);
							}

							@Override
							public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
								System.out.println("2 close");
								super.close(ctx, promise);
							}

							@Override
							public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
								System.out.println("2 deregister");
								super.deregister(ctx, promise);
							}

							@Override
							public void read(ChannelHandlerContext ctx) throws Exception {
								System.out.println("-------------");
								System.out.println("2 read");
								System.out.println("-------------");
								super.read(ctx);
							}

							@Override
							public void flush(ChannelHandlerContext ctx) throws Exception {
								System.out.println("2 flush");
								super.flush(ctx);
							}

						});
//						socketChannel.pipeline().addLast("2", new ByteBufTestChannelHandler());
//						socketChannel.pipeline().addLast("3", new ByteBufTestChannelHandler());
					}
				});
		ChannelFuture f = bootstrap.bind(80).sync();
		f.channel().closeFuture().syncUninterruptibly();

	}

}
