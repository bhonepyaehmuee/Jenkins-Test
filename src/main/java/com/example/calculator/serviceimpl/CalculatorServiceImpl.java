package com.example.calculator.serviceimpl;

import com.example.calculator.service.CalculatorService;
import org.springframework.stereotype.Service;

@Service
public class CalculatorServiceImpl implements CalculatorService {
    @Override
    public int sum(int num1, int num2) {
        return num1 + num2;
    }
}
