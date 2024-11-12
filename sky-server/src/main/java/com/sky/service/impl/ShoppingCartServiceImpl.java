package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * Add shopping cart
     * @param shoppingCartDTO
     */
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);

        // user can only view their own shopping carts
        shoppingCart.setUserId(BaseContext.getCurrentId());
        System.out.println("shoppingCart.getUserId() = " + shoppingCart.getUserId());

        // check whether the current shopping cart already contains the dish
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);

        if (shoppingCartList != null && shoppingCartList.size() == 1) {
            // if the current shopping cart already contains the dish, update the number of the dish
            shoppingCart = shoppingCartList.get(0);
            shoppingCart.setNumber(shoppingCart.getNumber() + 1);
            shoppingCartMapper.updateNumbersById(shoppingCart);
        }
        else {
            // if the current shopping cart does not contain the dish, insert the dish into the shopping cart
            // check whether the dto is a dish or a setmeal
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                // this is a dish
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {
                // this is a setmeal
                Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            // set the number of the item to 1
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * List shopping cart
     * @return
     */
    public List<ShoppingCart> listShoppingCart() {
        return shoppingCartMapper.list(ShoppingCart
                                        .builder()
                                        .userId(BaseContext.getCurrentId())
                                        .build());
    }

    /**
     * Delete shopping cart
     * @param id
     */
    public void deleteShoppingCart() {
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
    }

    /**
     * Subtract shopping cart
     * @param shoppingCartDTO
     */
    public void subtractShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        // check if shopping cart list has items
        if (shoppingCartList != null) {
            for (ShoppingCart item : shoppingCartList) {
                // check if the item number is greater than 1
                if (item.getNumber() > 1) {
                    item.setNumber(item.getNumber() - 1);
                    shoppingCartMapper.updateNumbersById(item);
                } else {
                    // if the item number is 1, delete the item
                    shoppingCartMapper.delete(item);
                }
            }
        }
    }
}
