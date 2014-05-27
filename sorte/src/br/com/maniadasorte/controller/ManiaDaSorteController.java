package br.com.maniadasorte.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ManiaDaSorteController {
	
	@RequestMapping("index")
	public String indexForm() {		
		return "index";
	}
	
	@RequestMapping("sobre")
	public String sobre() {		
		return "sobre";
	}
	
	@RequestMapping("contato")
	public String contato() {		
		return "index";
	}
	
	@RequestMapping("cadastro")
	public String cadastro() {		
		return "cadastro";
	}




	
	
}
