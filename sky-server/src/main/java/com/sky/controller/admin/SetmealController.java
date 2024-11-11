package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.service.SetmealService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "Setmeal Interface")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * Add setmeal
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation("Add setmeal")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        log.info("Add setmeal：{}", setmealDTO);

        setmealService.saveWithDishes(setmealDTO);
        return Result.success();
    }

//    /**
//     * Setmeal page query
//     * @param dishQueryDTO
//     * @return
//     */
//    @GetMapping("/page")
//    @ApiOperation("Dish page query")
//    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
//        log.info("Dish page query：{}", dishPageQueryDTO);
//
//        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
//        return Result.success(pageResult);
//    }
//
//    /**
//     * Delete dish
//     * @param ids
//     * @return
//     */
//    @DeleteMapping
//    @ApiOperation("Delete dish")
//    public Result delete(@RequestParam List<Long> ids){
//        log.info("Delete dish：{}", ids);
//
//        dishService.deleteBatch(ids);
//        return Result.success();
//    }
//
//    /**
//     * Get dish by id
//     * @param id
//     * @return
//     */
//    @GetMapping("/{id}")
//    @ApiOperation("Get dish by id")
//    public Result<DishVO> get(@PathVariable Long id){
//        log.info("Get dish by id：{}", id);
//        DishVO dishVO = dishService.getByIdWithFlavor(id);
//        return Result.success(dishVO);
//    }
//
//    /**
//     * Update dish
//     * @param dishDTO
//     * @return
//     */
//    @PutMapping
//    @ApiOperation("Update dish")
//    public Result update(@RequestBody DishDTO dishDTO){
//        log.info("Update dish：{}", dishDTO);
//
//        dishService.updateWithFlavor(dishDTO);
//        return Result.success();
//    }
}