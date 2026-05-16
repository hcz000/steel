package com.gv.steel.common.easyexcel.support;

import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.gv.steel.common.easyexcel.annotation.EasyExcelExport;
import com.gv.steel.common.easyexcel.model.HeadGenerator;

import javax.servlet.http.HttpServletResponse;

public class DefaultWriterBuilderAbstractProvider implements WriterBuilderAbstract {
    @Override
    public ExcelWriterBuilder excelWriterBuilder(ExcelWriterBuilder writerBuilder, HttpServletResponse response, EasyExcelExport easyExcelExport, String templatePath) {
        return writerBuilder;
    }

    @Override
    public ExcelWriterSheetBuilder excelWriterSheetBuilder(ExcelWriterSheetBuilder writerSheetBuilder, Integer sheetNo, String sheetName, Class<?> dataClazz, String template, Class<? extends HeadGenerator> headEnhancerClazz) {
        return writerSheetBuilder;
    }
}
