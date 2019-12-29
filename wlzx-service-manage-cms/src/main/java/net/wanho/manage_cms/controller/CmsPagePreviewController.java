package net.wanho.manage_cms.controller;

import net.wanho.common.util.ServletUtils;
import net.wanho.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class CmsPagePreviewController {

    @Autowired
    private CmsPageService cmsPageService;

    @GetMapping("/cms/preview/{pageId}")
    public void preview(@PathVariable String pageId,HttpServletResponse response) throws IOException {
        response.setHeader("Content-type","text/html;charset=utf-8");

        String pageHtml = cmsPageService.getPageHtml(pageId);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(pageHtml.getBytes("utf-8"));
        /*HttpServletResponse response = ServletUtils.getResponse();
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(pageHtml);*/
    }
}
