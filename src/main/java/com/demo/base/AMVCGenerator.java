package com.demo.base;

import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.zaxxer.hikari.HikariDataSource;

public class AMVCGenerator {
    private static final HikariDataSource dataSource = new HikariDataSource();

    static {
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/connector?useSSL=false&characterEncoding=utf-8&useInformationSchema=true");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
    }

    public static void main(String[] args) {
        GlobalConfig globalConfig = new GlobalConfig();

        globalConfig.getPackageConfig()
                .setBasePackage("flex.mybatis.generator")
                .setSourceDir(System.getProperty("user.dir") + "/src/main/generated");

        globalConfig.enableEntity()
                .setJdkVersion(Integer.MAX_VALUE)
                .setWithLombok(true)
                .setOverwriteEnable(true);
        globalConfig.enableMapper()
                .setOverwriteEnable(true);
        globalConfig.enableService()
                .setOverwriteEnable(false);
        globalConfig.enableController()
                .setRestStyle(Boolean.TRUE)
                .setOverwriteEnable(false);
        globalConfig.enableMapperXml()
                .setOverwriteEnable(true);

        Generator generator = new Generator(dataSource, globalConfig);
        generator.generate();
    }
}