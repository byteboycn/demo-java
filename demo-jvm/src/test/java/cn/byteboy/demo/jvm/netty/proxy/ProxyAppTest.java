package cn.byteboy.demo.jvm.netty.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hongshaochuan
 */
public class ProxyAppTest {

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            runCurl("http://local:9001/", i);
        }
    }

    private static void runCurl(String targetUrl, int count) {
        System.out.println(count);
        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> command = new ArrayList<>();
        command.add("curl");
        command.add("-k");
        command.add("-x");
        command.add("127.0.0.1:9000");
        command.add(targetUrl);
        processBuilder.command(command);
        try {
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
