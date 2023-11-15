package it.unibz;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        var validator = new Validator();

        validator.loadRules();


    }
}