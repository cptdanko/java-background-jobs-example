package org.example;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/")
    String hello() {
        return "Hello World!";
    }

    @GetMapping("/start")
    public HttpStatus startJob() {
        System.out.println("In controller");
        BackgroundJob.startJob();
        return HttpStatus.ACCEPTED;
    }
}
