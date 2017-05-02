package net.segner.poc.wlltw.view;

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
    private static final String MSG_PREPREND = "Different class with same classname, separate child classloader provides adequate segregation of classes between wars.<br>Demonstrated by different msg. Parent classloader and context is still shared - same injected shared service: ";

    private final HelloService helloService;

    public DemoViewController(HelloService helloService) {
        this.helloService = helloService;
    }

    @RequestMapping(value = "hello", method = RequestMethod.GET)
    public String helloWorld(Model model) {
        String msg = MSG_PREPREND + helloService.getHello();
        model.addAttribute(VIEWKEY_MSG, msg);
        return VIEWNAME_HELLO;
    }
}
