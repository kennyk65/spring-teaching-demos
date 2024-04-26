package config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HiController {
	@GetMapping("/hithere")
	public @ResponseBody String hiThere() {
		return "this is a rest endpoint";
	}
}
