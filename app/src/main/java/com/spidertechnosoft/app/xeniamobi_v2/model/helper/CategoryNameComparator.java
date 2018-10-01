package com.spidertechnosoft.app.xeniamobi_v2.model.helper;

import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Category;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Product;

import java.util.Comparator;

/**
 * Created by sandheepgr on 25/8/16.
 */
public class CategoryNameComparator implements Comparator<Category> {


    @Override
    public int compare(Category lhs, Category rhs) {

        // Get the seatNo
        String lhsName = lhs.getName();

        // Get the rhsUid
        String rhsName = rhs.getName();


        // If the seatnos are empty, return 0
        if ( (lhsName == null || lhsName.equals("")) &&
             (rhsName == null || rhsName.equals(""))) {

            return 0;

        }


        // If the rhsUid is empty return 1
        if ( rhsName == null || rhsName.equals("")) return -1;

        // If the lhsUid is empty , return -1
        if ( lhsName == null || lhsName.equals("")) return 1;

        // If the integer part is matching, then check the alpha part
        return lhsName.compareToIgnoreCase(rhsName);
    }




}
