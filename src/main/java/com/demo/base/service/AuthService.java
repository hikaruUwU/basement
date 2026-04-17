package com.demo.base.service;

import com.mybatisflex.annotation.UseDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@UseDataSource("master")
public class AuthService {

    public void process() {

    }


}