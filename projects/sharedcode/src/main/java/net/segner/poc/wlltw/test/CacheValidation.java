package net.segner.poc.wlltw.test;


import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component("cacheValidation")
public class CacheValidation implements SmartInitializingSingleton {

    private static final Logger LOGGER = Logger.getLogger(CacheValidation.class.getName());
    private int invocationCount = 0;

    @Override
    public void afterSingletonsInstantiated() {

        cacheableMethodInSameClass();
        cacheableMethodInSameClass();
        clearCache();

        LOGGER.info("Load time weaving cache test passed.");

    }

    @CacheEvict(value = "default", allEntries = true)
    private void clearCache() {//empty method just for clearing cache

    }

    @Cacheable(value = "default")
    private int cacheableMethodInSameClass() {

        invocationCount++;
        if (invocationCount > 1) { // true if we get here twice...
            throw new RuntimeException("Cache is disabled or Load Time Weaving is not working properly.");
        }
        return 0;
    }
}
