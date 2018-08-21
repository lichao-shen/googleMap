package co.uk.pshealth.googleMapCache.controller;


import javax.ws.rs.Path;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


//@RestController
@Controller
@RequestMapping("/hello")
public class StaticController {

	@RequestMapping("/test")
    public String hello(Model model, @RequestParam(value="name", required=false, defaultValue="World") String name) {
	//public String hello(Model model, String name) {
        model.addAttribute("name", name);
        return "hello";
    }
	
}
