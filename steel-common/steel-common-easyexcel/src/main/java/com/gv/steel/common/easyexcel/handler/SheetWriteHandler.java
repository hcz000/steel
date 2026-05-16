package com.gv.steel.common.easyexcel.handler;

import com.gv.steel.common.easyexcel.annotation.EasyExcelExport;

import javax.servlet.http.HttpServletResponse;

public interface SheetWriteHandler {
    boolean support(Object obj);

    void check(EasyExcelExport easyExcelExport);

    void export(Object o, HttpServletResponse response, EasyExcelExport easyExcelExport);

    void write(Object o, HttpServletResponse response, EasyExcelExport easyExcelExport);
}
