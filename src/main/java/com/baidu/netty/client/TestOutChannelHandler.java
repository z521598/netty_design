package com.baidu.netty.client;

import java.net.SocketAddress;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class TestOutChannelHandler extends ChannelOutboundHandlerAdapter {
	private String s;

	public TestOutChannelHandler(String s) {
		this.s = s;
	}

	public TestOutChannelHandler() {
		super();
	}

	@Override
	public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
		System.out.println("TestOutChannelHandler bind");
		super.bind(ctx, localAddress, promise);
	}

	@Override
	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
		System.out.println("TestOutChannelHandler connect");
		super.connect(ctx, remoteAddress, localAddress, promise);
	}

	@Override
	public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		System.out.println("TestOutChannelHandler disconnect");
		super.disconnect(ctx, promise);
	}

	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		System.out.println("TestOutChannelHandler close");
		super.close(ctx, promise);
	}

	@Override
	public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		System.out.println("TestOutChannelHandler deregister");
		super.deregister(ctx, promise);
	}

	@Override
	public void read(ChannelHandlerContext ctx) throws Exception {
		System.out.println("TestOutChannelHandler read");
//		ctx.writeAndFlush(Unpooled.copiedBuffer("123456", CharsetUtil.UTF_8));
		super.read(ctx);
	}

	@Override
	public void flush(ChannelHandlerContext ctx) throws Exception {
		System.out.println("TestOutChannelHandler flush");
		super.flush(ctx);
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		System.out.println("TestOutChannelHandler write");
		super.write(ctx, msg, promise);
	}

}
