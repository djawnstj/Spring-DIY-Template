package com.diy.framework.web.method.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class HandlerMethodArgumentResolverComposite implements HandlerMethodArgumentResolver {

    private final List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();

    public HandlerMethodArgumentResolverComposite addResolvers(final List<? extends HandlerMethodArgumentResolver> resolvers) {
        if (resolvers != null) argumentResolvers.addAll(resolvers);
        return this;
    }

    @Override
    public boolean supportsParameter(final Parameter parameter) {
        return selectResolver(parameter) != null;
    }

    @Override
    public Object resolveArgument(final Parameter parameter,
                                  final HttpServletRequest request,
                                  final HttpServletResponse response) throws Exception {
        final HandlerMethodArgumentResolver resolver = selectResolver(parameter);
        if (resolver == null) {
            throw new IllegalStateException("No HandlerMethodArgumentResolver for " + parameter);
        }
        return resolver.resolveArgument(parameter, request, response);
    }

    private HandlerMethodArgumentResolver selectResolver(final Parameter parameter) {
        for (final HandlerMethodArgumentResolver resolver : argumentResolvers) {
            if (resolver.supportsParameter(parameter)) return resolver;
        }
        return null;
    }
}
