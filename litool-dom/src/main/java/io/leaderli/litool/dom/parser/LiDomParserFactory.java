package io.leaderli.litool.dom.parser;

import io.leaderli.litool.core.exception.LiThrowableSupplier;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.dom.sax.SaxBean;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/6 8:52 AM
 */
public class LiDomParserFactory {


    private static final Map<String, LiThrowableSupplier<Constructor<? extends SaxBean>>> NAME_CONSTRUCTOR = new HashMap<>();


    public static void register(String name, Class<? extends SaxBean> cls) {
        NAME_CONSTRUCTOR.put(name, cls::getConstructor);
    }


    public static Lino<SaxBean> create(String tagName) {

        return Lino.of(NAME_CONSTRUCTOR.get(tagName))
                .throwable_map(LiThrowableSupplier::get)
                .throwable_map(Constructor::newInstance);
    }

}
