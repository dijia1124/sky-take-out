package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

public interface UserService {
    /**
     * WeChat login
     * @param UserLoginDTO
     * @return
     */
    User wxLogin(UserLoginDTO UserLoginDTO);
}
