/*
 * Copyright 2019 成都深地领航能源科技有限公司. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sdlh.demo.socket;

import java.io.*;
import java.net.Socket;

/**
 * @author 成都深地领航能源科技有限公司
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 7322);
        //构建IO
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        //向服务器端发送一条消息
        bw.write("测试客户端和服务器通信，服务器接收到消息返回到客户端\n");
        bw.flush();

//        socket.close();

        //读取服务器返回的消息
        byte[] buf = new byte[1024];
        while (is.read(buf) > 0) {
            String msg = new String(buf);
            System.out.println("接收到服务器端消息：" + msg);
        }
    }
}
