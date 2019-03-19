package com.elector;

import com.elector.Enums.ConfigEnum;
import com.elector.Utils.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SampleApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SampleApplication.class, args);
        if (ConfigUtils.getConfig(ConfigEnum.log_beans_names, false)) {
            LOGGER.info("Loaded beans:");
            for(String beanName : context.getBeanDefinitionNames()) {
                LOGGER.info(beanName);
            }
        }
        LOGGER.info("Application started.");
    }
}
