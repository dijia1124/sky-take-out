package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "Dish Interface")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Add dish
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("Add dish")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("Add dish：{}", dishDTO);

        dishService.saveWithFlavor(dishDTO);

        // clear redis cache
        clearRedisCache("dish_" + dishDTO.getCategoryId());
        return Result.success();
    }

    /**
     * Dish page query
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("Dish page query")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("Dish page query：{}", dishPageQueryDTO);

        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Delete dish
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("Delete dish")
    public Result delete(@RequestParam List<Long> ids){
        log.info("Delete dish：{}", ids);

        // clear redis cache by pattern: all of dishes
        clearRedisCache("dish_*");
        dishService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * Get dish by id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("Get dish by id")
    public Result<DishVO> get(@PathVariable Long id){
        log.info("Get dish by id：{}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * Update dish
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("Update dish")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("Update dish：{}", dishDTO);

        // clear redis cache
        clearRedisCache("dish_*");
        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * Get dish by category id
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("Get dish by category id")
    public Result<List<DishVO>> list(Long categoryId){

        // construct a key
        String key = "dish_" + categoryId;

        // if the key exists in redis, return the value without querying the database
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            if (redisTemplate.opsForValue().get(key) == null) {
                return Result.success(null);
            }
            List<DishVO> dishVOList = (List<DishVO>) redisTemplate.opsForValue().get(key);
            return Result.success(dishVOList);
        }
        // if the key does not exist in redis, query the database
        List<DishVO> dishVOList = dishService.listByCategoryId(categoryId);
        // store the result in redis
        redisTemplate.opsForValue().set(key, dishVOList);
        return Result.success(dishVOList);
    }

    /**
     * clear redis cache
     * @param pattern
     */
    private void clearRedisCache(String pattern) {
        Set keys = redisTemplate.keys(pattern);
    }
    // todo: missing status on/off method

}
