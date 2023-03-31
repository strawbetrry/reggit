package com.whxy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whxy.reggie.bean.Category;
import com.whxy.reggie.common.R;
import com.whxy.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping
    public R<String> saveCai(@RequestBody Category category){
        categoryService.save(category);
        return R.success("新增菜品成功!");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        Page<Category> pageInfo = new Page<>(page,pageSize);
        //创建条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //创建分页条件
        lambdaQueryWrapper.orderByAsc(Category::getSort);

        categoryService.page(pageInfo,lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> deleteById(@RequestParam("ids") Long id){
        log.info("id是多少--->{}",id);
        categoryService.remove(id);
        return R.success("删除成功!");
    }
    @PutMapping
    public R<String> modify(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功");
    }
}
