package com.skhanra52;
/*
In the below code we have ,
ANSI_RESET("\u001B[0m"),
    ANSI_BLACK("\u001B[30m"),
    ANSI_WHITE("\u001b[37m"),

    which are nothing but as below:

 public final class ThreadColor extends Enum<ThreadColor> {
    public static final ThreadColor ANSI_RESET = new ThreadColor("\u001B[0m");
    public static final ThreadColor ANSI_BLACK = new ThreadColor("\u001B[30m");
    public static final ThreadColor ANSI_WHITE = new ThreadColor("\u001B[37m");
}
 */

public enum ThreadColor {

    ANSI_RESET("\u001B[0m"),
    ANSI_BLACK("\u001B[30m"),
    ANSI_WHITE("\u001b[37m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_CYAN("\u001B[36m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_PURPLE("\u001B[35m"),
    ANSI_RED("\u001B[31m"),
    ANSI_YELLOW("\u001B[33m");

    private final String color;

    ThreadColor(String color) {
        this.color = color;
    }

    public String getColor(){
        return color;
    }
}
