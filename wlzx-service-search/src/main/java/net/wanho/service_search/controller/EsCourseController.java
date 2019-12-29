package net.wanho.service_search.controller;

import net.wanho.api.search.EsCourseControllerApi;
import net.wanho.common.vo.response.AjaxResult;
import net.wanho.common.web.BaseController;
import net.wanho.service_search.service.EsCourseService;
import net.wano.po.course.CoursePubDocument;
import net.wano.po.course.TeachplanMediaPub;
import net.wano.po.search.CourseSearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/search/course")
public class EsCourseController extends BaseController implements EsCourseControllerApi {
    @Autowired
    EsCourseService esCourseService;

    @Override
    @GetMapping(value="/list/{page}/{size}")
    public AjaxResult list(@PathVariable("page") int page, @PathVariable("size") int size, CourseSearchParam courseSearchParam) {
        return success(esCourseService.list(page,size,courseSearchParam));
    }

    @Override
    @GetMapping("/getall/{id}")
    public Map<String, CoursePubDocument> getall(@PathVariable("id") String id) {
        return null;
    }

    @Override
    @GetMapping(value="/getmedia/{teachplanId}")
    public TeachplanMediaPub getmedia(@PathVariable("teachplanId") String teachplanId) {
        return null;
    }
}