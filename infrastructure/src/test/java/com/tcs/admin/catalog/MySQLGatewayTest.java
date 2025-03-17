package com.tcs.admin.catalog;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@DataJpaTest
@ComponentScan(
        basePackages = "com.tcs.admin.catalog",
        useDefaultFilters = false,
        includeFilters = {
            @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*MySQLGateway")
})
@Tag("integrationTest")
public @interface MySQLGatewayTest {
}
