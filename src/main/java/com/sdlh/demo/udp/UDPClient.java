package com.sdlh.demo.udp;

import lombok.extern.slf4j.Slf4j;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * UDP包发送类
 *
 * @author 成都深地领航能源科技有限公司
 */
@Slf4j
public class UDPClient {
    public static void main(String[] args) {
        try {
            // 客服端建立连接，ip地址是本地，端口号是10000
            DatagramSocket client = new DatagramSocket(10000);
            String data = "hello udp program!";
            byte[] datas = data.getBytes();
            //指定发往的目的地
            DatagramPacket packet = new DatagramPacket(datas,
                                                       0,
                                                       datas.length,
                                                       new InetSocketAddress("localhost", 7331));
            client.send(packet);
            client.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
