package com.example.user;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

public class BaseTestContainer {
//we only want to start our Test Container once - rather than stop/start for each test
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest")
            .withDatabaseName("rocknrollalbumart")
            .withUsername("root")
            .withPassword("password");

    static {
        mySQLContainer.start();
    }

    @DynamicPropertySource
    static void setDataSourceProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
        dynamicPropertyRegistry.add("spring.datasource.url",mySQLContainer::getJdbcUrl);
    }


}
