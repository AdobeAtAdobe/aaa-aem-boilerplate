package com.adobe.aaa.cec.thing.servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.SlingServletException;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.commons.json.io.JSONWriter;
import org.apache.sling.api.resource.Resource;
import com.day.cq.wcm.api.Page;

import javax.servlet.ServletException;
import java.io.IOException;

@Property(name = "service.description", value = "Thing JSON Servlet")
@Component(metatype = false)
@SlingServlet(
        methods = {"GET"},
        resourceTypes = {"aaa/cec/pages/thing"},
        selectors = {"thing"}, // Ignored if paths is set
        extensions = {"json"},
        generateComponent = false
)
public class ThingJsonServlet extends SlingAllMethodsServlet{
    private static final Logger log = LoggerFactory.getLogger(ThingJsonServlet.class);

    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");

            Page currentPage = request.getResource().adaptTo(Page.class);
            JSONWriter w = new JSONWriter(response.getWriter());

            w.object();
            w.key("name").value(currentPage.getTitle());

            w.endObject();
        } catch (Exception ex) {
            log.error("Request failed", ex);
            throw new ServletException(ex);
        }
    }
}
