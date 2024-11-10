package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;

import java.util.List;

public interface DishService {
    /**
     * Add new dish
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * Dish page query
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * Delete dish
     * @param ids
     */
    void deleteBatch(List<Long> ids);
}
