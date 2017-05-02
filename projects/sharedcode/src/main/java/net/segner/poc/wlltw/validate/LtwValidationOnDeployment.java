package net.segner.poc.wlltw.validate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LtwValidationOnDeployment implements SmartInitializingSingleton {

    private int invocationCount = 0;

    public void afterSingletonsInstantiated() {

        cacheablePrivateMethodInSameClass();
        cacheablePrivateMethodInSameClass();

        log.info("Load time weaving test passed.");
    }

    @Cacheable(value = "default")
    private int cacheablePrivateMethodInSameClass() {

        invocationCount++;
        if (invocationCount > 1) {
            throw new RuntimeException("Load time weaving is not working properly.");
        }
        return 0;
    }
}
