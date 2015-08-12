package segner.poc.wlltw.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

/**
 * @author aaronsegner
 */
@Service("helloService")
public class HelloServiceImpl implements HelloService {

    private static final Logger LOGGER = Logger.getLogger(HelloService.class.getName());
    private static final String GREETING = "Saying hello #";

    private int helloCounter = 0;

    @PostConstruct
    public void postConstruct() {
        LOGGER.info("postConstruct helloService " + this.toString());
    }


    public String getHello() {
        return "[" + toString() + "] " + GREETING + getHelloCount();
    }

    @Cacheable(value = "default")
    private int getHelloCount(){
        return ++helloCounter;
    }
}
