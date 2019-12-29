package net.wano.po.ucenter;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import java.io.Serializable;


@Data
@ToString
@Entity
@TableName("wlzx-company")

public class XcCompany implements Serializable {
    private static final long serialVersionUID = -916357110051689786L;
   @TableId
    private String id;
    private String name;
    private String logo;
    private String intro;
    private String email;
    private String mobile;
    private String linkname;
    private String identitypic;
    private String worktype;
    private String businesspic;
    private String status;


}
