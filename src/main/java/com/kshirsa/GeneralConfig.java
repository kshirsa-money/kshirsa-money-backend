package com.kshirsa;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.concurrent.Executor;

@Configuration
@RequiredArgsConstructor
public class GeneralConfig {

    private final Environment env;

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Scheduled(fixedDelay = 1000*60*5)
    void renderKeepAlive() {
        if (Arrays.asList(env.getActiveProfiles()).contains("dev")) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForEntity("https://kshirsa-money-backend-dev.onrender.com/kshirsa/api/v1/auth/index", String.class);
        }
    }

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("async-");
        executor.initialize();
        // Wrap the executor to propagate the security context
        return new DelegatingSecurityContextTaskExecutor(executor);
    }
}
