package io.leaderli.litool.core.text;

public interface PlaceholderFunction {

    void literal(StringBuilder literal);

    void variable(StringBuilder variable);
}
