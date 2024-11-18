package org.ong.pet.pex.backendpetx.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HelthController {

    @GetMapping
    public String health() {
        return "Api is running";
    }

}
