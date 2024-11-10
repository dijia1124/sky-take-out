package com.sky.service;

import com.sky.dto.DishDTO;

public interface DishService {
    /**
     * Add new dish
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);
}
