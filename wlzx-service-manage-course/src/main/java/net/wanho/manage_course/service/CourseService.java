package net.wanho.manage_course.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.wanho.common.exception.ExceptionCast;
import net.wanho.common.util.StringUtils;
import net.wanho.common.vo.response.AjaxResult;
import net.wanho.common.vo.response.CommonCode;
import net.wanho.common.vo.response.PageInfo;
import net.wanho.manage_course.client.CmsPageClient;
import net.wanho.manage_course.mapper.*;
import net.wano.po.cms.CmsPage;
import net.wano.po.course.*;
import net.wano.po.course.ext.CourseView;
import net.wano.po.course.ext.TeachplanNode;
import net.wano.po.course.request.CourseListRequest;
import net.wano.po.course.response.CourseCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CourseService extends ServiceImpl<CourseMapper, CourseBase> {

    @Resource
    private TeachplanMapper teachplanMapper;
    @Resource
    private CourseMapper courseMapper;
    @Resource
    private CoursePicMapper coursePicMapper;
    @Resource
    private CourseMarketMapper courseMarketMapper;
    @Resource
    private CmsPageClient cmsPageClient;
    @Resource
    private CoursePubMapper coursePubMapper;

    @Value("${course-publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course-publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;

    public TeachplanNode findTeachplanList(String courseId) {
        return teachplanMapper.selectTree(courseId);
    }

    public void addTeachplan(Teachplan teachplan) {
        if(StringUtils.isNull(teachplan)||StringUtils.isEmpty(teachplan.getCourseid())||StringUtils.isEmpty(teachplan.getPname())){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        String courseid = teachplan.getCourseid();
        String parentid = teachplan.getParentid();
        if(StringUtils.isEmpty(parentid)){
            parentid=getTeachplanRoot(courseid);
        }
        Teachplan parentTeachplan = teachplanMapper.selectById(parentid);
        String parentTeachplanGrade = parentTeachplan.getGrade();
        teachplan.setParentid(parentid);
        if(parentTeachplanGrade.equals("1")){
            teachplan.setGrade("2");
        }else{
            teachplan.setGrade("3");
        }
        teachplanMapper.insert(teachplan);
    }

    private String getTeachplanRoot(String courseid) {
        QueryWrapper<Teachplan> wrapper = new QueryWrapper<>();
        wrapper.eq("courseid",courseid);
        wrapper.eq("parentid","0");
        Teachplan rootTeachplan = teachplanMapper.selectOne(wrapper);
        CourseBase courseBase = courseMapper.selectById(courseid);
        if(StringUtils.isNull(rootTeachplan)){
            Teachplan teachplan = new Teachplan();
            teachplan.setCourseid(courseid);
            teachplan.setPname(courseBase.getName());
            teachplan.setParentid("0");
            teachplan.setGrade("1");
            teachplan.setStatus("0");
            teachplanMapper.insert(teachplan);
            return teachplan.getId();
        }
        return rootTeachplan.getId();
    }

    public PageInfo findCourseList(int pageNo, int pageSize, CourseListRequest courseListRequest) {
        if(StringUtils.isNull(courseListRequest)){
            courseListRequest = new CourseListRequest();
        }
        //todo   暂时写死，后面从登录时获取用户的companyId
        //企业id
        courseListRequest.setCompanyId("2");


        IPage<CourseBase> page = new Page<>(pageNo,pageSize);
        QueryWrapper<CourseBase> wrapper = new QueryWrapper<>();
        wrapper.eq("company_id",courseListRequest.getCompanyId());
        page = courseMapper.selectPage(page, wrapper);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setTotal(page.getTotal());
        pageInfo.setList(page.getRecords());
        return pageInfo;
    }

    public void addCourseBase(CourseBase courseBase) {
        //课程状态默认为未发布
        courseBase.setStatus("202001");
        //todo componyId以后从登录用户中获取
        courseBase.setCompanyId("2");
        courseMapper.insert(courseBase);
    }

    public void updateCourseBase(String id, CourseBase courseBase) {
        CourseBase one = this.getById(id);
        if(StringUtils.isNull(one)){
            ExceptionCast.cast(CourseCode.COURSE_GET_NOTEXISTS);
        }
        //修改课程信息
        courseBase.setId(id);
        this.updateById(courseBase);
    }

    public void saveCoursePic(String courseId, String pic) {
        CourseBase one = this.getById(courseId);
        if(StringUtils.isNull(one)){
            ExceptionCast.cast(CourseCode.COURSE_GET_NOTEXISTS);
        }
        CoursePic onePic = coursePicMapper.selectById(courseId);
        if(StringUtils.isNotEmpty(onePic)){
            ExceptionCast.cast(CourseCode.COURSE_PIC_EXISTS);
        }
        else {
            //保存到course_pic表中
            CoursePic coursePic = new CoursePic();
            coursePic.setCourseid(courseId);
            coursePic.setPic(pic);
            coursePicMapper.insert(coursePic);
        }
    }

    public CourseView getCourseView(String id) {
        CourseView courseView = new CourseView();
        CourseBase courseBase= courseMapper.selectById(id);
        courseView.setCourseBase(courseBase);
        CourseMarket courseMarket= courseMarketMapper.selectById(id);
        courseView.setCourseMarket(courseMarket);
        CoursePic coursePic = coursePicMapper.selectById(id);
        courseView.setCoursePic(coursePic);
        TeachplanNode teachplanNode = teachplanMapper.selectTree(id);
        courseView.setTeachplanNode(teachplanNode);
        return courseView;
    }

    public String preview(String courseId) {
        CourseBase courseBase = this.getById(courseId);
        //发布课程预览页面
        CmsPage cmsPage = new CmsPage();
        //站点
        cmsPage.setSiteId(publish_siteId);//课程预览站点
        //模板
        cmsPage.setTemplateId(publish_templateId);
        //页面名称
        cmsPage.setPageName(courseId + ".html");
        //页面别名
        cmsPage.setPageAliase(courseBase.getName());
        //页面访问路径
        cmsPage.setPageWebPath(publish_page_webpath);
        //页面存储路径
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        //数据url
        cmsPage.setDataUrl(publish_dataUrlPre + courseId);
        //远程请求cms保存页面信息
        AjaxResult cmsPageResult = cmsPageClient.save(cmsPage);

        if(!cmsPageResult.isSuccess()){
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_VIEWERROR);
        }
        //页面id
        String pageId = cmsPageResult.getData().toString();
        //页面url
        String pageUrl = previewUrl + pageId;
        return pageUrl;
    }

    public String publish(String courseId) {
        CourseBase courseBase = this.getById(courseId);
        //发布课程预览页面
        CmsPage cmsPage = new CmsPage();
        //站点
        cmsPage.setSiteId(publish_siteId);//课程预览站点
        //模板
        cmsPage.setTemplateId(publish_templateId);
        //页面名称
        cmsPage.setPageName(courseId + ".html");
        //页面别名
        cmsPage.setPageAliase(courseBase.getName());
        //页面访问路径
        cmsPage.setPageWebPath(publish_page_webpath);
        //页面存储路径
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        //数据url
        cmsPage.setDataUrl(publish_dataUrlPre + courseId);
        //远程请求cms的postPageQuick方法发布课程信息
        AjaxResult ajaxResult = cmsPageClient.postPageQuick(cmsPage);
        if(!ajaxResult.isSuccess()){
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_CDETAILERROR);
        }

        //保存课程的发布状态为“已发布”
        this.changeCourseBaseState(courseId);

        //获取CoursePub信息
        CoursePub coursePub = createCoursePub(courseId);
        //保存信息
        this.saveCoursePub(courseId,coursePub);

        //得到页面的url
        String pageUrl = ajaxResult.getData().toString();

        return pageUrl;
    }
    /**
     * 创建CoursePub对象
     * @param courseId
     * @return
     */
    private CoursePub createCoursePub(String courseId) {
        CoursePub coursePub = new CoursePub();

        //根据课程id查询course_base
        CourseBase courseBase = courseMapper.selectById(courseId);
        //将courseBase属性拷贝到CoursePub中
        BeanUtils.copyProperties(courseBase,coursePub);

        //查询课程图片
        CoursePic coursePic =  coursePicMapper.selectById(courseId);
        BeanUtils.copyProperties(coursePic, coursePub);

        //课程营销信息
        CourseMarket courseMarket =  courseMarketMapper.selectById(courseId);
        BeanUtils.copyProperties(courseMarket, coursePub);

        //课程计划信息
        TeachplanNode teachplanNode = teachplanMapper.selectTree(courseId);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            coursePub.setTeachplan(objectMapper.writeValueAsString(teachplanNode));
            return coursePub;
        } catch (JsonProcessingException e) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_CDETAILERROR);
            return null;
        }



    }

    /**
     * 保存课程发布
     * @param courseId
     * @param coursePub
     */
    private void saveCoursePub(String courseId, CoursePub coursePub) {
        //根据课程id查询coursePub
        CoursePub one = coursePubMapper.selectById(courseId);
        if(one!=null){

            //把修改后值更新到one
            BeanUtils.copyProperties(coursePub,one);

            //重置主键
            one.setId(courseId);

            //时间戳,给logstach使用
            one.setTimestamp(new Date());

            //发布时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            one.setPubTime(sdf.format(new Date()));

            //修改
            coursePubMapper.updateById(one);
        }else{
            one = new CoursePub();

            //重置主键
            one.setId(courseId);

            //把新增值更新到one
            BeanUtils.copyProperties(coursePub,one);

            //时间戳,给logstach使用
            one.setTimestamp(new Date());

            //发布时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            one.setPubTime(sdf.format(new Date()));

            //新增
            coursePubMapper.insert(one);
        }
    }

    /**
     * 更新CourseBase状态
     * @param courseId
     */
    private void changeCourseBaseState(String courseId) {
        CourseBase courseBase = this.getById(courseId);
        //已发布
        courseBase.setStatus("202002");
        courseMapper.updateById(courseBase);
    }
}
