package com.sdlh.demo.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServer {
    public static void main(String[] args) {
        // 1. 首先创建 两个线程组 BossGroup和WorkerGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {

            // 2. 创建服务端启动类，配置启动参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            // 3. 配置具体的参数，配置具体参数
            /**
             * 3.1 配置group
             * 3.2 使用 NioServerSocketChannel 作为服务器的通道实现
             * 3.3 设置具体的Handler
             */
            serverBootstrap.group(bossGroup, workGroup)
                           .channel(NioServerSocketChannel.class)
                           .childHandler(new ChannelInitializer<SocketChannel>() {
                               @Override
                               protected void initChannel(SocketChannel channel) throws Exception {
                                   //4. 给 pipeline 添加处理器，每当有连接accept时，就会运行到此处。
                                   channel.pipeline().addLast(new ServerHandler());
                               }
                           });
            log.info("Netty Server is ready...");
            // 5. 绑定端口并且同步，生成了一个ChannelFuture 对象
            ChannelFuture channelFuture = serverBootstrap.bind("localhost", 7322).sync();
            // 6. 对channel进行关闭，注意这里全部都是异步操作
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
