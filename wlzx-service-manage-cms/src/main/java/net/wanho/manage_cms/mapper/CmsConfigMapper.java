package net.wanho.manage_cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wano.po.cms.CmsConfig;

public interface CmsConfigMapper extends BaseMapper<CmsConfig> {
    CmsConfig getConfigAndModelById(String id);

}
