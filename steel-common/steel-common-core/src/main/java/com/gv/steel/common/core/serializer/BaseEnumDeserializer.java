package com.gv.steel.common.core.serializer;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BaseEnumDeserializer extends JsonDeserializer<Enum<?>> {
    public static boolean isList(Field field) {
        boolean flag = false;
        String simpleName = field.getType().getSimpleName();
        if (simpleName.contains("List")) {
            flag = true;
        }

        return flag;
    }

    public static boolean isSet(Field field) {
        boolean flag = false;
        String simpleName = field.getType().getSimpleName();
        if (simpleName.contains("Set")) {
            flag = true;
        }

        return flag;
    }

    public static boolean isMap(Field field) {
        boolean flag = false;
        String simpleName = field.getType().getSimpleName();
        if ("Map".equals(simpleName) || "HashMap".equals(simpleName)) {
            flag = true;
        }

        return flag;
    }

    public static Class getFieldType(String parentCurrentName, Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Field[] var3 = fields;
        int var4 = fields.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Field field = var3[var5];
            field.setAccessible(true);
            String name = field.getName();
            Type genericType = field.getGenericType();
            if (parentCurrentName.equals(name)) {
                ParameterizedType parameterizedType;
                Type actualTypeArgument;
                if (!isList(field) && !isSet(field)) {
                    if (field.getType().isArray()) {
                        return field.getType().getComponentType();
                    }

                    if (!isMap(field)) {
                        return field.getType();
                    }

                    if (genericType instanceof ParameterizedType) {
                        parameterizedType = (ParameterizedType) genericType;
                        actualTypeArgument = parameterizedType.getActualTypeArguments()[1];
                        return (Class) actualTypeArgument;
                    }
                } else if (genericType instanceof ParameterizedType) {
                    parameterizedType = (ParameterizedType) genericType;
                    actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
                    return (Class) actualTypeArgument;
                }
            }
        }

        return null;
    }

    @Override
    public Enum<?> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = (JsonNode) jsonParser.getCodec().readTree(jsonParser);
        String currentName = jsonParser.currentName();
        Object currentValue = jsonParser.getCurrentValue();
        Class findPropertyType = null;
        if (StrUtil.isNotEmpty(currentName) && ObjectUtil.isNotEmpty(currentValue)) {
            findPropertyType = BeanUtils.findPropertyType(currentName, new Class[]{currentValue.getClass()});
        } else {
            String parentCurrentName = jsonParser.getParsingContext().getParent().getCurrentName();
            Object parentCurrentValue = jsonParser.getParsingContext().getParent().getCurrentValue();
            if (ObjectUtil.isNotEmpty(parentCurrentValue) && ObjectUtil.isNotEmpty(parentCurrentValue)) {
                findPropertyType = getFieldType(parentCurrentName, parentCurrentValue.getClass());
            }
        }

        Enum<?> valueOf = null;
        if (ObjectUtil.isNotEmpty(findPropertyType)) {
            JsonFormat annotation = (JsonFormat) findPropertyType.getAnnotation(JsonFormat.class);
            JsonNode name = node.get("name");
            if (!ObjectUtil.isEmpty(annotation) && annotation.shape() == JsonFormat.Shape.OBJECT && !ObjectUtil.isEmpty(name)) {
                valueOf = Enum.valueOf(findPropertyType, name.asText());
            } else {
                valueOf = Enum.valueOf(findPropertyType, node.asText());
            }
        }

        return valueOf;
    }
}
