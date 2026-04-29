package com.example.calculator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController {

    @GetMapping("/add")
    public String makeCalculator(@RequestParam int a, @RequestParam int b) {
        int totalSum = a + b;
        return "Here is the result -> " + totalSum;
    }
}
