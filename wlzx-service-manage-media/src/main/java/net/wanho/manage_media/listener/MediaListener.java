package net.wanho.manage_media.listener;

import net.wanho.common.util.HlsVideoUtil;
import net.wanho.common.util.Mp4VideoUtil;
import net.wanho.common.util.StringUtils;
import net.wanho.manage_media.mapper.MediaFileMapper;
import net.wanho.manage_media.mapper.MediaFileProcessM3u8Mapper;
import net.wano.po.media.MediaFile;
import net.wano.po.media.MediaFileProcessM3u8;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class MediaListener {
    @Resource
    private MediaFileMapper mediaFileMapper;

    @Resource
    private MediaFileProcessM3u8Mapper mediaFileProcessM3u8Mapper;

    @Value("${wlzx.ffmpeg-path}")
    private String ffmpeg_path;

    @Value("${wlzx.upload-location}")
    private String upload_location;

    //接收视频处理消息进行视频处理
    @RabbitListener(queues = "${wlzx.mq.queue}",containerFactory = "customContainerFactory")
    public void handleMediaProcess(String mediaId){

        //1.拿mediaId从数据库查询文件信息
        MediaFile mediaFile = mediaFileMapper.selectById(mediaId);
        if(StringUtils.isEmpty(mediaId)){
            return ;
        }
        //2.文件类型
        String fileType = mediaFile.getFileType();
        if(!fileType.equalsIgnoreCase("avi") && !fileType.equalsIgnoreCase("wmv")){
            mediaFile.setProcessStatus("303004");//无需处理
            mediaFileMapper.updateById(mediaFile);
            return ;
        }

        //需要处理
        mediaFile.setProcessStatus("303001");//处理中
        mediaFileMapper.updateById(mediaFile);

        //3. 要处理的视频文件
        String video_path= upload_location + mediaFile.getFilePath()+mediaFile.getFileName();
        //生成的mp4的文件名称
        String mp4_name=mediaFile.getFileId()+".mp4";
        //生成的mp4所在的路径,与之前原视频路径一致
        String mp4folder_path=upload_location+mediaFile.getFilePath();
        //创建工具类对象
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpeg_path, video_path, mp4_name, mp4folder_path);
        //进行处理
        String mp4Res = mp4VideoUtil.generateMp4();
        if(StringUtils.isEmpty(mp4Res) || !mp4Res.equalsIgnoreCase("success")){
            //处理失败
            mediaFile.setProcessStatus("303003");
            mediaFileMapper.updateById(mediaFile);
            return ;
        }


        //4. 将mp4生成m3u8和ts文件
        String mp4_video=mp4folder_path+mp4_name;
        String m3u8_name=mediaFile.getFileId()+".m3u8";
        String m3u8folder_path=upload_location+mediaFile.getFilePath()+"hls/";
        HlsVideoUtil hlsVideoUtil = new HlsVideoUtil(ffmpeg_path,mp4_video,m3u8_name,m3u8folder_path);
        String m3u8Res = hlsVideoUtil.generateM3u8();
        if(StringUtils.isEmpty(m3u8Res) || !m3u8Res.equalsIgnoreCase("success")){
            //处理失败
            mediaFile.setProcessStatus("303003");
            mediaFileMapper.updateById(mediaFile);
            return ;
        }

        //5.处理成功,插入media_file_process_m3u8记录
        //处理成功
        mediaFile.setProcessStatus("303002");
        mediaFile.setFileUrl(mediaFile.getFilePath()+"hls/"+m3u8_name);
        mediaFileMapper.updateById(mediaFile);


        List<MediaFileProcessM3u8> mediaFileProcessM3u8s = new ArrayList<>();
        for(String item:hlsVideoUtil.get_ts_list()){
            MediaFileProcessM3u8 mediaFileProcessM3u8 = new MediaFileProcessM3u8();
            mediaFileProcessM3u8.setTslist(item);
            mediaFileProcessM3u8.setMediaFileId(mediaFile.getFileId());
            mediaFileProcessM3u8s.add(mediaFileProcessM3u8);
        }

        mediaFileProcessM3u8Mapper.batchInsert(mediaFileProcessM3u8s);
    }
}
