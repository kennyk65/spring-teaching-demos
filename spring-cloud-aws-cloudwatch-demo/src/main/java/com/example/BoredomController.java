package com.example;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping(value="/" )
public class BoredomController {
	
	@Autowired BoredomService boredomService;
	
	/**
	 *	Display form: 
	 */
	@RequestMapping(method=GET )
	public String getName(Model m) {
		return "boredom";
	}

	/**
	 *	Process form submit.  Send snarky quips back to page. 
	 */
	@RequestMapping(method=POST )
	public String postName(@RequestParam("level") Long level, RedirectAttributes redirect) {
		redirect.addFlashAttribute("level", level);
		redirect.addFlashAttribute("result", boredomService.assessBoredomLevel(level));
		return "redirect:/";
	}
}
