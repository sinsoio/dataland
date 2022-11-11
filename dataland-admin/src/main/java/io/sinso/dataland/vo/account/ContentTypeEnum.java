package io.sinso.dataland.vo.account;

import lombok.Getter;

/**
 * @author hengbol
 * @date 8/3/22 11:17 AM
 */
@Getter
public enum ContentTypeEnum {
    ALL("all"),
    IMAGE("image"),
    AUDIO("audio"),
    VIDEO("video");

    private String type;

    ContentTypeEnum(String type) {
        this.type = type;
    }
}
