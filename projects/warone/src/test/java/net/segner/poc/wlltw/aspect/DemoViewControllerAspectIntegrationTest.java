package net.segner.poc.wlltw.aspect;

import net.segner.poc.wlltw.service.HelloService;
import net.segner.poc.wlltw.view.DemoViewController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.Model;

import static org.hamcrest.CoreMatchers.is;

/**
 * Integration test demonstrating one way of verifying aspect pointcuts
 *
 * @author asegner
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        loader = SpringockitoContextLoader.class,
        locations = {
                "classpath:spring/shared-services.xml",
                "classpath:spring/appone-context.xml",
                "classpath:spring/appone-view-context.xml"
        })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DemoViewControllerAspectIntegrationTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private DemoCounter demoCounter;
    @Autowired
    @ReplaceWithMock
    private HelloService helloService;
    private Model model;

    /**
     * NOTE:
     * Placing a weaved class as a field in the test will cause the class to load before the weaver and it will not be weaved
     * Alternatively, load the bean method-local using the beanFactory
     */
    //     private DemoViewController demoViewController;
    @Autowired
    private BeanFactory beanFactory;

    @Before
    public void setup() {
        model = Mockito.mock(Model.class);
    }

    @Test
    public void testAspectAroundHelloRequest() {
        DemoViewController demoViewController = beanFactory.getBean(DemoViewController.class);
        demoViewController.helloWorld(model);

        //verification
        Assert.assertThat("Check counter to verify around advice was called", demoCounter.getAroundAdvise(), is(1));
        Assert.assertThat("Check counter to verify throw advice was not called", demoCounter.getThrowAdvise(), is(0));
    }

    @Test
    public void testAspectExceptionInHelloRequest() {
        //setup
        DemoViewController demoViewController = beanFactory.getBean(DemoViewController.class);
        Mockito.when(helloService.getHello()).thenThrow(new RuntimeException());
        boolean firedExceptionFlag = false;

        //test
        try {
            demoViewController.helloWorld(model);
        } catch (Exception e) {
            firedExceptionFlag = true;
        }

        //verification
        Assert.assertTrue(firedExceptionFlag);
        Assert.assertThat("Check counter to verify around advice was called", demoCounter.getAroundAdvise(), is(1));
        Assert.assertThat("Check counter to verify throw advice was called", demoCounter.getThrowAdvise(), is(1));
    }

}