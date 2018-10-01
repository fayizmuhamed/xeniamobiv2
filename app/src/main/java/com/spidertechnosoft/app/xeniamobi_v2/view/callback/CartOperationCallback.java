package com.spidertechnosoft.app.xeniamobi_v2.view.callback;

import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Product;

/**
 * Created by DELL on 12/30/2017.
 */

public interface CartOperationCallback {

    public boolean changeQuantity(Product product, Double qty);

    public boolean increaseCartItem(Product product);

    public boolean reduceFromCart(Product product);

    public boolean reduceAndRemoveFromCart(Product product);

    public boolean removeFromCart(Product product);

    public Integer getCartQty(Product product);

    public boolean changeItemPrice(Product product, Double price);

}
