package com.micro.lcl.config.log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author lazasha
 * @date 2019/8/6 13:02
 */
public class HostAddress {
    public static String addHostAddress(String loggerFileName) {
        String hostName = getLocalHostAddress();
        String loggerFileNameWithHostAddress;
        int index = loggerFileName.lastIndexOf(".");
        if (index < 0) {
            loggerFileNameWithHostAddress = loggerFileName + "-" + hostName;
        } else {
            String pre = loggerFileName.substring(0, index);
            String suffix = loggerFileName.substring(index);
            loggerFileNameWithHostAddress = pre + "-" + hostName + suffix;
        }
        return loggerFileNameWithHostAddress;
    }

    private static String getLocalHostAddress() {
        Enumeration allNetInterfaces;
        String ipLocalAddr = null;
        InetAddress ip;

        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration addresses = netInterface.getInetAddresses();

                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    //IP是ipv4，ipv6换成Inet6Address
                    if (ip instanceof Inet4Address) {
                        String hostAddress = ip.getHostAddress();
                        if (!"127.0.0.1".equals(hostAddress) && !"/127.0.0.1".equals(hostAddress)) {
                            //得到本地IP
                            ipLocalAddr = ip.toString().split("[/]")[1];
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipLocalAddr;
    }
}
