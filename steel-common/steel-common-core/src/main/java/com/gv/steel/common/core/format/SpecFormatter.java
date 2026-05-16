package com.gv.steel.common.core.format;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.gv.steel.common.core.entity.Spec;
import org.jetbrains.annotations.NotNull;
import org.springframework.format.Formatter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpecFormatter implements Formatter<Spec> {
    public final static Pattern SPEC_PATTERN = Pattern.compile("([0-9]+(\\.[0-9]{0,3})?)(\\*)([0-9]+(\\.[0-9]{0,2})?)((\\*)(([0-9]+(\\.[0-9]{0,2})?)|([Cc])))?");

    private final static String STEEL_COIL_UNIT = "C";

    private final static BigDecimal STEEL_COIL_LENGTH = new BigDecimal("0");

    private final static String SPEC_SEPARATOR = "*";

    @NotNull
    @Override
    public Spec parse(@NotNull String text, @NotNull Locale locale) throws ParseException {
        if (StrUtil.isBlank(text)) {
            return null;
        }
        Matcher matcher = SPEC_PATTERN.matcher(text);
        if (!matcher.matches()) {
            throw new ParseException("Please use the specified format for the specification fields, format: [xxx*xxx*(xxx|C|c)]", 0);
        }

        String ply = matcher.replaceAll("$1");

        String width = matcher.replaceAll("$4");

        String length = matcher.replaceAll("$8");

        Spec spec = new Spec();
        spec.setPly(new BigDecimal(ply));
        spec.setWidth(new BigDecimal(width));
        spec.setLength(StrUtil.isBlank(length) || STEEL_COIL_UNIT.equalsIgnoreCase(length) ? STEEL_COIL_LENGTH : new BigDecimal(length));
        return spec;
    }

    @NotNull
    @Override
    public String print(@NotNull Spec object, @NotNull Locale locale) {
        if (ObjUtil.isNull(object)) {
            return "";
        }
        BigDecimal newPly = stripTrailingZeros(object.getPly());
        return StrUtil.join(SPEC_SEPARATOR,
                newPly.toPlainString(),
                object.getWidth().stripTrailingZeros().toPlainString(),
                STEEL_COIL_LENGTH.compareTo(object.getLength()) == 0
                        ? STEEL_COIL_UNIT
                        : object.getLength().stripTrailingZeros().toPlainString()
        );
    }

    public static BigDecimal stripTrailingZeros(BigDecimal ply) {
        BigDecimal newPly = ply.stripTrailingZeros();
        // 如果精度为0，则补上1
        if (newPly.scale() == 0) {
            newPly = newPly.setScale(1, RoundingMode.UNNECESSARY);
        }
        return newPly;
    }
}
