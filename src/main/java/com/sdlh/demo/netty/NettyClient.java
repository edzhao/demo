package com.sdlh.demo.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    public static void main(String[] args) {
        // 1. 客户端需要一个事件循环组
        EventLoopGroup clientGroup = new NioEventLoopGroup();

        try {
            // 2. 创建客户端启动对象
            Bootstrap bootstrap = new Bootstrap();

            // 3. 设置启动器的相关参数
            /**
             * 3.1 设置线程组
             * 3.2 设置客户端通道的实现类（使用反射）
             * 3.3 设置具体的处理handler
             */
            bootstrap.group(clientGroup)
                     .channel(NioSocketChannel.class)
                     .handler(new ChannelInitializer< SocketChannel >() {
                         @Override
                         protected void initChannel(SocketChannel socketChannel) throws Exception {
                             // 添加客户端处理逻辑Handler
                             socketChannel.pipeline().addLast(new ClientHandler());
                         }
                     });
            System.out.println("客户端 OK...");

            // 5. 连接服务器，注意这里全部都是异步的
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 7322).sync();

            // 6. 关闭通道连接监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            clientGroup.shutdownGracefully();
        }
    }
}
