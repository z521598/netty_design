package com.baidu.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class TestInChannelHandler extends ChannelInboundHandlerAdapter {
	private String s;

	public TestInChannelHandler(String s) {
		this.s = s;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("TestInChannelHandler channelRead");
		ByteBuf byteBuf = (ByteBuf) msg;
		String str = byteBuf.toString(CharsetUtil.UTF_8);
		System.out.println(str);
		ctx.fireChannelRead(Unpooled.copiedBuffer(str + s, CharsetUtil.UTF_8));
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("TestInChannelHandler channelReadComplete");
		ctx.close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("TestInChannelHandler exceptionCaught");
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("TestInChannelHandler channelActive");
		ctx.writeAndFlush("msg");
	}

	public TestInChannelHandler() {
		super();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("TestInChannelHandler channelRegistered");
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("TestInChannelHandler channelUnregistered");
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("TestInChannelHandler channelInactive");
		super.channelInactive(ctx);
	}
}