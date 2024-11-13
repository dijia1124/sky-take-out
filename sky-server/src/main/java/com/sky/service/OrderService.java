package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.dto.OrdersPaymentDTO;

public interface OrderService {
    /**
     * Submit order
     *
     * @param ordersSubmitDTO
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * order payment
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * Payment success, update order status
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * page query order
     * @param ordersPageQueryDTO
     */
    PageResult pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * condition query order
     * @param ordersPageQueryDTO
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);
}
