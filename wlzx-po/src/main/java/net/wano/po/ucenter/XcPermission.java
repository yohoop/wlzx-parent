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
@TableName("wlzx-permission")

public class XcPermission {

   @TableId
    private String id;
    @Column(name="roleId")
    private String role_id;
    @Column(name="menuId")
    private String menu_id;
    @Column(name="createTime")
    private Date create_time;


}
