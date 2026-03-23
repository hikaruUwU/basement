package com.demo.base.annotation.detective;

import com.demo.base.config.SQLLog4j2Config;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(SQLLog4j2Config.class)
@SuppressWarnings("unused")
public @interface EnableSqlLighthouse {
}
