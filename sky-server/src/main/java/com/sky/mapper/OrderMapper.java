package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {

    /**
     * insert order
     * @param order
     */
    void insert(Orders order);

    /**
     * query order by order number
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * update order
     * @param orders
     */
    void update(Orders orders);

    /**
     * page query for orders
     * @param ordersPageQueryDTO
     * @return
     */
    Page<OrderVO> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

}