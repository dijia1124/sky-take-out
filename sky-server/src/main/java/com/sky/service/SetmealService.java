package com.sky.service;

import com.sky.dto.SetmealDTO;

public interface SetmealService {

    /**
     * Add new setmeal
     * @param setmealDTO
     */
    void saveWithDishes(SetmealDTO setmealDTO);
}
