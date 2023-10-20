package com.example.challenge.model;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class LowCourseGpa implements courseGpa {
    @Override
    public int getGpa() {
        return 3;
    }
}