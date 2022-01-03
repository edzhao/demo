/*
 * Copyright 2019 成都深地领航能源科技有限公司. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sdlh.demo.udp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * UDP包接收解析类
 *
 * @author 成都深地领航能源科技有限公司
 */
@Slf4j
public class UDPServer {
    public static void main(String[] args) {
        try {
            // 1.构建DatagramSocket实例，指定本地端口
            DatagramSocket socket = new DatagramSocket(10000);
            byte[] buf = new byte[1024];
            // 2.构建需要收发的DatagramPacket报文--构造 DatagramPacket，用来接收长度为 length 的数据包。
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            while (true) {
                // 收报文---从此套接字接收数据报包，此方法在接收到数据报前一直阻塞
                socket.receive(packet);
                log.info("ip:" + packet.getAddress().getHostAddress());
                // 接收信息
                String receive = new String(packet.getData(), packet.getLength());
                log.info("接收信息" + receive);
                // 发报文
                String info = receive + "您好！";
                // 构造数据报包，用来将长度为 length 的包发送到指定主机上的指定端口号。
                DatagramPacket dp = new DatagramPacket(info.getBytes(), info.getBytes().length, packet.getAddress(), 10000);
                // 为此包设置数据缓冲区。将此 DatagramPacket 的偏移量设置为 0，长度设置为 buf 的长度
                dp.setData(info.getBytes());
                // 从此套接字发送数据报包
                socket.send(dp);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
