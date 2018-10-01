package com.spidertechnosoft.app.xeniamobi_v2.model.service;

import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductService implements IEntityService<Product> {

    @Override
    public Long save(Product product) {

        // check if the product is null
        if ( product == null ) return null;

        Product existingProduct=findByUid(product.getUid());

        if(existingProduct==null){

            return product.save();

        }else {

            product.setId(existingProduct.getId());

            return product.save();
        }

    }

    @Override
    public Product findById(Long id) {

        // Create the Product object
        Product product = new Product();

        // the user by id
        return  product.findById(Product.class, id);
    }

    @Override
    public List<Product> findByQuery(String whereClause, String[] whereArgs, String groupBy, String orderBy, String limit) {

        // Create the Product object
        Product product = new Product();

        // Return the list
        return product.find(Product.class, whereClause, whereArgs, groupBy, orderBy, limit);
    }

    @Override
    public boolean delete(Long id) {

        // Create the Product object
        Product product = new Product();

        // Return the response after delete
        return product.delete();
    }

    @Override
    public boolean delete(Product product) {

        // Return the response after delete
        return product.delete();
    }

    public Product findByUid(String uid) {

        // Create the Category object
        List<Product> products = new ArrayList<>();

        Product product=new Product();

        // the user by username
        products=  product.find(Product.class, "uid = ?", uid);

        return products.size()>0?products.get(0):null;

    }

    public  List<Product> findActiveProducts() {

        // Create the Category object
        List<Product> products = new ArrayList<>();

        Product product=new Product();

        // the user by username
        return Product.find(Product.class, "active = ?",new String[]{"1"},null, "name ASC",null);


    }

    public  Boolean isDeleteEnabled(String categoryUid) {

        // the user by username
        List<Product> products = Product.find(Product.class, "category_Uid = ?",new String[]{categoryUid},null, "name ASC",null);

        return (products==null||products.isEmpty())?true:false;
    }

}
