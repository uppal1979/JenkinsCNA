/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wipro.catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author JOSEPHE
 */

@RestController
public class ProductController {
    static AtomicInteger ID = new AtomicInteger();
    List<Product> products = new ArrayList<>();
    
    @Autowired
    RestTemplate restTemplate;
    
    @GetMapping("/products")
    public List<Product> getProducts() {
        return products;
    }

    @GetMapping("/products/{id}")
//    public ResponseEntity<Product> getProduct(@RequestParam(value = "Product unique identifier",required=true) @PathVariable("id") Integer id) {
    public ResponseEntity<ProductDetail> getProduct(@PathVariable("id") Integer id) {
//    public ProductDetail getProduct(@PathVariable("id") Integer id) {
          for(Product p : products) {
              if(p.getId().equals(id)) {
                  ProductDetail pd = new ProductDetail(p);
                  pd.setSkus(getSkus(p.getId()));
                  return new ResponseEntity<ProductDetail>(pd, HttpStatus.OK);
//                  return pd;
              }
          }
          ProductDetail pd = null;
          return new ResponseEntity<ProductDetail>(pd, HttpStatus.NOT_FOUND);
//          return null;
    }
    
    private List<SKU> getSkus(Integer productId) {
        return restTemplate.getForEntity("http://inventory-service-kev/skus?productId=" + productId, List.class).getBody();
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> putProduct(@PathVariable("id") Integer id, 
            @Valid @RequestBody(required = true) Product modifiedProduct) {
          Product foundProduct = null;
          for(Product a : products) {
              if(a.getId() == id) {
                  int itemIndex = products.indexOf(a);
                  System.out.println(itemIndex);
                  modifiedProduct.setId(a.getId());
                  products.set(itemIndex, modifiedProduct);
                  return new ResponseEntity<Product>(modifiedProduct, HttpStatus.OK);
              }
          }
          return new ResponseEntity<Product>(foundProduct, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") Integer id) {
          Product foundProduct = null;
          for(Product a : products) {
              if(a.getId() == id) {
                  foundProduct = a;
                  products.remove(a); 
                  return new ResponseEntity<Product>(foundProduct, HttpStatus.OK);
              }
          }
          return new ResponseEntity<Product>(foundProduct, HttpStatus.NOT_FOUND);
    }
    
    @PostMapping("/products")
//    public ResponseEntity<String> addProduct(@Valid @RequestBody(required = true) Product newProduct, HttpServletResponse response) {
//    public ResponseEntity<Product> addProduct(@Valid @RequestBody(required = true) Product newProduct) {
    public ResponseEntity<?> addProduct(@Valid @RequestBody(required = true) Product newProduct) {
        try {

            if ((newProduct.getName() == null) || newProduct.getCatalog() == null) {
                System.out.println("Name or Catalog is null");
                return new ResponseEntity<>("catalog mandatory", HttpStatus.BAD_REQUEST);
            } else {
                newProduct.setId(ID.incrementAndGet());
                products.add(newProduct);
                return new ResponseEntity<Product>(newProduct, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            System.out.println("Caught error...");
//            return new ResponseEntity<>("payload empty", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    
}
