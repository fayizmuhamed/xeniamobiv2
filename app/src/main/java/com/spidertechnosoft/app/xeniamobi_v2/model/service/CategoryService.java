package com.spidertechnosoft.app.xeniamobi_v2.model.service;

import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryService implements IEntityService<Category> {

    @Override
    public Long save(Category category) {

        // check if the category is null
        if ( category == null ) return null;

        Category existingCategory=findByUid(category.getUid());

        if(existingCategory==null){

            return category.save();

        }else {

            category.setId(existingCategory.getId());

            return category.save();
        }

    }

    @Override
    public Category findById(Long id) {

        // Create the Category object
        Category category = new Category();

        // the user by id
        return  category.findById(Category.class, id);
    }

    @Override
    public List<Category> findByQuery(String whereClause, String[] whereArgs, String groupBy, String orderBy, String limit) {

        // Create the Category object
        Category category = new Category();

        // Return the list
        return category.find(Category.class, whereClause, whereArgs, groupBy, orderBy, limit);
    }

    @Override
    public boolean delete(Long id) {

        // Create the Category object
        Category category = new Category();

        // Return the response after delete
        return category.delete();
    }

    @Override
    public boolean delete(Category category) {

        // Return the response after delete
        return category.delete();
    }

    public Category findByUid(String uid) {

        // Create the Category object
        List<Category> categories = new ArrayList<>();

        Category category=null;

        // the user by username
        categories=  category.find(Category.class, "uid = ?", uid);

        return categories.size()>0?categories.get(0):null;

    }

    public  List<Category> findActiveCategories() {
        Boolean isActive=false;

        Category category=new Category();

        // the user by username
        return category.find(Category.class, "active = ?",new String[]{"1"},null, "name ASC",null);


    }

}
