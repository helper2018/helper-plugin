/*
 * Copyright 2021-2031 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.helper.demo.app;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <p>
 * 类名:AppApplication.
 * 2021/10/12 13:46
 * <p>
 *
 * @author qiang wang
 * @version 1.0.0
 */
@SpringBootApplication
@ServletComponentScan
@MapperScan("org.helper.demo.service.module.**.dao")
@ComponentScan(basePackages = {"org.helper.base.config", "org.helper.demo.app", "org.helper.demo.service"})
@EnableTransactionManagement
public class AppApplication {
    private static final Logger logger = LoggerFactory.getLogger(AppApplication.class);

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication application = new SpringApplication(AppApplication.class);
        final ApplicationContext applicationContext = application.run(args);
        Environment environment = applicationContext.getEnvironment();
        logger.info("\n--------------------------------------------------------\n\t"
                        + "Application '{}' is running! Access URLS: \n\t "
                        + "API DOC: \thttp://{}:{}/swagger-ui/index.html\n\t"
                        + "Local:   \thttp://localhost:{}\n\t"
                        + "External:\thttp://{}:{}\n--------------------------------------------------------",
                environment.getProperty("spring.application.name"),
                InetAddress.getLocalHost().getHostAddress(),
                environment.getProperty("server.port"),
                environment.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                environment.getProperty("server.port"));
    }
}
