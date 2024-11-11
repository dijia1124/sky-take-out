package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * Add new setmeal
     * @param setmealDTO
     */
    void saveWithDishes(SetmealDTO setmealDTO);

    /**
     * Setmeal page query
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * Get setmeal by id with dish
     * @param id
     * @return
     */
    SetmealVO getByIdWithDish(Long id);

    /**
     * Update setmeal
     * @param setmealDTO
     */
    void updateWithDish(SetmealDTO setmealDTO);

    /**
     * start or stop setmeal
     * @param id
     * @param status
     */
    void startOrStop(Integer status, Long id);

    /**
     * Delete setmeal
     * @param ids
     */
    void deleteBatch(List<Long> ids);
}
