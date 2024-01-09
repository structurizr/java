package com.structurizr.graphviz;

/**
 * The various rank directions used by graphviz.
 */
public enum RankDirection {

    TopBottom("TB"),
    BottomTop("BT"),
    LeftRight("LR"),
    RightLeft("RL");

    private String code;

    RankDirection(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}