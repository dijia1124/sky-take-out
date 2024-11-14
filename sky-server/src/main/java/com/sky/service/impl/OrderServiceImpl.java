package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.mapper.UserMapper;
import com.sky.result.PageResult;
import com.sky.service.AddressBookService;
import com.sky.service.OrderService;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private UserMapper userMapper;


    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        // exception handling (empty address book, empty shopping cart, beyond delivery range)

        // check empty address book
        AddressBook addressBook = addressBookService.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        // check empty shopping cart
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);

        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);

        if (shoppingCartList == null && shoppingCartList.size() == 0) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // check beyond delivery range
        // undefined feature, not implemented

        // construct order details
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, order);
        order.setPhone(addressBook.getPhone());
        StringBuilder address = new StringBuilder();
        address.append(addressBook.getProvinceName())
                .append(addressBook.getCityName())
                .append(addressBook.getDistrictName())
                .append(addressBook.getDetail());
        order.setAddress(address.toString());
        order.setConsignee(addressBook.getConsignee());
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setUserId(userId);
        order.setStatus(Orders.PENDING_PAYMENT);
        order.setPayStatus(Orders.UN_PAID);
        order.setOrderTime(LocalDateTime.now());

        // insert order into database
        orderMapper.insert(order);

        // construct order details
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart item : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(item, orderDetail);
            orderDetail.setOrderId(order.getId());
            orderDetailList.add(orderDetail);
        }

        // insert order details into database
        orderDetailMapper.insertBatch(orderDetailList);

        // delete shopping cart
        shoppingCartMapper.deleteByUserId(userId);

        // return order submit result
        return OrderSubmitVO.builder()
                .id(order.getId())
                .orderNumber(order.getNumber())
                .orderTime(order.getOrderTime())
                .orderAmount(order.getAmount())
                .build();

    }


    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
//        // 当前登录用户id
//        Long userId = BaseContext.getCurrentId();
//        User user = userMapper.getById(userId);
//
//        //调用微信支付接口，生成预支付交易单
//        JSONObject jsonObject = weChatPayUtil.pay(
//                ordersPaymentDTO.getOrderNumber(), //商户订单号
//                new BigDecimal(0.01), //支付金额，单位 元
//                "苍穹外卖订单", //商品描述
//                user.getOpenid() //微信用户的openid
//        );
//
//        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
//            throw new OrderBusinessException("该订单已支付");
//        }
//
//        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
//        vo.setPackageStr(jsonObject.getString("package"));
//
//        return vo;
        return null;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    /**
     * page query for orders
     *
     * @param ordersPageQueryDTO
     * @return
     */
    public PageResult pageQuery(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * page query for orders
     *
     * @param ordersPageQueryDTO
     * @return
     */
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);
        List<OrderVO> orderVOList = getOrderVOList(page.getResult());
        return new PageResult(page.getTotal(), orderVOList);
    }

    private List<OrderVO> getOrderVOList(List<Orders> ordersList) {
        List<OrderVO> orderVOList = new ArrayList<>();
        for (Orders orders : ordersList) {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(orders, orderVO);
            orderVO.setOrderDishes(getOrderString(orders));
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }

    private String getOrderString(Orders orders) {
        // get order detail list by order id
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        // concat order detail list to string ( dish name * quantity )
        StringBuilder sb = new StringBuilder();
        for (OrderDetail orderDetail : orderDetailList) {
            sb.append(orderDetail.getName()).append(" * ").append(orderDetail.getNumber()).append(" ");
        }
        return sb.toString();
    }

    /**
     * query order by its id
     *
     * @param orderId
     * @return
     */
    public OrderVO getById(Long orderId) {
        Orders orders = orderMapper.getById(orderId);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
//        orderVO.setOrderDishes(getOrderString(orders));
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orderId);
        orderVO.setOrderDetailList(orderDetailList);
        return orderVO;
    }

    /**
     * repeat order, filling up the shopping cart with the same items
     *
     * @param orderId
     */
    public void repeatOrder(Long orderId) {
        Long userId = BaseContext.getCurrentId();
        // get the original order by its id
        Orders orders = orderMapper.getById(orderId);
        // get the original order details by its id
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orderId);
        // construct a new list of shopping cart items
        List<ShoppingCart> shoppingCartList = new ArrayList<>();
        // fill up the shopping cart with the same items
        for (OrderDetail orderDetail : orderDetailList) {
            // todo: use stream to insert shopping cart
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(orderDetail, shoppingCart);
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartList.add(shoppingCart);
        }

        // insert new order into database
        shoppingCartMapper.insertBatch(shoppingCartList);
    }

    /**
     * cancel order
     *
     * @param orderId
     */
    public void userCancelOrder(Long orderId) {
        // Get the order by its id
        Orders orders = orderMapper.getById(orderId);
        // Check if the order exists
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        // Check if the order is in the right status
        // Can only be cancelled if the merchant has not confirmed the order
        if (orders.getStatus() > Orders.TO_BE_CONFIRMED) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // Check if the order is to be confirmed
        // if so, need refund
        if (orders.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            // todo: refund via third-party payment platform
            //调用微信支付退款接口
//            weChatPayUtil.refund(
//                    ordersDB.getNumber(),
//                    ordersDB.getNumber(),
//                    new BigDecimal(0.01),
//                    new BigDecimal(0.01));
            orders.setPayStatus(Orders.REFUND);
        }

        // Update the order status
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("User cancelled the order");
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * order statistics
     *
     * @return
     */
    public Map<String, Integer> statistics() {
        Map<String, Integer> map = new HashMap<>();
        map.put("confirmed", orderMapper.countByStatus(Orders.CONFIRMED));
        map.put("toBeConfirmed", orderMapper.countByStatus(Orders.TO_BE_CONFIRMED));
        map.put("deliveryInProgress", orderMapper.countByStatus(Orders.DELIVERY_IN_PROGRESS));
        return map;
    }
}
