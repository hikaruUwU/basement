package com.demo.base.service;

import com.mybatisflex.annotation.UseDataSource;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@ExtensionMethod({StringUtils.class})
@RequiredArgsConstructor
@UseDataSource("master")
public class AuthService {

}