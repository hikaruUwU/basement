package com.demo.base.module.user.mapper;

import com.demo.base.module.user.data2object.User;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
