package com.example.calculator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController {

    @GetMapping("/")
    public String makeCalculator(@RequestParam int num1, @RequestParam int num2) {
        int totalSum = num1 + num2;
        return "Here is the result -> " + totalSum;
    }
}
