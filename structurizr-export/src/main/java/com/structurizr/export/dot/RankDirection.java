package com.structurizr.export.dot;

/**
 * The various rank directions used by Graphviz.
 */
enum RankDirection {

    TopBottom("TB"),
    BottomTop("BT"),
    LeftRight("LR"),
    RightLeft("RL");

    private String code;

    RankDirection(String code) {
        this.code = code;
    }

    String getCode() {
        return code;
    }

}