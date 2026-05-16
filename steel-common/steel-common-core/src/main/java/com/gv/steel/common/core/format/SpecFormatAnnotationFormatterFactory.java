package com.gv.steel.common.core.format;

import com.google.common.collect.Sets;
import com.gv.steel.common.core.entity.Spec;
import com.gv.steel.common.core.format.annotation.SpecFormat;
import org.jetbrains.annotations.NotNull;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.util.Set;

public class SpecFormatAnnotationFormatterFactory implements AnnotationFormatterFactory<SpecFormat> {

    @NotNull
    @Override
    public Set<Class<?>> getFieldTypes() {
        return Sets.newHashSet(Spec.class);
    }

    @NotNull
    @Override
    public Printer<?> getPrinter(@NotNull SpecFormat annotation, @NotNull Class<?> fieldType) {
        return formatter();
    }

    @NotNull
    @Override
    public Parser<?> getParser(@NotNull SpecFormat annotation, @NotNull Class<?> fieldType) {
        return formatter();
    }

    private Formatter<Spec> formatter() {
        return new SpecFormatter();
    }
}
