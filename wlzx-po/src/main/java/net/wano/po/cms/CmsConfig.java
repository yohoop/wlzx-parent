package net.wano.po.cms;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.List;


@Data
@ToString
@TableName(value = "cms_config")
public class CmsConfig {

  @TableId
    private String id;
    private String name;
    private List<CmsConfigModel> model;

}
