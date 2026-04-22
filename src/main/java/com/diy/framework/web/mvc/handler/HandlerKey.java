package com.diy.framework.web.mvc.handler;

import java.util.Objects;

public class HandlerKey {
    private final String url;
    private final String httpMethod;

    public HandlerKey(String url, String httpMethod) {
        this.url = url;
        this.httpMethod = httpMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandlerKey that = (HandlerKey) o;
        return url.equals(that.url) && httpMethod.equals(that.httpMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, httpMethod);
    }
}
