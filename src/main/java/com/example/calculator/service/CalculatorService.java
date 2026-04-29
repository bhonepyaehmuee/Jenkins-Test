package com.example.calculator.service;


import org.springframework.cache.annotation.Cacheable;

public interface CalculatorService {
    public int sum(int a, int b);


}
