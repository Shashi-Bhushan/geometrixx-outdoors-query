package com.github.aem.servlet;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.github.aem.constant.Constants;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shabhushan on 2/23/2017.
 */
@Component(label="Product Query Servlet", description = "Returns Query Results in json format")
@Service(Servlet.class)
@Properties({
  @Property(name="sling.servlet.paths", value="/bin/product-search"),
  @Property(name="sling.servlet.methods", value="GET"),
  @Property(name="sling.servlet.extensions", value="json")
})
public class ProductQueryServlet extends SlingSafeMethodsServlet {

  @Reference
  private QueryBuilder queryBuilder;

  private static final String[] MONTHS = {
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
  };

  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
//    super.doGet(request, response);
    response.setHeader("Content-Type", "application/json");

    JSONArray array = null;

    Map<String, String> searchCriteria = getSearchMap(request);
    try {
      array = getResults(request, searchCriteria);
    } catch (RepositoryException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }

    response.getWriter().write(array.toString());

  }

  private Map<String, String> getSearchMap(SlingHttpServletRequest request) {
    Map<String, String> searchCriteria = new HashMap<String, String>();

    searchCriteria.put(Constants.PATH, "/etc/commerce/products/geometrixx-outdoors");
    searchCriteria.put(Constants.TYPE, "nt:unstructured");

    searchCriteria.put("1_property", "sling:resourceType");
    searchCriteria.put("1_property.value", "commerce/components/product");

    searchCriteria.put("2_property", "jcr:title");
    searchCriteria.put("2_property.value", request.getParameter("searchTerm"));

    searchCriteria.put("3_property", "cq:commerceType");
    searchCriteria.put("3_property.1_value", "product");
    searchCriteria.put("3_property.2_value", "variant");

    return searchCriteria;
  }

  private JSONArray getResults(SlingHttpServletRequest request, Map<String, String> searchCriteria) throws RepositoryException, JSONException {
    JSONArray searchResults = new JSONArray();

    PredicateGroup predicateGroup = PredicateGroup.create(searchCriteria);
    Query query = queryBuilder.createQuery(predicateGroup, request.getResourceResolver().adaptTo(Session.class));

    SearchResult searchResult = query.getResult();

    for(Hit hit: searchResult.getHits()) {
      JSONObject result = new JSONObject();
      result.put(Constants.URL, hit.getPath());

      ValueMap properties = hit.getProperties();
      result.put(Constants.TITLE, String.valueOf(properties.get("jcr:title")));
      Calendar calendar = (GregorianCalendar)properties.get("jcr:lastModified");
      result.put(Constants.DATE, String.valueOf(MONTHS[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR) + "," + calendar.get(Calendar.DATE)));

      searchResults.put(result);
    }

    return searchResults;
  }
}
