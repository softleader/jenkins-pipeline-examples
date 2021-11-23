package com.transglobe.framework.web.filter;

import static java.util.Optional.ofNullable;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.filter.OncePerRequestFilter;

import com.transglobe.framework.web.autoconfigure.TransGlobeWebProperties.WebTraceProperties.ResponseHeaderName;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Add TraceContext information to the HTTP server response
 *
 * @author Matt Ho
 */
@RequiredArgsConstructor
public class TraceContextInResponseFilter extends OncePerRequestFilter {

  @NonNull
  final Tracer tracer;
  @NonNull
  final ResponseHeaderName headerName;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    ofNullable(tracer.currentSpan())
        .map(Span::context)
        .ifPresent(context -> inject(context, response));
    chain.doFilter(request, response);
  }

  void inject(TraceContext context, HttpServletResponse response) {
    response.addHeader(headerName.getTraceId(), context.traceId());
    response.addHeader(headerName.getSpanId(), context.spanId());
    response.addHeader(headerName.getParentSpanId(), context.parentId());
    response.addHeader(headerName.getSampled(), Objects.toString(context.sampled()));
  }
}
