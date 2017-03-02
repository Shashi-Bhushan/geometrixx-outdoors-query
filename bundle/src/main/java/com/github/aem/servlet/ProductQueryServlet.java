package com.github.aem.servlet;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import javax.jcr.Session;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shabhushan on 2/23/2017.
 */
@Component(label="Product Query Servlet", description = "Returns Query Results in json format")
@Properties({
  @Property(name="sling.servlet.paths", value="/bin/product-search"),
  @Property(name="sling.servlet.methods", value="get"),
  @Property(name="sling.servlet.extensions", value="json")
})
public class ProductQueryServlet extends SlingSafeMethodsServlet {

  @Reference
  private QueryBuilder queryBuilder;

  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
    super.doGet(request, response);
    response.setHeader("Content-Type", "application/json");

  }

  private void getResults(SlingHttpServletRequest request) {
    Map<String, String> searchCriteria = new HashMap<String, String>();

    searchCriteria.put("path", "/etc/commerce/products");


    PredicateGroup predicateGroup = PredicateGroup.create(searchCriteria);
    Query query = queryBuilder.createQuery(predicateGroup, request.getResourceResolver().adaptTo(Session.class));

    SearchResult searchResult = query.getResult();

    for(Hit hit: searchResult.getHits()) {

    }
  }
}
