package net.wanho.manage_media.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wano.po.media.MediaFileProcessM3u8;

import java.util.List;

public interface MediaFileProcessM3u8Mapper extends BaseMapper<MediaFileProcessM3u8> {

    void batchInsert(List<MediaFileProcessM3u8> mediaFileProcessM3u8s);
}
