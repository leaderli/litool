package io.leaderli.litool.test;

public interface MethodAssert {
    /**
     * @param _this   方法调用者，static方法为 null
     * @param args    方法的参数
     * @param _return 放的返回值
     * @return 返回断言错误信息， 当不为空时表示断言失败
     */
    String apply(Object _this, Object[] args, Object _return);
}
