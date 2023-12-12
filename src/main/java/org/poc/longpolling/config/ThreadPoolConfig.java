package org.poc.longpolling.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean("dispatcherResultWriterPool")
    public TaskExecutor dispatcherResultWriterPool() {
        ThreadPoolTaskExecutor t = new ThreadPoolTaskExecutor();
        t.setCorePoolSize(4);
        t.setMaxPoolSize(8);
        t.setQueueCapacity(100);
        t.setThreadNamePrefix("deferred-rslt-");
        return t;
    }
}
