package io.leaderli.litool.core.resource.net;

import io.leaderli.litool.core.meta.Lino;

import java.net.InetAddress;

/**
 * A utilities of net
 *
 * @author leaderli
 * @since 2022/6/27
 */
public class NetUtil {

    /**
     * @param address the ip address
     * @return Check if the IP address is accessible
     */
    public static boolean pingable(String address) {

        return Lino.of(address)
                .throwable_map(InetAddress::getByName)
                .throwable_map(net -> net.isReachable(10000))
                .get(false);

    }
}
