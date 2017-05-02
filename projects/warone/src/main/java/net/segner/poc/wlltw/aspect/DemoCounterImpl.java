package net.segner.poc.wlltw.aspect;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author asegner on 5/2/17
 */
@Component
@Data
public class DemoCounterImpl implements DemoCounter {

    private int throwAdvise, aroundAdvise;

    public void incrementAroundAdvise() {
        aroundAdvise++;
    }

    public void incrementThrowAdvise() {
        throwAdvise++;
    }

}

