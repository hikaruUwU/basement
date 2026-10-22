package com.demo.infra.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

/**
 * Bean Validation 调优的占位(fail-fast 的 Hibernate Validator 绑定 i18n 消息源);
 * 当前未启用 —— 请求载荷改由各模块的 {@code *Assertion} 组件校验。
 * <p>
 * Placeholder for Bean Validation tuning (fail-fast Hibernate Validator bound to the i18n
 * message source); currently inert — request payloads are checked by the per-module
 * {@code *Assertion} components instead.
 */
@Configuration
@RequiredArgsConstructor
public class ValidatorConfig {
//    private final MessageSource messageSource;
//    @Bean
//    public LocalValidatorFactoryBean validator() {
//        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
//        factoryBean.setProviderClass(HibernateValidator.class);
//        factoryBean.setValidationMessageSource(messageSource);
//
//        Map<String, String> props = factoryBean.getValidationPropertyMap();
//        props.put("hibernate.validator.fail_fast", "true");
//        props.put("hibernate.validator.allow_parallel_method_parameter_constraint", "true");
//        props.put("hibernate.validator.allow_multiple_cascaded_validation_on_result", "true");
//
//        return factoryBean;
//    }

}
