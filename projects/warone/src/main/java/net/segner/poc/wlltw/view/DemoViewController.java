package net.segner.poc.wlltw.view;

import net.segner.poc.wlltw.aspect.DemoAspectHere;
import net.segner.poc.wlltw.aspect.DemoCounter;
import net.segner.poc.wlltw.service.HelloService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * This class is intentionally named the same as the other controller class in this demo to demonstrate child classloader segregation.
 * This is not recommended for real world applications.
 *
 * @author aaronsegner
 */
@Controller
public class DemoViewController {
    private static final String VIEWNAME_HELLO = "hello.jsp";
    private static final String VIEWKEY_MSG = "msg";

    private final HelloService helloService;
    private final DemoCounter aspectCounter;

    public DemoViewController(HelloService helloService, DemoCounter aspectCounter) {
        this.helloService = helloService;
        this.aspectCounter = aspectCounter;
    }

    @RequestMapping(value = "hello", method = RequestMethod.GET)
    public String helloWorld(Model model) {
        model.addAttribute(VIEWKEY_MSG, getMessage());
        return VIEWNAME_HELLO;
    }

    @DemoAspectHere
    private String getMessage() {
        return helloService.getHello() + " " + aspectCounter.toString();
    }
}
