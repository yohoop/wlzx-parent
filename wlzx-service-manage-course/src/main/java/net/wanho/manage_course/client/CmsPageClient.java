package net.wanho.manage_course.client;

import net.wanho.common.client.WlzxServiceList;
import net.wanho.common.vo.response.AjaxResult;
import net.wano.po.cms.CmsPage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = WlzxServiceList.WLZX_SERVICE_MANAGE_CMS)
public interface CmsPageClient {
    //保存页面
    @PostMapping("/cms/page/add")
    AjaxResult save(@RequestBody CmsPage cmsPage);

    @PostMapping("/cms/page/postPageQuick")
    AjaxResult postPageQuick(CmsPage cmsPage);
}
