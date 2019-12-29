package net.wanho.manage_course.controller;

import net.wanho.api.course.CategoryControllerApi;
import net.wanho.common.web.BaseController;
import net.wanho.manage_course.service.CategoryService;
import net.wano.po.course.ext.CategoryNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController extends BaseController implements CategoryControllerApi {

     @Autowired
     private CategoryService categoryService;

    @Override
    @GetMapping("/list")
    public CategoryNode findList() {
        return categoryService.selectTree();
    }
}
