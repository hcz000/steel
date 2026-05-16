package com.gv.steel.common.easyexcel.listener;

import com.alibaba.excel.event.AnalysisEventListener;
import com.gv.steel.common.easyexcel.model.ErrorMessage;

import java.util.List;

public abstract class ListAnalysisEventListener<T> extends AnalysisEventListener<T> {

    public abstract List<T> getList();

    public abstract List<ErrorMessage> getErrorMessage();

}
