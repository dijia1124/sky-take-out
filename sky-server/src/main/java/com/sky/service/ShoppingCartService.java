package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    /**
     * Add shopping cart
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * List shopping cart
     * @return
     */
    List<ShoppingCart> listShoppingCart();

    /**
     * Delete shopping cart
     */
    void deleteShoppingCart();

    /**
     * Subtract shopping cart
     * @param shoppingCartDTO
     */
    void subtractShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
