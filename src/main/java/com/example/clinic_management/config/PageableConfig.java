package com.example.clinic_management.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PageableConfig implements WebMvcConfigurer {
    @Value("${paging.default-page-size:10}")
    private int defaultPageSize;

    @Value("${paging.default-page-number:0}")
    private int defaultPageNumber;

    @Value("${paging.default-sort-by:id}")
    private String sortBy;

    @Value("${paging.default-sort-dir:desc}")
    private String sortDir;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setPageParameterName("page");
        resolver.setSizeParameterName("size");
        resolver.setOneIndexedParameters(false);

//        resolver.setFallbackPageable(PageRequest.of(defaultPageNumber, defaultPageSize));
        Sort defaultSort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        resolver.setFallbackPageable(PageRequest.of(defaultPageNumber, defaultPageSize, defaultSort));
        argumentResolvers.add(resolver);
    }
}
