package net.wanho.manage_course.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanho.manage_course.mapper.CategoryMapper;
import net.wano.po.course.Category;
import net.wano.po.course.ext.CategoryNode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {
    @Resource
    private CategoryMapper categoryMapper;

    public CategoryNode selectTree() {
        return categoryMapper.selectTree();
    }
}
