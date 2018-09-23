package com.github.resource4j.demo.admin;

import com.github.resource4j.objects.providers.mutable.ResourceValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;

import static com.github.resource4j.ResourceKey.plain;
import static com.github.resource4j.resources.context.ResourceResolutionContext.in;

@Controller
@RequestMapping("/cms")
public class CMSController {

    @Autowired
    private ResourceValueRepository repository;

    @RequestMapping(method = RequestMethod.POST)
    public String update(@RequestParam Locale locale,
                         @RequestParam String key,
                         @RequestParam String value) {
        repository.put(plain(key), in(locale), value);
        return "redirect:/";
    }

}
