package com.gv.steel.common.core.format;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.gv.steel.common.core.format.annotation.SpecFormat;
import com.gv.steel.common.core.format.deserializer.SpecFieldDeserializer;
import com.gv.steel.common.core.format.serializer.SpecFieldSerializer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SpecFieldAnnotationIntrospector extends NopAnnotationIntrospector {
    private static final long serialVersionUID = -4823691959595162947L;

    private final SpecFormatter specFormatter;

    @Override
    public Object findSerializer(Annotated am) {
        SpecFormat annotation = am.getAnnotation(SpecFormat.class);
        if (annotation != null) {
            return new SpecFieldSerializer(specFormatter);
        }
        return super.findSerializer(am);
    }

    @Override
    public Object findDeserializer(Annotated am) {
        SpecFormat annotation = am.getAnnotation(SpecFormat.class);
        if (annotation != null) {
            return new SpecFieldDeserializer(specFormatter);
        }
        return super.findDeserializer(am);
    }
}
