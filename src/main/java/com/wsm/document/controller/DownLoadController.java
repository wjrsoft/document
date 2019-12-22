package com.wsm.document.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class DownLoadController {
    @GetMapping("/downloadhtml")
    public String download() {
    	return "download";
    }
}
