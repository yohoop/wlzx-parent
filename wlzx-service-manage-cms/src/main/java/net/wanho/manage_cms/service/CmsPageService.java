package net.wanho.manage_cms.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import net.wanho.common.exception.ExceptionCast;
import net.wanho.common.util.StringUtils;
import net.wanho.common.vo.response.PageInfo;
import net.wanho.manage_cms.mapper.CmsPageMapper;
import net.wanho.manage_cms.mapper.CmsSiteMapper;
import net.wanho.manage_cms.mapper.CmsTemplateMapper;
import net.wano.po.cms.CmsPage;
import net.wano.po.cms.CmsSite;
import net.wano.po.cms.CmsTemplate;
import net.wano.po.cms.request.QueryPageRequest;
import net.wano.po.cms.response.CmsCode;
import org.apache.commons.io.IOUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.*;
import java.util.Date;
import java.util.Map;

@Service
public class CmsPageService extends ServiceImpl<CmsPageMapper, CmsPage> {

    @Resource
    private CmsPageMapper cmsPageMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Resource
    private CmsTemplateMapper cmsTemplateMapper;
    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Resource
    private CmsSiteMapper cmsSiteMapper;

    //交换器的名称
    @Value("${wlzx.mq.exchange}")
    private  String exchange_name;

    //队列的名称
    @Value("${wlzx.mq.queue}")
    private  String queue_name;

    //路由键 即站点Id
    @Value("${wlzx.mq.routingKey}")
    private  String routingKey;

    public PageInfo findList(int pageNo, int pageSize, QueryPageRequest queryPageRequest) {
        //分页参数
        if(pageNo <=0){
            pageNo = 1;
        }
        if(pageSize<=0){
            pageSize = 10;
        }
        IPage<CmsPage> page = new Page<>(pageNo,pageSize);
        //条件
        QueryWrapper<CmsPage> wrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            wrapper.eq("site_id",queryPageRequest.getSiteId());
        }
        // 模板Id
        //设置模板id作为查询条件
        if(StringUtils.isNotEmpty(queryPageRequest.getTemplateId())){
            wrapper.eq("template_id",queryPageRequest.getTemplateId());
        }
        // 页面别名查询页面信息
        //设置页面别名作为查询条件，模糊查询
        if(StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
            wrapper.like("page_aliase",queryPageRequest.getPageAliase());
        }
        page = cmsPageMapper.selectPage(page, wrapper);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(page.getRecords());
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    private boolean isExist(CmsPage cmsPage){
        QueryWrapper<CmsPage> wrapper = new QueryWrapper<>();
        wrapper.eq("page_name",cmsPage.getPageName());
        wrapper.eq("site_id",cmsPage.getSiteId());
        wrapper.eq("page_web_path",cmsPage.getPageWebPath());
        CmsPage selectOne = cmsPageMapper.selectOne(wrapper);
        if(StringUtils.isNotEmpty(cmsPage.getPageId())){
            if(StringUtils.isNotEmpty(selectOne)){
               if(cmsPage.getPageId().equals(selectOne.getPageId())){return false;}else{return true;}
            }else{return false;}
        }else{
            return StringUtils.isNotEmpty(selectOne);
        }
    }

    public CmsPage add(CmsPage cmsPage) {
        QueryWrapper<CmsPage> wrapper = new QueryWrapper<>();
        wrapper.eq("page_name",cmsPage.getPageName());
        wrapper.eq("site_id",cmsPage.getSiteId());
        wrapper.eq("page_web_path",cmsPage.getPageWebPath());

        CmsPage dbCmsPage = cmsPageMapper.selectOne(wrapper);
        if(dbCmsPage==null){
            //调用dao新增页面
            cmsPage.setPageId(null);
            cmsPage.setPageCreateTime(new Date());
            cmsPageMapper.insert(cmsPage);
            return cmsPage;
        }
        return dbCmsPage;
    }

