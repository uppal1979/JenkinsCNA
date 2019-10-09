/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wipro.catalog;

import java.util.List;

/**
 *
 * @author JOSEPHE
 */
public class ProductDetail extends Product {
    
    List<SKU> skus;

    public ProductDetail(Product p) {
        super();
        setId(p.getId());
        setName(p.getName());
        setCatalog(p.getCatalog());
    }
 
    public List<SKU> getSkus() {
        return skus;
    }

    public void setSkus(List<SKU> skus) {
        this.skus = skus;
    }
    
}
