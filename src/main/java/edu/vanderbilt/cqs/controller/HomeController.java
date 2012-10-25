package edu.vanderbilt.cqs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController extends RootController {
	@RequestMapping("/")
	public String goindex() {
		return "redirect:/home";
	}

	@RequestMapping("/home")
	public String gohome(ModelMap model) {
		return "home";
	}
}