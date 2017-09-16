package com.ahmed.springsluethawstraceids;

import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanTextMap;
import org.springframework.cloud.sleuth.instrument.web.HttpSpanExtractor;
import org.springframework.cloud.sleuth.util.TextMapUtil;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import static com.ahmed.springsluethawstraceids.SpringSluethAwsTraceIdsApplication.AMAZON_TRACE_ID;

/**
 * Created by Ahmed
 */
public class AwsTraceHttpSpanExtractor implements HttpSpanExtractor {

    @Override
    public Span joinTrace(SpanTextMap entries) {
        Map<String, String> carrier = TextMapUtil.asMap(entries);

        // Amazon Trace ID is of the format
        // X-Amzn-Trace-Id: Self=1-67891234-12456789abcdef012345678;Root=1-67891233-abcdef012345678912345678;CalledFrom=app

        String traceIdHeader = carrier.get(AMAZON_TRACE_ID);

        if (traceIdHeader != null && !traceIdHeader.isEmpty()) {
            String[] traceIds = traceIdHeader.split(";");
            Optional<String> rootIdOptional = Stream.of(traceIds).filter(t -> t.startsWith("Root")).findFirst();

            if (rootIdOptional.isPresent()) {
                String rootTrace = rootIdOptional.get().substring(5); // len("Root=") = 5
                String[] rootTraceParts = rootTrace.split("-");


                return Span.builder()
                        .traceId(Span.hexToId(rootTraceParts[2]))
                        .spanId((new Random()).nextLong())
                        .baggage(AMAZON_TRACE_ID, rootTrace)
                        .build();
            }

        } else if (carrier.get(Span.TRACE_ID_NAME) != null) {
            return Span.builder()
                    .traceId(Span.hexToId(carrier.get(Span.TRACE_ID_NAME)))
                    .spanId(Span.hexToId(carrier.get(Span.SPAN_ID_NAME)))
                    .build();
        }

        return null;
    }
}
