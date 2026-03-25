package com.diy.framework.web.mvc.view;

public class ViewResolver {

    public View resolveView(String viewName) {
        return new JspView("/" + viewName + ".jsp");
    }
}
