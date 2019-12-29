package net.wanho.manage_media;

import org.junit.Test;

import java.io.*;

public class TestFile {
    @Test
    public void testMerge() throws IOException {
        //合 读多写1
        File file = new File("E:\\wlzx\\video\\chunks");
        int count = file.list().length;
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("E:\\wlzx\\video\\new.mp4"));
        int len = 0;
        byte[] bs = new byte[1024 * 1024];
        for (int i = 1; i <= count; i++) {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream("E:\\wlzx\\video\\chunks\\" + i), 1024 * 1024);
            len = bis.read(bs, 0, bs.length);
            bos.write(bs, 0, len);
            bis.close();
        }
        bos.close();
    }
}

