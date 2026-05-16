package com.gv.steel.common.easyexcel.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateTime to Excel date format String data
 */
public enum LocalDateTimeStringConvert implements Converter<LocalDateTime> {
    INSTANCE;

    LocalDateTimeStringConvert() {
    }

    @Override
    public Class<?> supportJavaTypeKey() {
        return LocalDateTime.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDateTime convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String pattern;
        if (contentProperty != null && contentProperty.getDateTimeFormatProperty() != null) {
            pattern = contentProperty.getDateTimeFormatProperty().getFormat();
        } else {
            pattern = switchDateFormat(cellData.getStringValue());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(cellData.getStringValue(), formatter);
    }

    @Override
    public WriteCellData<?> convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        DateTimeFormatter formatter;
        if (contentProperty != null && contentProperty.getDateTimeFormatProperty() != null) {
            formatter = DateTimeFormatter.ofPattern(contentProperty.getDateTimeFormatProperty().getFormat());
        } else {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        }

        return new WriteCellData<>(value.format(formatter));
    }

    private static String switchDateFormat(String dateString) {
        int length = dateString.length();
        switch (length) {
            case 10:
                return "yyyy-MM-dd";
            case 11:
            case 12:
            case 13:
            case 15:
            case 16:
            case 18:
            default:
                throw new IllegalArgumentException("can not find date format for：" + dateString);
            case 14:
                return "yyyyMMddHHmmss";
            case 17:
                return "yyyyMMdd HH:mm:ss";
            case 19:
                return dateString.contains("-") ? "yyyy-MM-dd HH:mm:ss" : "yyyy/MM/dd HH:mm:ss";
        }
    }
}
