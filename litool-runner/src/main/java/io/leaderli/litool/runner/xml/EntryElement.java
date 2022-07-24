package io.leaderli.litool.runner.xml;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.sax.BodyEvent;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.dom.sax.SaxBody;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public class EntryElement implements SaxBean {

    public String label;
    public SaxBody key;
    public String def = "";
    public String type = "str";

    private SaxBean father;

    @Override
    public void father(SaxBean father) {
        this.father = father;
    }

    @Override
    public void body(BodyEvent bodyEvent) {

//        SaxBean father = bodyEvent.getFather();
//        boolean duplicate = father.entryList.lira().filter(entry -> StringUtils.equals(entry.key.text, bodyEvent.description())).present();

//        System.out.println(father == this);
        //TODO 由父类自动去校验
        System.out.println(father);
        if (father instanceof RequestElement) {
            boolean duplicate = ((RequestElement) father).entryList.lira().filter(entry -> StringUtils.equals(entry.key.text, bodyEvent.description())).present();
            LiAssertUtil.assertFalse(duplicate, String.format("duplicate entry key %s at line %d", bodyEvent.description(), bodyEvent.locator.getLineNumber()));

        }
        this.key = new SaxBody(bodyEvent.description());
//        SaxBean.super.body(bodyEvent);
    }

    @Override
    public String name() {
        return "entry";
    }
}
