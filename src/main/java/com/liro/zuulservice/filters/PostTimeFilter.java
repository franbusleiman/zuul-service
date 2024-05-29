package com.liro.zuulservice.filters;

import com.google.common.io.CharStreams;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;


@Component
public class PostTimeFilter extends ZuulFilter {

   private static Logger log =  LoggerFactory.getLogger(PostTimeFilter.class);

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext ctx = RequestContext.getCurrentContext();

        try (final InputStream responseDataStream = ctx.getResponseDataStream()) {
            final String responseData = CharStreams.toString(new InputStreamReader(responseDataStream, StandardCharsets.UTF_8));
            System.out.println(responseData);
            ctx.setResponseBody(responseData);
        } catch (IOException e) {
            log.warn("Error reading body",e);
        }
        return null;
    }
}
