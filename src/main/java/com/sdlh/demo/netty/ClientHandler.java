package com.sdlh.demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * 用于与服务器建立连接之后被调用，一般用于建立之后发送消息。
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client: " + ctx);
        // 给服务器发送消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello ,服务器", CharsetUtil.UTF_8));
    }

    /**
     * 从服务器接收到一条消息后被调用
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("服务器说: " + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器地址为: " + ctx.channel().remoteAddress());
    }

    /**
     * 处理异常信息
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
