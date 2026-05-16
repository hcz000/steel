package com.gv.steel.common.easyexcel.resolver;

import com.alibaba.excel.EasyExcel;
import com.gv.steel.common.easyexcel.annotation.EasyExcelImport;
import com.gv.steel.common.easyexcel.convert.LocalDateStringConvert;
import com.gv.steel.common.easyexcel.convert.LocalDateTimeStringConvert;
import com.gv.steel.common.easyexcel.listener.ListAnalysisEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class RequestExcelArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(EasyExcelImport.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Class<?> parameterType = parameter.getParameterType();
        if (!parameterType.isAssignableFrom(List.class)) {
            throw new IllegalArgumentException("Excel upload request resolver error, @RequestExcel parameter is not List " + parameterType);
        } else {
            EasyExcelImport easyExcelImport = parameter.getParameterAnnotation(EasyExcelImport.class);

            assert easyExcelImport != null;

            Class<? extends ListAnalysisEventListener<?>> readListenerClass = easyExcelImport.readListener();
            ListAnalysisEventListener<?> readListener = BeanUtils.instantiateClass(readListenerClass);
            HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

            assert request != null;

            InputStream inputStream;
            if (request instanceof MultipartRequest) {
                MultipartFile file = ((MultipartRequest) request).getFile(easyExcelImport.fileName());
                Assert.notNull(file, "excel import: file can not be null!");
                inputStream = file.getInputStream();
            } else {
                inputStream = request.getInputStream();
            }

            Class<?> excelModelClass = ResolvableType.forMethodParameter(parameter).getGeneric(new int[]{0}).resolve();
            EasyExcel.read(inputStream, excelModelClass, readListener).registerConverter(LocalDateStringConvert.INSTANCE).registerConverter(LocalDateTimeStringConvert.INSTANCE).ignoreEmptyRow(easyExcelImport.ignoreEmptyRow()).sheet().doRead();
            WebDataBinder dataBinder = binderFactory.createBinder(webRequest, readListener.getErrorMessage(), "excel");
            ModelMap model = mavContainer.getModel();
            model.put(BindingResult.MODEL_KEY_PREFIX + "excel", dataBinder.getBindingResult());
            return readListener.getList();
        }
    }
}
