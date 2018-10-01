package com.spidertechnosoft.app.xeniamobi_v2.session;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Product;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.User;
import com.spidertechnosoft.app.xeniamobi_v2.model.resource.CartItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SessionManager {

    // Shared Preferences
    SharedPreferences pref;

    Gson gson = new Gson();

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;


    // Sharedpref file password
    private static final String PREF_NAME = "XeniaMobi";

    private static final String KEY_IS_FIRST_TIME_USER="first_time_use";

    private static final String KEY_LOGGED_IN_USER="logged_in_user";

    private static final String KEY_CART_CONTENT="cart_content";


    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE| Context.MODE_MULTI_PROCESS);
        editor = pref.edit();
    }

    public boolean isFirstTimeUse() {
        try {

            return pref.getBoolean(KEY_IS_FIRST_TIME_USER, true);

        } catch (Exception e) {
            e.printStackTrace();//TODO handle Exception elegantly (throw Observable.error)
        }
        return true;
    }


    public void setFirstTimeUsage(Boolean status) {

        try {
            editor.putBoolean(KEY_IS_FIRST_TIME_USER,status);
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();//TODO handle Exception elegantly (throw Observable.error)
        }
    }


    public User getLoggedInUser() {
        try {
            String json = pref.getString(KEY_LOGGED_IN_USER, null);
            User user = gson.fromJson(json, User.class);
            return user;

        } catch (Exception e) {
            e.printStackTrace();//TODO handle Exception elegantly (throw Observable.error)
        }
        return null;
    }

    public void setLoggedInUser(User user) {
        try {
            String json = gson.toJson(user);
            editor.putString(KEY_LOGGED_IN_USER,json);
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();//TODO handle Exception elegantly (throw Observable.error)
        }

    }
    public Boolean removeLoggedInUser() {

        try {
            // Store ite
            editor.putString(KEY_LOGGED_IN_USER,"");

            // Commit
            editor.commit();

            return true;

        } catch (Exception e) {
            e.printStackTrace();//TODO handle Exception elegantly (throw Observable.error)
        }

        return false;
    }

    /**
     * Method to update the cart in the shared pref
     *
     * @param cartItem - The cart Item list
     */
    public void updateCart(CartItem cartItem) {

        HashMap<String,CartItem> cartItemHashMap=null;

        cartItemHashMap=getCartContentMap();

        if(cartItemHashMap==null)
            cartItemHashMap=new HashMap<>();

        cartItemHashMap.put(cartItem.getItemUid(),cartItem);

        // Get the json representation of the cartItemList
        String cartListJson = gson.toJson(cartItemHashMap);

        // Store ite
        editor.putString(KEY_CART_CONTENT,cartListJson);

        // Commit
        editor.commit();


    }

    /**
     * Function to return the cartItem list
     * Returns null if the data is not available
     */
    public HashMap<String,CartItem> getCartContentMap() {

        // Get the json representation of the cartItemList
        String cartListJson =  pref.getString(KEY_CART_CONTENT, "");

        // Delcare the list
        HashMap<String,CartItem> cartItemHashMap=new HashMap<>();

        // parse if its not null
        if ( cartListJson != null && !cartListJson.equals("") &&!cartListJson.equals("null")) {

            // Parse back to the item list
            cartItemHashMap = gson.fromJson(cartListJson, new TypeToken<HashMap<String,CartItem>>(){}.getType());

        }

        // Return the list
        return cartItemHashMap;


    }

    /**
     * Function to return the cartItem list
     * Returns null if the data is not available
     */
    public List<CartItem> getCartContent() {

        // Get the json representation of the cartItemList
        String cartListJson =  pref.getString(KEY_CART_CONTENT, "");

        // Delcare the list
        HashMap<String,CartItem> cartItemHashMap=new HashMap<>();

        // parse if its not null
        if ( cartListJson != null && !cartListJson.equals("") &&!cartListJson.equals("null")) {

            // Parse back to the item list
            cartItemHashMap = gson.fromJson(cartListJson, new TypeToken<HashMap<String,CartItem>>(){}.getType());

        }

        // Return the list
        return cartItemHashMap==null?null:new ArrayList<CartItem>(cartItemHashMap.values());


    }

    public void removeProductFromCart(Product product) {

        HashMap<String,CartItem> cartItemHashMap=null;

        cartItemHashMap=getCartContentMap();

        if(cartItemHashMap==null)
            cartItemHashMap=new HashMap<>();

        if(cartItemHashMap.containsKey(product.getUid()))
            cartItemHashMap.remove(product.getUid());

        // Get the json representation of the cartItemList
        String cartListJson = gson.toJson(cartItemHashMap);

        // Store ite
        editor.putString(KEY_CART_CONTENT,cartListJson);

        // Commit
        editor.commit();


    }

    /**
     * Function to return the cartItem list
     * Returns null if the data is not available
     */
    public CartItem getCartContentForProduct(Product product) {

        HashMap<String,CartItem> cartItemHashMap=null;

        cartItemHashMap=getCartContentMap();

        if(cartItemHashMap==null)
            cartItemHashMap=new HashMap<>();

        if(cartItemHashMap.containsKey(product.getUid()))
            return cartItemHashMap.get(product.getUid());

        // Return the list
        return null;


    }


    /**
     * Puts the cart to empty string
     */
    public void clearCart() {

        // Store ite
        editor.putString(KEY_CART_CONTENT,"");

        // Commit
        editor.commit();

    }


}
