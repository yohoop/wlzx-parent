package net.wanho.manage_cms.listener;

import lombok.extern.slf4j.Slf4j;
import net.wanho.common.exception.ExceptionCast;
import net.wanho.common.util.StringUtils;
import net.wanho.manage_cms.service.CmsPageService;
import net.wano.po.cms.CmsPage;
import net.wano.po.cms.response.CmsCode;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PostPageListener {

    @Autowired
    private CmsPageService cmsPageService;

    @RabbitListener(queues = "${wlzx.mq.queue}")
    public void receive(String pageId){
        CmsPage cmsPage = cmsPageService.findById(pageId);
        if(!StringUtils.isNotEmpty(cmsPage)){
            log.error("receive postpage msg,cmsPage is null,pageId:{}", pageId);
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        cmsPageService.savePageToServerPath(pageId);
    }
}
