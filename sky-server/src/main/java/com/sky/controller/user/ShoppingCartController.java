package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ShoppingCartController
 */

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "Client end: shopping cart interface")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * Add shopping cart
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("Add shopping cart")
    public Result<String> add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("Add shopping cart：{}", shoppingCartDTO);

        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * List shopping cart
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("List shopping cart")
    public Result list(){
        return Result.success(shoppingCartService.listShoppingCart());
    }
}
