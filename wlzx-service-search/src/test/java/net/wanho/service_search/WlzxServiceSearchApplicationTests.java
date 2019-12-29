package net.wanho.service_search;

import net.wanho.common.vo.response.PageInfo;
import net.wanho.service_search.service.EsCourseService;
import net.wano.po.course.CoursePubDocument;
import net.wano.po.search.CourseSearchParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WlzxServiceSearchApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private EsCourseService esCourseService;

    @Test
    public void testFindByNameLike(){
        CourseSearchParam courseSearchParam = new CourseSearchParam();
        courseSearchParam.setKeyword("mysql mybatis");
        courseSearchParam.setMt("1-3");
        PageInfo<CoursePubDocument> list = esCourseService.list(1, 5, courseSearchParam);
        System.out.println(list.getList().size());
    }
}
