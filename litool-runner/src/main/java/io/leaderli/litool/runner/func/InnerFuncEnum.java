package io.leaderli.litool.runner.func;

public enum InnerFuncEnum    {
    AND(new IFunc() {
        @Override
        public String apply(Object[] objects) {
            return null;
        }

        @Override
        public Class<?>[] support() {
            return new Class[0];
        }
    });


    public final IFunc func;

    InnerFuncEnum( IFunc func) {
        this.func = func;
    }


}
