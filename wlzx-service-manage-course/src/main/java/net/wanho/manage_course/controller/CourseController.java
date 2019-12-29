package net.wanho.manage_course.controller;

import net.wanho.api.course.CourseControllerApi;
import net.wanho.common.vo.response.AjaxResult;
import net.wanho.common.web.BaseController;
import net.wanho.manage_course.service.CourseMarketService;
import net.wanho.manage_course.service.CoursePicService;
import net.wanho.manage_course.service.CourseService;
import net.wanho.manage_course.service.FileSystemService;
import net.wano.po.course.*;
import net.wano.po.course.ext.CourseView;
import net.wano.po.course.ext.TeachplanNode;
import net.wano.po.course.request.CourseListRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/course")
public class CourseController extends BaseController implements CourseControllerApi {

    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseMarketService courseMarketService;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private CoursePicService coursePicService;

    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable String courseId) {
        return courseService.findTeachplanList(courseId);
    }

    @Override
    @PostMapping("/teachplan/add")
    public AjaxResult addTeachplan(@RequestBody Teachplan teachplan) {
        courseService.addTeachplan(teachplan);
        return success();
    }

    @Override
    @GetMapping("/courseBase/list/{page}/{size}")
    public AjaxResult findCourseList(@PathVariable int page,@PathVariable int size, CourseListRequest courseListRequest) {
        return success(courseService.findCourseList(page,size,courseListRequest));
    }

    @Override
    @PostMapping("/coursebase/add")
    public AjaxResult addCourseBase(@RequestBody CourseBase courseBase) {
        courseService.addCourseBase(courseBase);
        return success();
    }

    @Override
    @GetMapping("/courseBase/get/{courseId}")
    public CourseBase getCourseBaseById(@PathVariable String courseId) throws RuntimeException {
        return courseService.getById(courseId);
    }

    @Override
    @PutMapping("/courseBase/update/{id}")
    public AjaxResult updateCourseBase(@PathVariable String id, @RequestBody CourseBase courseBase) {
        courseService.updateCourseBase(id,courseBase);
        return success();
    }

    @Override
    @GetMapping("/courseMarket/get/{courseId}")
    public CourseMarket getCourseMarketById(@PathVariable String courseId) {
        return courseMarketService.getById(courseId);
    }

    @Override
    @PostMapping("/courseMarket/update/{id}")
    public AjaxResult updateCourseMarket(@PathVariable String id,@RequestBody CourseMarket courseMarket) {
        courseMarketService.updateCourseMarket(id,courseMarket);
        return success();
    }

    @Override
    @PostMapping("/coursePic/add")
    public AjaxResult addCoursePic(String courseId, String pic) {
        courseService.saveCoursePic(courseId,pic);
        return success();
    }

    @Override
    @GetMapping("/coursePic/list/{courseId}")
    public CoursePic findCoursePic(@PathVariable String courseId) {
        return  coursePicService.getById(courseId);
    }

    @Override
    @DeleteMapping("/coursePic/delete")
    public AjaxResult deleteCoursePic(String courseId) {
        coursePicService.removeById(courseId);
        return success();
    }

    @Override
    @GetMapping("/courseView/{id}")
    public CourseView courseView(@PathVariable String id) {
        return courseService.getCourseView(id);
    }

    @Override
    @PostMapping("/preview/{id}")
    public AjaxResult preview(@PathVariable String id) {
        return success("",courseService.preview(id));
    }

    @Override
    @PostMapping("/publish/{id}")
    public AjaxResult publish(@PathVariable String id) {
        return success("",courseService.publish(id));
    }

    @Override
    public AjaxResult saveMedia(TeachplanMedia teachplanMedia) {
        return null;
    }

    @Override
    @PostMapping("/upload")
    public AjaxResult upload(MultipartFile multipartFile, String fileTag, String businessKey, String metadata) {
//        fileSystemService.upload(multipartFile, fileTag, businessKey, metadata);
        return success(fileSystemService.upload(multipartFile, fileTag, businessKey, metadata));
    }
}
