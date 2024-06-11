package nl.quintor.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@TestConfiguration
@ComponentScan(basePackages = "nl.quintor")
@EnableTransactionManagement
public class TestApplicationContext {
}
