package net.segner.poc.wlltw.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author aaronsegner
 */
@Service
@Slf4j
public class HelloServiceImpl implements HelloService {

    private static final String GREETING = "Saying hello #";

    private int helloCounter = 0;

    @PostConstruct
    public void postConstruct() {
        log.info("postConstruct helloService " + this.toString());
    }


    public String getHello() {
        return "[" + toString() + "] " + GREETING + getHelloCount();
    }

    @Cacheable(value = "default")
    private int getHelloCount() {
        return ++helloCounter;
    }
}
