package net.wanho.manage_cms.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.wanho.api.cms.CmsTemplateControllerApi;
import net.wanho.common.vo.response.AjaxResult;
import net.wanho.common.web.BaseController;
import net.wanho.manage_cms.service.CmsTemplateService;
import net.wano.po.cms.CmsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms/template")
public class CmsTemplateController extends BaseController implements CmsTemplateControllerApi {
    @Autowired
    private CmsTemplateService cmsTemplateService;

    @GetMapping("/{siteId}")
    public AjaxResult findBySiteId(@PathVariable String siteId){
        QueryWrapper<CmsTemplate> wrapper = new QueryWrapper<>();
        wrapper.eq("site_id",siteId);
        return success(cmsTemplateService.list(wrapper));
    }
}
