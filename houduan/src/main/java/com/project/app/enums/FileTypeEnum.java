package com.project.app.enums;

public enum FileTypeEnum {
    FILE("FILE"),
    PIC("PIC"),
    DOC("DOC"),
    AUDIO("AUDIO"),
    VIDEO("VIDEO"),
    ZIP("ZIP");


    private final String type;


    FileTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
