package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * confirm order
     * @param ordersConfirmDTO
     */
    @PutMapping("/confirm")
    @ApiOperation("confirm order")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        orderService.adminConfirmOrder(ordersConfirmDTO);
        return Result.success();
    }

    /**
     * reject order
     * @param ordersRejectionDTO
     */
    @PutMapping("/rejection")
    @ApiOperation("reject order")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        orderService.adminRejectOrder(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * cancel order
     * @param OrdersCancelDTO
     */
    @PutMapping("/cancel")
    @ApiOperation("cancel order")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        orderService.adminCancelOrder(ordersCancelDTO);
        return Result.success();
    }
}
