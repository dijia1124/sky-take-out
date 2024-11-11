package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "shop related interface")
@Slf4j
public class ShopController {

    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    /**
     * set shop status
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("set shop status")
    public Result setStatus(@PathVariable Integer status) {
        log.info("set shop status: {}", status == 1 ? "Open" : "Close");
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }

    /**
     * get shop status
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("get shop status")
    public Result getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        if (status == null) {
            status = 0;
        }
        log.info("get shop status: {}", status == 1 ? "Open" : "Close");
        return Result.success(status);
    }

}
