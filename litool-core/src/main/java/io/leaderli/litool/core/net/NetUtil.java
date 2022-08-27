package io.leaderli.litool.core.net;

import io.leaderli.litool.core.meta.Lino;

import java.net.InetAddress;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public class NetUtil {

/**
 * @param address IP地址
 * @return 查看IP地址是否可以访问
 */
public static boolean pingable(String address) {

    return Lino.of(address)
            .throwable_map(InetAddress::getByName)
            .throwable_map(net -> net.isReachable(10000))
            .get(false);

}
}
