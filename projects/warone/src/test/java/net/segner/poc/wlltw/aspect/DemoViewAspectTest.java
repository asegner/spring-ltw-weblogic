package net.segner.poc.wlltw.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
/**
 * Unit test for DemoViewAspect
 *
 * @author asegner
 */
@RunWith(MockitoJUnitRunner.class)
public class DemoViewAspectTest {

    @InjectMocks
    private DemoViewAspect demoViewAspect;
    @Mock
    private DemoCounter demoCounter;
    @Mock
    private ProceedingJoinPoint joinPoint;

    @Test
    public void testAroundAdvise() throws Throwable {
        demoViewAspect.aroundAdvise(joinPoint);

        // verifications
        verify( demoCounter, times(0)).incrementThrowAdvise();
        verify( demoCounter, times(1)).incrementAroundAdvise();
    }

    @Test
    public void testThrowAdvise() {
        demoViewAspect.throwingAdvise();

        // verifications
        verify( demoCounter, times(1)).incrementThrowAdvise();
        verify( demoCounter, times(0)).incrementAroundAdvise();
    }
}