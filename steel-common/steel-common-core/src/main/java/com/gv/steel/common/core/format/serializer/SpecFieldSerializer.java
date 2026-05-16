package com.gv.steel.common.core.format.serializer;

import cn.hutool.core.util.ObjUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.gv.steel.common.core.entity.Spec;
import com.gv.steel.common.core.format.SpecFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;

@RequiredArgsConstructor
public class SpecFieldSerializer extends JsonSerializer<Spec> implements ContextualSerializer {
    private final SpecFormatter specFormatter;

    @Override
    public void serialize(Spec s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (ObjUtil.isNull(s)) {
            jsonGenerator.writeNull();
            return;
        }
        jsonGenerator.writeObject(specFormatter.print(s, LocaleContextHolder.getLocale()));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) {
        return new SpecFieldSerializer(new SpecFormatter());
    }
}
