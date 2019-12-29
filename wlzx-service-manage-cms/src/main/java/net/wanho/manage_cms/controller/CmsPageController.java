package net.wanho.manage_cms.controller;

import net.wanho.api.cms.CmsPageControllerApi;
import net.wanho.common.vo.response.AjaxResult;
import net.wanho.common.vo.response.PageInfo;
import net.wanho.common.web.BaseController;
import net.wanho.manage_cms.service.CmsPageService;
import net.wano.po.cms.CmsPage;
import net.wano.po.cms.request.QueryPageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cms/page")
public class CmsPageController extends BaseController implements CmsPageControllerApi {

    @Autowired
    CmsPageService cmsPageService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public AjaxResult findList(@PathVariable int page,@PathVariable int size, QueryPageRequest queryPageRequest) {
        return success(cmsPageService.findList(page, size, queryPageRequest));
    }

    @Override
    @PostMapping("/add")
    public AjaxResult add(@RequestBody CmsPage cmsPage) {
        cmsPage = cmsPageService.add(cmsPage);
        return success("",cmsPage.getPageId());
    }

    @Override
    @GetMapping("/get/{id}")
    public AjaxResult findById(@PathVariable String id) {
        return success(cmsPageService.findById(id));
    }

    @Override
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody CmsPage cmsPage) {
        cmsPageService.editById(cmsPage);
        return success();
    }

    @Override
    @DeleteMapping("/del/{id}")
    public AjaxResult delete(@PathVariable String id) {
        cmsPageService.deleteById(id);
        return success();
    }

    @Override
    @PostMapping("/postPage/{pageId}")
    public AjaxResult post(@PathVariable String pageId) {
        cmsPageService.post(pageId);
        return success();
    }

    @Override
    public AjaxResult save(CmsPage cmsPage) {
        return null;
    }

    @Override
    @PostMapping("/postPageQuick")
    public AjaxResult postPageQuick(CmsPage cmsPage) {
        String pageUrl = cmsPageService.postPageQuick(cmsPage);
        return  success("",pageUrl);
    }

}
