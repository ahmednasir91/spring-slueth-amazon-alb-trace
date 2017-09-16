package com.ahmed.springsluethawstraceids;

import org.slf4j.MDC;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanTextMap;
import org.springframework.cloud.sleuth.instrument.web.HttpSpanInjector;
import org.springframework.cloud.sleuth.util.TextMapUtil;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Created by Ahmed
 */
public class AwsTraceHttpSpanInjector implements HttpSpanInjector {
    @Override
    public void inject(Span span, SpanTextMap entries) {
        Map<String, String> carrier = TextMapUtil.asMap(entries);
        this.setHeader(entries, carrier, Span.TRACE_ID_NAME, span.traceIdString());

        if (span.getBaggageItem("X-Amzn-Trace-Id") != null) {
            entries.put(Span.TRACE_ID_NAME, span.getBaggageItem("X-Amzn-Trace-Id"));
        }
    }

    private void setHeader(SpanTextMap map, Map<String, String> carrier, String name, String value) {
        if(StringUtils.hasText(value) && !carrier.containsKey(name)) {
            map.put(name, value);
        }

    }
}
