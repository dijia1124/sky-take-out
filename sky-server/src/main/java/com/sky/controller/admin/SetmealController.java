package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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
    @CacheEvict(cacheNames = "setmealCache", key = "#setmealDTO.categoryId")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        log.info("Add setmeal：{}", setmealDTO);

        setmealService.saveWithDishes(setmealDTO);
        return Result.success();
    }

    /**
     * Setmeal page query
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("Setmeal page query")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("Setmeal page query：{}", setmealPageQueryDTO);

        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Delete setmeal
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("Delete setmeal")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result delete(@RequestParam List<Long> ids){
        log.info("Delete setmeal：{}", ids);

        setmealService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * Get setmeal by id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("Get setmeal by id")
    public Result<SetmealVO> get(@PathVariable Long id){
        log.info("Get setmeal by id：{}", id);
        SetmealVO setmealVO = setmealService.getByIdWithDish(id);
        return Result.success(setmealVO);
    }

    /**
     * Update dish
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @ApiOperation("Update setmeal")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("Update setmeal：{}", setmealDTO);

        setmealService.updateWithDish(setmealDTO);
        return Result.success();
    }

    /**
     * start or stop setmeal
     * @param status
     * @param id
     */
    @PostMapping("/status/{status}")
    @ApiOperation("start or stop setmeal")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result startOrStop(@PathVariable Integer status, Long id){
        log.info("Start or Stop setmeal: status:{}, id:{}", status, id);
        setmealService.startOrStop(status, id);
        return Result.success();
    }
}