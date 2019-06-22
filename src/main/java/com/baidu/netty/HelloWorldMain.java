package com.baidu.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HelloWorldMain {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                byte[] reg = new byte[buf.readableBytes()];
                                buf.readBytes(reg);
                                String body = new String(reg, "UTF-8");
                                StringBuilder messageBuilder = new StringBuilder();
                                messageBuilder.append("HTTP/1.1 ").append(200).append(" ").append("OK").append("\n");
                                messageBuilder.append("Content-Type: ").append("text/html;charset=UTF-8").append("\n");
                                messageBuilder.append("Content-Length: ").append(body.length() + 26).append("\n");
                                messageBuilder.append("\n");
                                messageBuilder.append("<html><body>");
                                messageBuilder.append(body);
                                messageBuilder.append("</body></html>");
                                String message = messageBuilder.toString();

                                ByteBuf respByteBuf = Unpooled.copiedBuffer(message.getBytes());
                                ctx.write(respByteBuf);
                            }

                            @Override
                            public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                                ctx.flush();
                            }
                        });
                    }
                });
        ChannelFuture f = bootstrap.bind(80).sync();
        f.channel().closeFuture().sync();

    }


}
