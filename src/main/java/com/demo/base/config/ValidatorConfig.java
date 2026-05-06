package com.demo.base.config;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ValidatorConfig {
    private final MessageSource messageSource;
    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setProviderClass(HibernateValidator.class);
        factoryBean.setValidationMessageSource(messageSource);

        Map<String, String> props = factoryBean.getValidationPropertyMap();
        props.put("hibernate.validator.fail_fast", "true");
        props.put("hibernate.validator.allow_parallel_method_parameter_constraint", "true");
        props.put("hibernate.validator.allow_multiple_cascaded_validation_on_result", "true");

        return factoryBean;
    }

}
