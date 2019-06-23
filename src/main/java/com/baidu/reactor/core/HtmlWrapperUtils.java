package com.baidu.reactor.core;

import java.io.UnsupportedEncodingException;

public class HtmlWrapperUtils {
    public static byte[] wrapperHttpHtmlRepoonse(byte[] bytes) throws UnsupportedEncodingException {
        String body = new String(bytes, "UTF-8");
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("HTTP/1.1 ").append(200).append(" ").append("OK").append("\n");
        messageBuilder.append("Content-Type: ").append("text/html;charset=UTF-8").append("\n");
        messageBuilder.append("Content-Length: ").append(body.length() + 26).append("\n");
        messageBuilder.append("\n");
        messageBuilder.append("<html><body>");
        messageBuilder.append(body);
        messageBuilder.append("</body></html>");
        String message = messageBuilder.toString();
        return message.getBytes();
    }
}
