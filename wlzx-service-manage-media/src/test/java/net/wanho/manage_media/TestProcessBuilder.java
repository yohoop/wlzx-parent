package net.wanho.manage_media;

import net.wanho.common.util.Mp4VideoUtil;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TestProcessBuilder {
    //使用ProcessBuilder来调用系统命令
    @Test
    public void testProcessBuilder() throws IOException {
        //创建processBuilder对象
        ProcessBuilder processBuilder = new ProcessBuilder();

        //设置第三方应用程序的命令
//        processBuilder.command("ping","wanho.net");
        processBuilder.command("ipconfig");

        //将标准输入流和错误流合并
        processBuilder.redirectErrorStream(true);
        //启动一个进程
        Process process = processBuilder.start();
        //通过标准输入流来拿到正常和错误的信息
        InputStream is = process.getInputStream();
        //转成缓存流
        InputStreamReader isr = new InputStreamReader(is,"gbk");
        BufferedReader br =  new BufferedReader(isr);
        String line=null;
        while((line=br.readLine())!=null){
            System.out.println(line);
        }
        br.close();
        isr.close();
        is.close();
    }
    @Test
    public void testFFmpeg() throws IOException {
        //创建processBuilder对象
        ProcessBuilder processBuilder = new ProcessBuilder();

        List<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-i");
        command.add("E:\\wlzx\\video\\mycat.wmv");
        command.add("-y");//覆盖输出文件
        command.add("-c:v");
        command.add("libx264");
        command.add("-s");
        command.add("1280x720");
        command.add("-pix_fmt");
        command.add("yuv420p");
        command.add("-b:a");
        command.add("63k");
        command.add("-b:v");
        command.add("753k");
        command.add("-r");
        command.add("18");
        command.add("E:\\wlzx\\video\\mycat2.mp4");
        processBuilder.command(command);

        //将标准输入流和错误流合并
        processBuilder.redirectErrorStream(true);
        //启动一个进程
        Process process = processBuilder.start();
        //通过标准输入流来拿到正常和错误的信息
        InputStream is = process.getInputStream();
        //转成缓存流
        InputStreamReader isr = new InputStreamReader(is,"gbk");
        BufferedReader br =  new BufferedReader(isr);
        String line=null;
        while((line=br.readLine())!=null){
            System.out.println(line);
        }
        br.close();
        isr.close();
        is.close();
    }
    @Test
    public void testMp4VideoUtil(){
        String ffmpeg_path ="ffmpeg";
        String video_path="E:\\wlzx\\video\\mycat.wmv";
        String mp4_name="mycat3.mp4";
        String mp4folder_path="E:\\wlzx\\video\\";
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpeg_path,video_path,mp4_name,mp4folder_path);
        mp4VideoUtil.generateMp4();
    }
}
