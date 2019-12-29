package net.wano.po.ucenter;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import java.io.Serializable;


@Data
@ToString
@Entity
@TableName("wlzx-teacher")
@GenericGenerator(name = "jpa-assigned", strategy = "assigned")
public class XcTeacher implements Serializable {
    private static final long serialVersionUID = -916357110051689786L;
    @TableId
    private String id;
    private String name;
    private String pic;
    private String intro;
    private String resume;
    private String userId;

}
