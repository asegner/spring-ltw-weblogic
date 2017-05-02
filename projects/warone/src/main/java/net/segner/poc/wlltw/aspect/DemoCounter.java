package net.segner.poc.wlltw.aspect;

/**
 * @author asegner on 5/2/17
 */
public interface DemoCounter {

    void incrementAroundAdvise();

    void incrementThrowAdvise();

    int getAroundAdvise();

    int getThrowAdvise();
}
