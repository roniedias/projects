package br.com.senchaDefaultProject.controller;

import java.io.IOException;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
public class SenchaDefaultProjectController {


	@RequestMapping("index")
	public String indexForm() {		
		return "index";
	}
	
	
	@RequestMapping("users.json")
	public void users(HttpServletRequest request, HttpServletResponse response) {
		
		String jsonString = "{\"success\": true, \"users\": [{\"id\": 1, \"name\": \"Ed\", \"email\": \"ed@sencha.com\"}, {\"id\": 2, \"name\": \"Tommy\", \"email\": \"tommy@sencha.com\"}]}";
		 
		
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
			response.getWriter().write(jsonString);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	


	@RequestMapping(value = "updateUsers.json", method=RequestMethod.POST)
	public void updateUsers(@RequestBody String postPayload, HttpServletRequest request, HttpServletResponse response) {
		
		int id = 0;
		String name = null;
		String email = null;
		
		try {
			
			JSONObject jsonObject = new JSONObject(postPayload);
		
			id = jsonObject.getInt("id");
			name = jsonObject.getString("name");
			email = jsonObject.getString("email");
			

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		System.out.println("id: " + id);
		System.out.println("name: " + name);
		System.out.println("email: " + email);
		
		String returnResponse = "{\"success\": true}";
		
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
			response.getWriter().write(returnResponse);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
	}


	
}
