package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.mapper.OrderMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "Client end: order interface")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMapper orderMapper;

    /**
     * Submit order
     *
     * @param ordersSubmitDTO
     */
    @PostMapping("/submit")
    @ApiOperation("Submit order")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("Submit order: {}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * order payment
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        // OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        // we're not going to implement the payment logic here
        // as i don't have wechat merchant account
        OrderPaymentVO orderPaymentVO = OrderPaymentVO.builder()
                                            .nonceStr("11111111")
                                            .packageStr("22222222")
                                            .paySign("33333333")
                                            .signType("MD5")
                                            .timeStamp("44444444")
                                            .build();
        log.info("generate prepayment transaction order: {}", orderPaymentVO);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://localhost:8080/notify/paySuccess");
        httpClient.execute(httpGet);
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
        return Result.success(orderPaymentVO);
    }

    /**
     * page query history orders
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/historyOrders")
    @ApiOperation("page query history orders")
    public Result<PageResult> historyOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("query history orders: {}", ordersPageQueryDTO);
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        PageResult pageResult = orderService.pageQuery(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * query order details
     * @param orderId
     * @return
     */
    @GetMapping("/orderDetail/{orderId}")
    @ApiOperation("query order details")
    public Result<OrderVO> orderDetail(@PathVariable Long orderId) {
        log.info("query order details: {}", orderId);
        OrderVO orderVO = orderService.getById(orderId);
        return Result.success(orderVO);
    }

    /**
     * repeat order
     * @param orderId
     * @return
     */
    @PostMapping("/repetition/{orderId}")
    @ApiOperation("repeat order")
    public Result repetition(@PathVariable Long orderId) {
        log.info("repeat order: {}", orderId);
        orderService.repeatOrder(orderId);
        return Result.success();
    }
}
