package com.whxy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whxy.reggie.bean.Category;
import com.whxy.reggie.bean.Dish;
import com.whxy.reggie.bean.Setmeal;
import com.whxy.reggie.common.CustomerException;
import com.whxy.reggie.mapper.CategoryMapper;
import com.whxy.reggie.service.CategoryService;
import com.whxy.reggie.service.DishService;
import com.whxy.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;
    @Override
    public void remove(Long id) {
        //查看当前分类是否关联了菜品
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //查看菜品的数据库里是否有当前这个菜品分类id
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if(count1 > 0){
            throw new CustomerException("该菜单分类关联了菜品，不能删除!");
        }
        //查看当前分类是否关联了套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if(count2 > 0){
            throw new CustomerException("该菜单分类关联了套餐，不能删除!");
        }
        super.removeById(id);
    }
}
