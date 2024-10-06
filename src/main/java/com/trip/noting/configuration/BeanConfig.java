package com.trip.noting.configuration;

import com.alibaba.fastjson2.JSON;
import com.trip.noting.graph.DirectedAcyclicGraph;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class BeanConfig {

    @Bean
    public World world() {
        return new World("world-bean");
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // @Bean
    // public Hello hello(World world) {
    //     Hello hello = new Hello();
    //     hello.setWorld(world);
    //     System.out.println(JSON.toJSONString(hello));
    //     return hello;
    // }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public DirectedAcyclicGraph taskGraph(ThreadPoolTaskExecutor executor) {
        DirectedAcyclicGraph directedAcyclicGraph = new DirectedAcyclicGraph();
        directedAcyclicGraph.executor = executor;
        return directedAcyclicGraph;
    }


}
