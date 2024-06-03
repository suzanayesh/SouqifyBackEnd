package com.code.auth.service;

import com.code.auth.entity.ProductPic;
import com.code.auth.repo.ProductPicRepositpry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductPicService {

    @Autowired
    private ProductPicRepositpry productPicRepositpry;


    public boolean addProductOic(Long productId, String url){
        ProductPic productPic = new ProductPic(productId, url);
        productPicRepositpry.save(productPic);
        return true;
    }
}
