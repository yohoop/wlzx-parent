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
@TableName("wlzx-user_role")

public class XcUserRole {

   @TableId
    private String id;

    @Column(name="user_id")
    private String userId;
    @Column(name="role_id")
    private String roleId;
    private String creator;
    @Column(name="create_time")
    private Date createTime;

}
