package com.github.resource4j.cms.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Locale;

@Controller
@RequestMapping("/cms")
public class CMSController {

    @ResponseBody
    public String update(@RequestParam Locale locale,
                         @RequestParam String key,
                         @RequestParam String value) {
        return "OK";
    }

}
