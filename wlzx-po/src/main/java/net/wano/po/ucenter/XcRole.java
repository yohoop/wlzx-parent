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
@TableName("wlzx-role")

public class XcRole {

   @TableId
    private String id;
    @Column(name="role_name")
    private String roleName;
    @Column(name="roleCode")
    private String role_code;
    private String description;
    private String status;
    @Column(name="createTime")
    private Date create_time;
    @Column(name="update_time")
    private Date updateTime;


}
