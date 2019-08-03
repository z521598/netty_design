package com.baidu.netty.future;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public class FutureDemo {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup eventExecutors = new NioEventLoopGroup();

        Channel channel = new NioSocketChannel();
        eventExecutors.register(channel);

        ChannelFuture channelFuture = channel.connect(new InetSocketAddress("www.baidu.com", 80));
        System.out.println("addListenerFuture start");
        channelFuture.addListener(new SendMessageChannelFutureListener());
        System.out.println("addListenerFuture end");
        channelFuture.channel().closeFuture().sync();
    }

    public static class SendMessageChannelFutureListener implements ChannelFutureListener {

        private String getMethodMessage = "GET / HTTP/1.1\n" +
                "Host: www.baidu.com\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Connection: Keep-Alive\n" +
                "User-agent: Mozilla/5.0.\n" +
                "\n" +
                "name=world";

        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            System.out.println("SendMessageChannelFutureListener operationComplete");
            if (future.isSuccess()) {
                ByteBuf byteBuf = Unpooled.copiedBuffer(getMethodMessage, CharsetUtil.UTF_8);
                ChannelFuture wf = future.channel().writeAndFlush(byteBuf);
                wf.channel().pipeline().addLast(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new PrintChannelHandler());
                    }
                });
            }
            System.out.println("SendMessageChannelFutureListener operationComplete end");
        }


    }

    public static class PrintChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
//            System.out.println("本次read0数据大小: " + msg.toString(CharsetUtil.UTF_8).length());
            System.out.println(msg.toString(CharsetUtil.UTF_8));
        }
    }
}
