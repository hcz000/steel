package com.gv.steel.common.core.format.deserializer;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.gv.steel.common.core.constant.ErrorCodeConstants;
import com.gv.steel.common.core.entity.Spec;
import com.gv.steel.common.core.exception.BaseException;
import com.gv.steel.common.core.format.SpecFormatter;
import com.gv.steel.common.core.util.MsgUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;
import java.text.ParseException;

@RequiredArgsConstructor
public class SpecFieldDeserializer extends JsonDeserializer<Spec> implements ContextualDeserializer {
    private final SpecFormatter specFormatter;

    @Override
    public Spec deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonToken jsonToken = jsonParser.getCurrentToken();
        if (JsonToken.VALUE_STRING != jsonToken) {
            throw new BaseException(MsgUtils.getSystemMessage(ErrorCodeConstants.SPEC_FORMAT_ERROR));
        }
        // 解析字符串
        String text = jsonParser.getValueAsString();
        try {
            if (StrUtil.isBlank(text)) {
                return null;
            }
            return specFormatter.parse(text, LocaleContextHolder.getLocale());
        } catch (ParseException e) {
            throw new BaseException(MsgUtils.getSystemMessage(ErrorCodeConstants.SPEC_FORMAT_ERROR));
        }
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty) throws JsonMappingException {
        return new SpecFieldDeserializer(new SpecFormatter());
    }
}
