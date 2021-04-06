package cn.itechyou.cms.security.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.itechyou.cms.utils.EscapeUtil;

/**
 * XSS过滤处理
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    XssHttpServletRequestWrapper(HttpServletRequest request){
        super(request);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            int length = values.length;
            String[] escapseValues = new String[length];
            for (int i = 0; i < length; i++) {
                // 防xss攻击和过滤前后空格
                escapseValues[i] = EscapeUtil.escape(values[i]).trim();
            }
            return escapseValues;
        }
        return super.getParameterValues(name);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        // 非json类型，直接返回
        if (!isJsonRequest()) {
            return super.getInputStream();
        }

        // 为空，直接返回
        String json = IoUtil.read(super.getInputStream(), "utf-8");
        if (StrUtil.isEmpty(json)) {
            return super.getInputStream();
        }

        // xss过滤
        json = EscapeUtil.escape(json).trim();
        final ByteArrayInputStream bis = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        return new ServletInputStream() {
            @Override
            public boolean isFinished(){
                return true;
            }

            @Override
            public boolean isReady(){
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read(){
                return bis.read();
            }
        };
    }

    /**
     * 是否是Json请求
     */
    private boolean isJsonRequest() {
        String header = super.getHeader(HttpHeaders.CONTENT_TYPE);
        return MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(header)
                || MediaType.APPLICATION_JSON_UTF8_VALUE.equalsIgnoreCase(header);
    }
}
