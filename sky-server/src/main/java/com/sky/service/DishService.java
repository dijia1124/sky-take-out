package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.DishVO;

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

    /**
     * Get dish by id with flavor
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * Update dish
     * @param dishDTO
     * @return
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * List dishes by category id
     * @param categoryId
     * @return
     */
    List<DishVO> listByCategoryId(Long categoryId);

    /**
     * Start of stop dish
     * @param status
     * @param id
     * @return
     */
    Result startOrStop(Integer status, Long id);
}
