package me.aaronakhtar.jbot.utils;

public class Terminal {

    public static void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
