package org.rtsl.openmetrics.utils.servlet;

import org.rtsl.openmetrics.utils.Metric;
import org.rtsl.openmetrics.utils.MetricProvider;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public final class OpenmetricsProducerServlet extends HttpServlet {

    private static MetricProvider<Number> metricProvider;

    public void setMetricProvider(MetricProvider metricProvider) {
        OpenmetricsProducerServlet.metricProvider = metricProvider;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        try {
            for (Metric currentMetric : metricProvider.getMetrics()) {
                resp.getOutputStream().print(currentMetric.getAsString());
                resp.getOutputStream().print("\n");
            }
        } catch (Exception ex) {
            // TODO: manage error
        }
    }

}
