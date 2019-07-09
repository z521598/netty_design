package com.baidu.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioSocketChannel;
import io.netty.util.CharsetUtil;

public class HelloWorldClientMain {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup eventExecutors = new OioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventExecutors)
                .channel(OioSocketChannel.class)
                .remoteAddress("127.0.0.1", 80)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ClientHandler("i1"))
                                .addLast(new ClientHandler("i2"))
                                .addLast(new ClientHandler("i3"));
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect().sync();
        channelFuture.channel().closeFuture().sync();
    }

    @ChannelHandler.Sharable
    public static class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
        private String initMsg;

        public ClientHandler() {
        }

        public ClientHandler(String initMsg) {
            this.initMsg = initMsg;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            Thread.sleep(2000);
            String msg = "EventLoopGroup eventExecutors = new OioEventLoopGroup();\n" +
                    "        Bootstrap bootstrap = new Bootstrap();\n" +
                    "        bootstrap.group(eventExecutors)\n" +
                    "                .channel(OioSocketChannel.class)\n" +
                    "                .remoteAddress(\"127.0.0.1\", 80)\n" +
                    "                .handler(new ChannelInitializer<SocketChannel>() {\n" +
                    "                    @Override\n" +
                    "                    protected void initChannel(SocketChannel ch) throws Exception {\n" +
                    "                        ch.pipeline().addLast(new ClientHandler());\n" +
                    "                    }\n" +
                    "                });\n" +
                    "        ChannelFuture channelFuture = bootstrap.connect().sync();\n" +
                    "        channelFuture.channel().closeFuture().sync();\n";
            msg += initMsg;
            ctx.writeAndFlush(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            System.out.println("received:" + msg.toString(CharsetUtil.UTF_8));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
