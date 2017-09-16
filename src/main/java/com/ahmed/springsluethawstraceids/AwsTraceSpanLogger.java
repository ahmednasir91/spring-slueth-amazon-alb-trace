package com.ahmed.springsluethawstraceids;

import org.slf4j.MDC;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.log.Slf4jSpanLogger;

import static com.ahmed.springsluethawstraceids.SpringSluethAwsTraceIdsApplication.AMAZON_TRACE_ID;

/**
 * Created by Ahmed
 */
public class AwsTraceSpanLogger extends Slf4jSpanLogger {

    AwsTraceSpanLogger(String nameSkipPattern) {
        super(nameSkipPattern);
    }

    @Override
    public void logStartedSpan(Span parent, Span span) {
        super.logStartedSpan(parent, span);
        putAwsTraceFromBaggage(parent, span);
    }

    @Override
    public void logContinuedSpan(Span span) {
        super.logContinuedSpan(span);
        putAwsTraceFromBaggage(null, span);
    }

    @Override
    public void logStoppedSpan(Span parent, Span span) {
        super.logStoppedSpan(parent, span);
        putAwsTraceFromBaggage(parent, span);
    }

    private void putAwsTraceFromBaggage(Span parent, Span span) {
        if (parent != null && parent.getBaggageItem(AMAZON_TRACE_ID) != null) {
            MDC.put(Span.TRACE_ID_NAME, parent.getBaggageItem(AMAZON_TRACE_ID));
        }

        if (span != null && span.getBaggageItem(AMAZON_TRACE_ID) != null) {
            MDC.put(Span.TRACE_ID_NAME, span.getBaggageItem(AMAZON_TRACE_ID));
        }
    }
}
