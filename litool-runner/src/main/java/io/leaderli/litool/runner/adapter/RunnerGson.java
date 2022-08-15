package io.leaderli.litool.runner.adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.leaderli.litool.dom.sax.SaxList;
import io.leaderli.litool.runner.instruct.Instruct;
import io.leaderli.litool.runner.xml.RequestElement;
import io.leaderli.litool.runner.xml.ResponseElement;
import io.leaderli.litool.runner.xml.funcs.FuncsElement;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public class RunnerGson {

    public static final GsonBuilder GSON_BUILDER;
    public static final Gson GSON;

    static {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeHierarchyAdapter(SaxList.class, new SaxListTypeAdapter());
        gsonBuilder.registerTypeAdapter(RequestElement.class, new RequestElementTypeAdapter());
        gsonBuilder.registerTypeAdapter(ResponseElement.class, new ResponseElementTypeAdapter());
        gsonBuilder.registerTypeAdapter(FuncsElement.class, new FuncsElementTypeAdapter());
        gsonBuilder.registerTypeHierarchyAdapter(Instruct.class, new InstructTypeAdapter());


        GSON_BUILDER = gsonBuilder;
        GSON = GSON_BUILDER
                .setPrettyPrinting()
               .create();

    }

}

