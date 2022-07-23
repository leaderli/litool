package io.leaderli.litool.runner;

import com.google.gson.Gson;
import io.leaderli.litool.dom.parser.LiDomDFSContext;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @author leaderli
 * @since 2022/7/24
 */
class RequestElementTest {


    @Test
    void test() throws ParserConfigurationException, IOException, SAXException {

        RequestElement parse = LiDomDFSContext.parse("request.xml", RequestElement.class);

        Gson gson = new Gson();
        System.out.println(gson.toJson(parse));

    }
}
