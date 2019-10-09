/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wipro.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author JOSEPHE
 */
@RestController
public class SKUController {
    static AtomicInteger ID = new AtomicInteger();
    List<SKU> skus = new ArrayList<>();
    
    @GetMapping("/skus")
    public List<SKU> getSKUs(@RequestParam(name="productId", required=false) Integer productId) {
        if (productId != null) {
            return skus
                    .stream()
                    .filter(s -> productId.equals(s.getProductId()))
                    .collect(Collectors.toList());                    
        }
        return skus;
    }

    @GetMapping("/skus/{id}")
//    public ResponseEntity<SKU> getSKU(@RequestParam(value = "SKU unique identifier",required=true) @PathVariable("id") Integer id) {
    public ResponseEntity<SKU> getSKU(@PathVariable("id") Integer id) {
          SKU foundSku = null;
          for(SKU a : skus) {
              if(a.getId() == id) {
                  foundSku = a;
                  return new ResponseEntity<SKU>(foundSku, HttpStatus.OK);
              }
          }
          return new ResponseEntity<SKU>(foundSku, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/skus/{id}")
    public ResponseEntity<SKU> putSKU(@PathVariable("id") Integer id, 
            @Valid @RequestBody(required = true) SKU modifiedSku) {
          SKU foundSku = null;
          for(SKU a : skus) {
              if(a.getId() == id) {
                  int itemIndex = skus.indexOf(a);
                  System.out.println(itemIndex);
                  modifiedSku.setId(a.getId());
                  skus.set(itemIndex, modifiedSku);
                  return new ResponseEntity<SKU>(modifiedSku, HttpStatus.OK);
              }
          }
          return new ResponseEntity<SKU>(foundSku, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/skus/{id}")
    public ResponseEntity<SKU> deleteSKU(@PathVariable("id") Integer id) {
          SKU foundSku = null;
          for(SKU a : skus) {
              if(a.getId() == id) {
                  foundSku = a;
                  skus.remove(a); 
                  return new ResponseEntity<SKU>(foundSku, HttpStatus.OK);
              }
          }
          return new ResponseEntity<SKU>(foundSku, HttpStatus.NOT_FOUND);
    }
    
    @PostMapping("/skus")
//    public ResponseEntity<String> addSKU(@Valid @RequestBody(required = true) SKU newSku, HttpServletResponse response) {
//    public ResponseEntity<SKU> addSKU(@Valid @RequestBody(required = true) SKU newSku) {
    public ResponseEntity<?> addSKU(@Valid @RequestBody(required = true) SKU newSku) {
        try {

            if (newSku.getProductId() == null) {
                System.out.println("ProductId is null");
                return new ResponseEntity<>("ProductId is null", HttpStatus.BAD_REQUEST);
            } else {
                newSku.setId(ID.incrementAndGet());
                skus.add(newSku);
                return new ResponseEntity<SKU>(newSku, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            System.out.println("Caught error...");
//            return new ResponseEntity<>("payload empty", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    
}
