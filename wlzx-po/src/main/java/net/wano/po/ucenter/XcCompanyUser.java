package net.wano.po.ucenter;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;


@Data
@ToString
@Entity
@TableName("wlzx-company_user")

public class XcCompanyUser implements Serializable {
    private static final long serialVersionUID = -916357110051689786L;
   @TableId
    private String id;
    @Column(name="company_id")
    private String companyId;
    @Column(name="user_id")
    private String userId;


}
