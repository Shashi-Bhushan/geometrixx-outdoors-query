package com.github.aem.service.impl;

import com.github.aem.service.ProductSearchService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

/**
 * Created by shabhushan on 2/23/2017.
 */
@Component(label = "Product Search Service", description = "Utility Service for Query Search")
@Service(ProductSearchService.class)
public class ProductSearchServiceImpl implements ProductSearchService{
  public String getResults(String param) {
    return null;
  }
}
