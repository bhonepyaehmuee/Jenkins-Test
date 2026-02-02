package com.example.calculator.servicetest;

import com.example.calculator.service.CalculatorService;
import com.example.calculator.serviceimpl.CalculatorServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CalculatorTest {
    private final CalculatorService calculatorTest = new CalculatorServiceImpl();

    @Test
    void shouldReturnTotalSum() {
        int result = calculatorTest.sum(2, 4);
        assertEquals(6, result);
    }
}
