package net.wano.po.ucenter.ext;


import lombok.Data;
import lombok.ToString;
import net.wano.po.ucenter.XcMenu;
import net.wano.po.ucenter.XcUser;

import java.util.List;

/**
 * Created by admin on 2018/3/20.
 */
@Data
@ToString
public class XcUserExt extends XcUser {

    //权限信息
    private List<XcMenu> permissions;

    //企业信息
    private String companyId;
}
