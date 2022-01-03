/*
 * Copyright 2019 成都深地领航能源科技有限公司. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sdlh.demo.socket;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author 成都深地领航能源科技有限公司
 */
public class Server {
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(9952);
            System.out.println("服务器端socket启动...");
            Socket socket = serverSocket.accept();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            new Thread(() -> {
                try {
                    while (true) {
                        Thread.sleep(2000);
                        System.out.println("心跳检测");
                        bw.write("heart beat!\n");
                        bw.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("心跳检测失败，关闭serversocket");
                    close(socket);
                }
            }).start();
            InputStream in = socket.getInputStream();
            byte[] buf = new byte[1024];
            try {
                while (in.read(buf) > 0) {
                    System.out.println("客户端:"+socket.getInetAddress().getLocalHost()+"已连接到服务器");
                    //读取客户端发送来的消息
                    String msg = new String(buf);
                    System.out.println("收到客户端消息：" + msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("客户端socket断开，关闭serversocket");
            close(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
