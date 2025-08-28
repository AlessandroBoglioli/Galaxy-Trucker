package it.polimi.ingsoftware.ll13.utils.input;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class InputChecker{
    private static final Scanner in = new Scanner(System.in);
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^(([0-9]{1,3})\\.){3}([0-9]{1,3})$"
    );

    public static boolean isValidIP(String ip) {
        if (!IPV4_PATTERN.matcher(ip).matches()) {
            return false;
        }
        String[] segments = ip.split("\\.");
        for (String segment : segments) {
            int num = Integer.parseInt(segment);
            if (num < 0 || num > 255) {
                return false;
            }
        }

        return true;
    }
    public static boolean isValidPort(String port) {
        try {
            int portNumber = Integer.parseInt(port);
            return portNumber >= 1024 && portNumber <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isStringValid(String[] expected, String gotten){
        if(gotten.isEmpty()) {
            return false;
        }

        return expected[0].equalsIgnoreCase(gotten) || expected[1].equalsIgnoreCase(gotten);
    }
}
