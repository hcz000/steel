package com.gv.steel.common.easyexcel.handler;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.gv.steel.common.core.exception.BaseException;
import com.gv.steel.common.easyexcel.annotation.EasyExcelExport;
import com.gv.steel.common.easyexcel.annotation.Sheet;
import com.gv.steel.common.easyexcel.properties.EasyExcelProperties;
import com.gv.steel.common.easyexcel.support.WriterBuilderAbstract;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 多 sheet 导出
 */
@Slf4j
public class MultipleSheetWriteHandler extends AbstractSheetWriteHandler {

    public MultipleSheetWriteHandler(EasyExcelProperties easyExcelProperties, ObjectProvider<List<Converter<?>>> converterProvider, WriterBuilderAbstract writerBuilderAbstract) {
        super(easyExcelProperties, converterProvider, writerBuilderAbstract);
    }

    @Override
    public boolean support(Object obj) {
        if (!(obj instanceof List)) {
            log.error("@EasyExcelExport 返回值必须为List类型");
            throw new BaseException();
        } else {
            List<?> objList = (List<?>) obj;
            return !objList.isEmpty() && objList.get(0) instanceof List;
        }
    }

    @Override
    public void write(Object obj, HttpServletResponse response, EasyExcelExport easyExcelExport) {
        List<?> objList = (List<?>) obj;
        ExcelWriter excelWriter = this.getExcelWriter(response, easyExcelExport);
        Sheet[] sheets = easyExcelExport.sheets();

        for (int i = 0; i < sheets.length; ++i) {
            List<?> eleList = (List<?>) objList.get(i);
            if (CollUtil.isEmpty(eleList)) continue;
            Class<?> dataClazz = eleList.get(0).getClass();
            WriteSheet sheet = this.sheet(sheets[i], dataClazz, easyExcelExport.template(), easyExcelExport.headGenerator());
            excelWriter.write(eleList, sheet);
        }

        excelWriter.finish();
    }
}
