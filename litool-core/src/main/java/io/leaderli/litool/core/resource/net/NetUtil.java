package io.leaderli.litool.core.resource.net;

import io.leaderli.litool.core.meta.Lino;

import java.net.InetAddress;

/**
 * 网络工具类
 */
public class NetUtil {

    /**
     * 判断指定 IP 地址是否可以访问，使用 ICMP 协议进行检测。默认情况下，检测最长持续时间为 10 秒。
     * <p>
     * 如果需要指定不同的检测时间，可以使用 {@link #pingable(String, int)} 方法。
     *
     * @param ipAddress 要检测的 IP 地址
     * @return 如果 IP 地址可以访问，则返回 true；否则返回 false
     * @see #pingable(String, int)
     */
    public static boolean pingable(String ipAddress) {

        return Lino.of(ipAddress)
                .mapIgnoreError(InetAddress::getByName)
                .mapIgnoreError(net -> net.isReachable(10000))
                .get(false);

    }

    /**
     * 判断指定 IP 地址是否可以访问，使用 ICMP 协议进行检测。可以指定检测的最长持续时间。
     *
     * @param ipAddress     要检测的 IP 地址
     * @param timeoutMillis 检测最长持续时间（以毫秒为单位）
     * @return 如果 IP 地址可以访问，则返回 true；否则返回 false
     */
    public static boolean pingable(String ipAddress, int timeoutMillis) {

        return Lino.of(ipAddress)
                .mapIgnoreError(InetAddress::getByName)
                .mapIgnoreError(net -> net.isReachable(timeoutMillis))
                .get(false);

    }
}
