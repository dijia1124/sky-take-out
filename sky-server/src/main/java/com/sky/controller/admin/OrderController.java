package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "Admin end: order interface")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Order condition search
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("Order condition search")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("Order condition search: {}", ordersPageQueryDTO);
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * order statistics
     * @return
     */
    @GetMapping("/statistics")
    @ApiOperation("order statistics")
    public Result<Map<String, Integer>> statistics() {
        // return data with integers: confirmed, deliveryInProgress, toBeConfirmed
        return Result.success(orderService.statistics());
    }
}
