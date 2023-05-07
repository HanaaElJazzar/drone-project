package com.musala.droneproject.module.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:drone.properties", ignoreResourceNotFound = true)
@Getter
public class DronePropertyValues
{
    @Value("${drone.fleet:10}")
    private Long droneFleet;
}
