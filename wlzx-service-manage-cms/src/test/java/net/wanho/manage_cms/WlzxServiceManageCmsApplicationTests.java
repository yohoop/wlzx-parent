package net.wanho.manage_cms;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.wanho.common.util.StringUtils;
import net.wanho.manage_cms.mapper.CmsPageMapper;
import net.wanho.manage_cms.service.CmsPageService;
import net.wano.po.cms.CmsPage;
import net.wano.po.cms.CmsPageParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class WlzxServiceManageCmsApplicationTests {

    @Autowired
    private CmsPageService cmsPageService;

    @Resource
    private CmsPageMapper cmsPageMapper;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testInsert() {
        //定义实体类
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId("s01");
        cmsPage.setTemplateId("t01");
        cmsPage.setPageName("测试页面");
        cmsPage.setPageCreateTime(new Date());
        List<CmsPageParam> cmsPageParams = new ArrayList<>();
        CmsPageParam cmsPageParam = new CmsPageParam();
        cmsPageParam.setPageParamName("param1");
        cmsPageParam.setPageParamValue("value1");
        cmsPageParams.add(cmsPageParam);
        cmsPage.setPageParams(cmsPageParams);
        cmsPageService.save(cmsPage);
        System.out.println(cmsPage);
    }

    //修改
    @Test
    public void testUpdate() {
        CmsPage cmsPage =cmsPageService.getById("38c382bd5ba352b21412c5241611c7bf");
        if(StringUtils.isNotEmpty(cmsPage)){
            cmsPage.setPageName("测试页面01111");
            cmsPageService.updateById(cmsPage);
        }
    }

    //删除
    @Test
    public void testDelete() {
        cmsPageService.removeById("38c382bd5ba352b21412c5241611c7bf");
    }

    // 查询所有
    @Test
    public void testFindAll() {
        List<CmsPage> all = cmsPageService.list();
        System.out.println(all);

    }

    // 分页查询
    @Test
    public void testFindPage() {
        //分页参数
        IPage<CmsPage> page = new Page<>(1,3);
        IPage<CmsPage> pages = cmsPageService.page(page);
        System.out.println(pages.getRecords());
    }

    @Test
    public void testFindPage2() {
        //分页参数
        IPage<CmsPage> page = new Page<>(1,3);
        IPage<CmsPage> pages = cmsPageMapper.selectPage(page,new QueryWrapper<>());
        System.out.println(pages.getRecords());
    }

    // 自定义条件查询测试
    @Test
    public void testFindAllByExample() {
        //分页参数
        IPage<CmsPage> page = new Page<>(1,3);
        QueryWrapper<CmsPage> wrapper = new QueryWrapper<>();
        wrapper.eq("page_aliase","轮播");
        IPage<CmsPage> pages = cmsPageService.page(page,wrapper);
        List<CmsPage> content = pages.getRecords();
        System.out.println(content);
    }

}
