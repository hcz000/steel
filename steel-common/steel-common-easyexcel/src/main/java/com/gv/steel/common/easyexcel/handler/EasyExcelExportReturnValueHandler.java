package com.gv.steel.common.easyexcel.handler;

import com.gv.steel.common.easyexcel.annotation.EasyExcelExport;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequiredArgsConstructor
public class EasyExcelExportReturnValueHandler implements HandlerMethodReturnValueHandler {
    private final List<SheetWriteHandler> sheetWriteHandlerList;

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getMethodAnnotation(EasyExcelExport.class) != null;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        Assert.state(response != null, "No HttpServletResponse");
        EasyExcelExport easyExcelExport = returnType.getMethodAnnotation(EasyExcelExport.class);
        Assert.state(easyExcelExport != null, "No @EasyExcelExport");
        mavContainer.setRequestHandled(true);
        this.sheetWriteHandlerList.stream().filter((handler) -> handler.support(returnValue)).findFirst().ifPresent((handler) -> {
            handler.export(returnValue, response, easyExcelExport);
        });
    }
}
