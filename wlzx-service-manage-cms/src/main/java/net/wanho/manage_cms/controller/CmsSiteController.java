package net.wanho.manage_cms.controller;

import net.wanho.api.cms.CmsPageControllerApi;
import net.wanho.api.cms.CmsSiteControllerApi;
import net.wanho.common.vo.response.AjaxResult;
import net.wanho.common.web.BaseController;
import net.wanho.manage_cms.service.CmsSiteService;
import net.wano.po.cms.CmsPage;
import net.wano.po.cms.request.QueryPageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms/site")
public class CmsSiteController extends BaseController implements CmsSiteControllerApi {

    @Autowired
    private CmsSiteService cmsSiteService;


    @Override
    @GetMapping()
    public AjaxResult findAllCmsSite() {
        return success(cmsSiteService.list());
    }
}
