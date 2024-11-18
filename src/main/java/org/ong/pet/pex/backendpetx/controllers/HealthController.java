package org.ong.pet.pex.backendpetx.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@CrossOrigin(origins = {"http://localhost:4200", "https://petx-v4.onrender.com"})
public class HealthController {

    @GetMapping("/status")
    public ResponseEntity<String> statusApi() {
        return ResponseEntity.ok("Api is running");
    }

}
