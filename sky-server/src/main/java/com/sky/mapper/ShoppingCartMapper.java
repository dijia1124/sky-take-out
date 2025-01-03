package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * conditional query
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * update numbers of shopping cart by its id
     * @param shoppingCart
     * @return
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    int updateNumbersById(ShoppingCart shoppingCart);

    /**
     * insert one record into shopping cart
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart (name, user_id, image, dish_id, setmeal_id, dish_flavor, number, amount, create_time) values (#{name}, #{userId}, #{image}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * delete shopping cart by current user id
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);

    /**
     * delete shopping cart item
     * @param shoppingCart
     */
    void delete(ShoppingCart shoppingCart);

    /**
     * insert shopping cart in batch
     * @param shoppingCartList
     */
    void insertBatch(List<ShoppingCart> shoppingCartList);
}
