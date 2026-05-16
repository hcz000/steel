package com.gv.steel.common.core.enums.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.gv.steel.common.core.serializer.BaseEnumDeserializer;

import java.io.Serializable;

@JsonDeserialize(
        using = BaseEnumDeserializer.class
)
public interface BaseEnum<E extends Serializable> extends Serializable {
    @JsonCreator
    static <E extends Enum<E> & BaseEnum> E valueOf(String enumCode, Class<E> clazz) {
        return Enum.valueOf(clazz, enumCode);
    }

    E getParam();
}
