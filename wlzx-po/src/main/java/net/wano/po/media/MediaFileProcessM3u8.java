package net.wano.po.media;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author: mrt.
 * @Description:
 * @Date:Created in 2018/1/24 10:04.
 * @Modified By:
 */
@Data
@TableName("media_file_process_m3u8")
public class MediaFileProcessM3u8 {
    @TableId(type = IdType.UUID)
    //文件id
    private String id;
    //ts列表
    private String tslist;
    private String mediaFileId;

}
