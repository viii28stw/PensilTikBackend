package com.viii28stw.pensiltikbackend.util;

import org.springframework.web.client.RestTemplate;

public class RestTemplateFactory {

    private static final RestTemplate REST_TEMPLATE;

    static {
        REST_TEMPLATE = new RestTemplate();
    }

    private RestTemplateFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static RestTemplate getRestTemplate() {
        return REST_TEMPLATE;
    }

}
