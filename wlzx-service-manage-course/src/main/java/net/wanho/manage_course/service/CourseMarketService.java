package net.wanho.manage_course.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanho.common.util.StringUtils;
import net.wanho.manage_course.mapper.CourseMarketMapper;
import net.wano.po.course.CourseMarket;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CourseMarketService extends ServiceImpl<CourseMarketMapper, CourseMarket> {
    @Resource
    private CourseMarketMapper courseMarketMapper;
    public void updateCourseMarket(String id, CourseMarket courseMarket) {
        CourseMarket one = courseMarketMapper.selectById(id);
        if(StringUtils.isNull(one)){
            courseMarket.setId(id);
            courseMarketMapper.insert(courseMarket);
        }else{
            courseMarket.setId(id);
            courseMarketMapper.updateById(courseMarket);
        }

    }
}
