package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * get order by its id
     * @param id
     * @return
     */
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    /**
     * count by status
     * @param status
     */
    @Select("select count(1) from orders where status = #{status}")
    int countByStatus(Integer status);

    /**
     * query orders by status and order time
     * @param status
     * @param orderTime
     */
    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> listByStatusAndOrderTime(Integer status, LocalDateTime orderTime);

    /**
     * sum by map
     * @param map
     * @return
     */
    Double sumByMap(Object map);

    /**
     * count orders based on time range and status
     * @param map
     * @return
     */
    Integer countByMap(Object map);
}
