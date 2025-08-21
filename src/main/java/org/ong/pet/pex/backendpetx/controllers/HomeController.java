package org.ong.pet.pex.backendpetx.controllers;

import org.springframework.stereotype.Controller;

@Controller
public class HomeController extends PageControl {



    @Override
    void begin() {

    }

    public String home() {
        begin();
        return "index";
    }


}
