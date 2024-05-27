package com.kfc.app.controller;

import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.UserDto;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
public abstract class AbstractController {
    protected <T> Map<String, Object> prepareResponse(ResultPageWrapper<T> resultPageWrapper) {
        Map<String, Object> response = new HashMap<>();
        response.put(getResource(), resultPageWrapper.getPagesResult());
        response.put("currentPage", resultPageWrapper.getCurrentPage());
        response.put("totalItems", resultPageWrapper.getTotalItems());
        response.put("totalPages", resultPageWrapper.getTotalPages());
        return response;
    }
    protected abstract String getResource();
}
