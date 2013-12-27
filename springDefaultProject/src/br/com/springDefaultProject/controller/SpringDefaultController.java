package br.com.springDefaultProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpringDefaultController {

	
	
	@RequestMapping("index")
	public String indexForm() {		
		return "index";
	}

	
	
}
