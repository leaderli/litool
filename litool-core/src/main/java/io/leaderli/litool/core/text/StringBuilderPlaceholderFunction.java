package io.leaderli.litool.core.text;

abstract class StringBuilderPlaceholderFunction implements PlaceholderFunction {

    private final StringBuilder stringBuilder = new StringBuilder();

    public void append(Object append) {
        this.stringBuilder.append(append);
    }

    @Override
    public void literal(StringBuilder literal) {
        append(literal);
    }

    @Override
    public String toString() {
        return this.stringBuilder.toString();
    }
}
