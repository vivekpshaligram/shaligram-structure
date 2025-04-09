package com.codestracture.utils;

public interface HtmlTaskCallback extends BaseMvpView {
    void onReceiveHtml(String href, String html);
}
