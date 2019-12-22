package com.wsm.document.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShopController {

	 @GetMapping("/shopHtml")
	    public String shopHtml() {
	    	return "shopHtml";
	    }
}
