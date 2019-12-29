package net.wanho.manage_media;

import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class TestFile2 {
    @Test
    public void testChunk() throws Exception {
        //分  读1写多
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream("E:\\wlzx\\video\\mycat.mp4"), 1024 * 1024);
        BufferedOutputStream bos;
        byte[] bs = new byte[1024 * 1024];
        int len;
        int i = 1;
        while ((len = bis.read(bs, 0, bs.length)) != -1) {
            //写多
            bos = new BufferedOutputStream(new FileOutputStream("E:\\wlzx\\video\\chunks\\" + (i++)));
            bos.write(bs, 0, len);
            bos.close();
        }
        bis.close();
    }
}
