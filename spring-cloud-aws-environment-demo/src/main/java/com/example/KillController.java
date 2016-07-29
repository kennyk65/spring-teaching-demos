package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class KillController {

	@Autowired KillService killService;
	
	
	@RequestMapping("/kill")
	public String kill() throws Exception {
		killService.killTheJvm();
		return "redirect:/";
	}

}
