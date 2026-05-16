package com.gv.steel.common.easyexcel.config;

import com.alibaba.excel.converters.Converter;
import com.gv.steel.common.easyexcel.handler.EasyExcelExportReturnValueHandler;
import com.gv.steel.common.easyexcel.handler.MultipleSheetWriteHandler;
import com.gv.steel.common.easyexcel.handler.SheetWriteHandler;
import com.gv.steel.common.easyexcel.handler.SingleSheetWriteHandler;
import com.gv.steel.common.easyexcel.properties.EasyExcelProperties;
import com.gv.steel.common.easyexcel.support.DefaultWriterBuilderAbstractProvider;
import com.gv.steel.common.easyexcel.support.WriterBuilderAbstract;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({EasyExcelProperties.class})
public class EasyExcelHandlerAutoConfiguration {
    private final EasyExcelProperties easyExcelProperties;
    private final ObjectProvider<List<Converter<?>>> converterProvider;

    @Bean
    @ConditionalOnMissingBean
    public MultipleSheetWriteHandler multipleSheetWriteHandler() {
        return new MultipleSheetWriteHandler(this.easyExcelProperties, this.converterProvider, this.writerBuilderAbstract());
    }

    @Bean
    @ConditionalOnMissingBean
    public SingleSheetWriteHandler singleSheetWriteHandler() {
        return new SingleSheetWriteHandler(this.easyExcelProperties, this.converterProvider, this.writerBuilderAbstract());
    }

    @Bean
    @ConditionalOnMissingBean
    public WriterBuilderAbstract writerBuilderAbstract() {
        return new DefaultWriterBuilderAbstractProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public EasyExcelExportReturnValueHandler responseExcelReturnValueHandler(List<SheetWriteHandler> sheetWriteHandlerList) {
        return new EasyExcelExportReturnValueHandler(sheetWriteHandlerList);
    }
}
