package com.sdlh.demo;

import java.io.*;

public class MWDSignalCheck {
     public static void main( String[] args ){
         String path = "D:\\workspace\\projects\\demo\\src\\main\\resources\\Data1.bin";
        MWDSignalCheck diao = new MWDSignalCheck();
//        diao.GetBinSource(path);
        readByBytes(path);
    }

    /**
     * byte[] 转 int 低字节在前（低字节序）
     * @param b
     * @return
     */
    public static int toIntH(byte[] b){
        int res = 0;
        for(int i=0;i<b.length;i++){
            res += (b[i] & 0xff) << (i*8);
        }
        return res;
    }

    /**
     * byte[] 转 int 高字节在前（高字节序）
     * @param b
     * @return
     */
    public static int toIntL(byte[] b){
        int res = 0;
        for(int i=0;i<b.length;i++){
            res += (b[i] & 0xff) << ((3-i)*8);
        }
        return res;
    }

    /**
     * int 转 byte[]   高字节在前（高字节序）
     * @param n
     * @return
     */
    public static byte[] toHH(int n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * int 转 byte[]   低字节在前（低字节序）
     * @param n
     * @return
     */
    public static byte[] toLH(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    private static void readByBytes(String path) {
        File src = new File(path);//创建源
        try(FileInputStream fis = new FileInputStream(src)) {//读取操作
            int temp;
//            byte[] bytes = "27CC".getBytes();
            byte[] bytes = new byte[4];
            while ((temp = fis.read()) != -1) {//读不到内容返回-1
                fis.read(bytes);
//                int value = Integer.parseInt(new String(bytes));
//                double value = getDouble(bytes);
//                int value = byteArrToInt(bytes);
//                int value = toIntL(bytes);
//                int value = Integer.parseInt("0027CC", 16);
//                System.out.println(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int byteArrToInt(byte[] bytes) {
        if (bytes.length > 4) {
            throw new IllegalArgumentException("bytes 长度不能大于 4 位");
        }
        return ((bytes[0] & 0xff) << 24) | ((bytes[1] & 0xff) << 16) | ((bytes[2] & 0xff) << 8) | (bytes[3] & 0xff);
    }

    public static double getDouble(byte[] b) {
        long l;
        l = b[0];
        l &= 0xff;
        l |= ((long) b[1] << 8);
        l &= 0xffff;
        l |= ((long) b[2] << 16);
        l &= 0xffffff;
        l |= ((long) b[3] << 24);
        l &= 0xffffffffl;
        l |= ((long) b[4] << 32);
        l &= 0xffffffffffl;
        l |= ((long) b[5] << 40);
        l &= 0xffffffffffffl;
        l |= ((long) b[6] << 48);
        l &= 0xffffffffffffffl;
        l |= ((long) b[7] << 56);
        return Double.longBitsToDouble(l);
    }

    // 获取二进制文件
    // sourceBin:二进制文件的路径(源数据)
    public void GetBinSource( String SourceBinPath )
    {
        FileInputStream file = null;
        DataInputStream dis = null;

        try {
            file = new FileInputStream(SourceBinPath);
            byte[] b = new byte[file.available()];
            file.read(b);
            dis = new DataInputStream( new ByteArrayInputStream(b));
//            float v = dis.readFloat();
            while (dis.read() != -1) {
                System.out.println( dis.readInt() );
            }
//            int a;
//            int b;
//            a = 10;
//            b = 20;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
