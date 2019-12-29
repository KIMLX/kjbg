package com.kjbg.config;

import com.kjbg.filter.SecurityFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class WebFilter {

    @Autowired
    DiscoveryClient discoveryClient;

    @Bean
    public Filter filter(){
        List<ServiceInstance> gateways = discoveryClient.getInstances("gateway");
        Set<String> stringSet = new HashSet<String>();
        gateways.forEach(instanceInfo -> {
            String ipAddr =  instanceInfo.getHost();;
            stringSet.add(ipAddr);
        });
        return new SecurityFilter(stringSet);
    }
}
