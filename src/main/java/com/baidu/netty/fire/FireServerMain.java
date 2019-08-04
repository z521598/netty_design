package com.baidu.netty.fire;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

public class FireServerMain {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(1);
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // 入站消息顺序
                        // 出站消息逆序
                        // InBoundHandler之间一般通过ctx.fire方法传递
                        // InBoundHandler传递到OutBoundHandler时，一般通过ctx.write()方法，需要把InBoundHandler方在结尾
                        socketChannel.pipeline()
//                                .addLast(new InBoundHandler1())
//                                .addLast(new InBoundHandler2())
//                                .addLast(new OutBoundHandler1())
//                                .addLast(new OutBoundHandler2());
                        /*
                        InBoundHandler1 receive: a

                        InBoundHandler2 receive: InBoundHandler1: a
                         */
                                .addLast(new OutBoundHandler1())
                                .addLast(new OutBoundHandler2())
                                .addLast(new InBoundHandler1())
                                .addLast(new InBoundHandler2());
                        /*
                        InBoundHandler1 receive: a
                        InBoundHandler2 receive: InBoundHandler1: a
                        OutBoundHandler2 receive: InBoundHandler2: InBoundHandler1: a
                         */

                    }
                });
        ChannelFuture f = bootstrap.bind(80).sync();
        f.channel().closeFuture().syncUninterruptibly();


    }


}
