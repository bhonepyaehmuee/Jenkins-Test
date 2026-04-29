package com.example.calculator.serviceimpl;

import com.example.calculator.service.CalculatorService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CalculatorServiceImpl implements CalculatorService {
//    @Override
//    public int sum(int a, int b) {
//        return a + b;
//    }
    @Cacheable(value = "sum")
    public int sum(int a, int b)
    {
        try{
            System.out.println("loading ....");
            Thread.sleep(3000);
            System.out.println("end ....");
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        return a+b;
    }
}
