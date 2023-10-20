package com.example.challenge.model;


import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class Grade {
    private int coefficient;
    private String note;

    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}