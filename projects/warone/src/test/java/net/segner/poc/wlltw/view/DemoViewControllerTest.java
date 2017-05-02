package net.segner.poc.wlltw.view;

import net.segner.poc.wlltw.aspect.DemoCounter;
import net.segner.poc.wlltw.service.HelloService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author asegner
 */
@RunWith(MockitoJUnitRunner.class)
public class DemoViewControllerTest {

    @InjectMocks
    private DemoViewController demoViewController;
    @Mock
    private HelloService helloService;
    @Mock
    private DemoCounter demoCounter;
    @Mock
    private Model model;

    @Test
    public void testRequestForNoActiveAdvice() {
        demoViewController.helloWorld(model);

        // verification
        verify(demoCounter, times(0)).incrementAroundAdvise();
        verify(demoCounter, times(0)).incrementThrowAdvise();
        verify(helloService, times(1)).getHello();

    }
}