    public CmsPage findById(String id) {
        CmsPage cmsPage = cmsPageMapper.selectById(id);
        if(!StringUtils.isNotEmpty(cmsPage)){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        return cmsPage;
    }


    public void editById(CmsPage cmsPage) {
         if(isExist(cmsPage)){
             ExceptionCast.cast(CmsCode.CMS_EDITPAGE_EXISTSNAME);
         }else{
             cmsPageMapper.updateById(cmsPage);
         }
    }

    public void deleteById(String id) {
        CmsPage cmsPage = cmsPageMapper.selectById(id);
        if(!StringUtils.isNotEmpty(cmsPage)){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        cmsPageMapper.deleteById(id);
    }

    public String getPageHtml(String pageId){
        Map model = getModelByPageId(pageId);
        if(!StringUtils.isNotEmpty(model)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        String templateContent = getTemplateByPageId(pageId);
        if(StringUtils.isEmpty(templateContent)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        String html = generateHtml(model,templateContent);
        return html;
    }

    private String generateHtml(Map model, String templateContent) {
        //创建配置对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //创建模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",templateContent);
        //向configuration配置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        try {
            Template template = configuration.getTemplate("template");
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getTemplateByPageId(String pageId) {
        CmsPage cmsPage = findById(pageId);
        if(!StringUtils.isNotEmpty(cmsPage)){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String templateId = cmsPage.getTemplateId();
        if(StringUtils.isEmpty(templateId)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        CmsTemplate cmsTemplate = cmsTemplateMapper.selectById(templateId);
        if(!StringUtils.isNotEmpty(cmsTemplate)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        String templateContent = cmsTemplate.getTemplateContent();
        if(StringUtils.isEmpty(templateContent)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        String group = templateContent.substring(0,templateContent.indexOf("/"));
        String path = templateContent.substring(templateContent.indexOf("/")+1);
        byte[] bytes = fastFileStorageClient.downloadFile(group, path, new DownloadByteArray());
        return new String(bytes);
    }

    private Map getModelByPageId(String pageId) {
        CmsPage cmsPage = findById(pageId);
        if(!StringUtils.isNotEmpty(cmsPage)){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String dataUrl = cmsPage.getDataUrl();
        if(StringUtils.isEmpty(dataUrl)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        ResponseEntity<Map> entity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = entity.getBody();
        return body;
    }

    public void post(String pageId) {
        try {
            saveHtml(pageId);
            sendPostPage(pageId);
        } catch (Exception e) {
            ExceptionCast.cast(CmsCode.CMS_POST_PAGE_FAIL);
        }
    }

    private void sendPostPage(String pageId) {
        rabbitTemplate.convertAndSend(exchange_name,routingKey,pageId);
    }

    private void saveHtml(String pageId) throws Exception {
        String pageHtml = getPageHtml(pageId);
        CmsPage cmsPage = cmsPageMapper.selectById(pageId);
        if(!StringUtils.isNotEmpty(cmsPage)){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        InputStream inputStream = new ByteArrayInputStream(pageHtml.getBytes("utf-8"));
        StorePath storePath = fastFileStorageClient.uploadFile(inputStream, inputStream.available(), "html", null);
        cmsPage.setHtmlFilePath(storePath.getGroup()+"/"+storePath.getPath());
        cmsPageMapper.updateById(cmsPage);
    }

    public void savePageToServerPath(String pageId) {
        CmsPage cmsPage = cmsPageMapper.selectById(pageId);
        if(!StringUtils.isNotEmpty(cmsPage)){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String siteId = cmsPage.getSiteId();
        CmsSite cmsSite = cmsSiteMapper.selectById(siteId);
        if(!StringUtils.isNotEmpty(cmsPage)){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String pagePath = cmsSite.getSitePhysicalPath() + cmsPage.getPagePhysicalPath();

        File file = new File(pagePath);
        //目录不存在时创建
        if(!file.exists()){
            file.mkdirs();
        }
        try {
            String htmlFilePath = cmsPage.getHtmlFilePath();
            String group = htmlFilePath.substring(0,htmlFilePath.indexOf("/"));
            String path = htmlFilePath.substring(htmlFilePath.indexOf("/")+1);
            byte[] bytes = fastFileStorageClient.downloadFile(group, path, new DownloadByteArray());
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

            FileOutputStream fileOutputStream = new FileOutputStream(pagePath+cmsPage.getPageName());

            IOUtils.copy(byteArrayInputStream,fileOutputStream);
        } catch (Exception e) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_SAVEHTMLERROR);
        }

    }

    public String postPageQuick(CmsPage cmsPage) {
        //得到页面信息
        CmsPage one = this.add(cmsPage);
        //获取页面id
        String pageId = one.getPageId();
        //调用cmsPage的发布方法
        this.post(pageId);

        //拼接页面Url= cmsSite.siteDomain+cmsSite.siteWebPath+ cmsPage.pageWebPath + cmsPage.pageName
        String siteId = one.getSiteId();
        CmsSite cmsSite = cmsSiteMapper.selectById(siteId);
        String pageUrl =cmsSite.getSiteDomain()+cmsSite.getSiteWebPath()+ one.getPageWebPath() + one.getPageName();
        return pageUrl;
    }
}
