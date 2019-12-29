package net.wano.po.ucenter;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by admin on 2018/3/19.
 */
@Data
@ToString
@Entity
@TableName("wlzx-menu")

public class XcMenu {

   @TableId
    private String id;
    private String code;
    @Column(name="p_code")
    private String pCode;
    @Column(name="p_id")
    private String pId;
    @Column(name="menu_name")
    private String menuName;
    private String url;
    @Column(name="is_menu")
    private String isMenu;
    private Integer level;
    private Integer sort;
    private String status;
    private String icon;
    @Column(name="create_time")
    private Date createTime;
    @Column(name="update_time")
    private Date updateTime;


}
