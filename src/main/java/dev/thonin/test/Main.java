package dev.thonin.test;

import dev.thonin.annotations.Logger;

@Logger(id="names", type = String.class)
@Logger(id="cars", type = String.class)
public class Main {

    private static final String[] names = {"Richboy", "Kaycee", "David"};

    public Main(){

    }

    public static void main(String[] args){
        new Main();
    }
}
