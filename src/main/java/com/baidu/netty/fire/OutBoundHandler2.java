package com.baidu.netty.fire;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;

public class OutBoundHandler2 extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf data = (ByteBuf) msg;
        System.out.println("OutBoundHandler2 write: " + data.toString(CharsetUtil.UTF_8));
        ctx.write("OutBoundHandler2: " + data.toString(CharsetUtil.UTF_8));
        ctx.flush();
    }
}
