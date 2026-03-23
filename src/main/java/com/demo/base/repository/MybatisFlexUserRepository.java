package com.demo.base.repository;

import com.demo.base.domain.User;
import com.demo.base.mapper.UserMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class MybatisFlexUserRepository extends ServiceImpl<UserMapper, User> {

}