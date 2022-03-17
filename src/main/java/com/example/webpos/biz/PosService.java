package com.example.webpos.biz;

import com.example.webpos.model.Cart;
import com.example.webpos.model.Product;

import java.util.List;

public interface PosService {
    public Cart getCart();

    public Cart newCart();

    public void checkout(Cart cart);

    public void total(Cart cart);

    public boolean add(Product product, int amount);

    public boolean add(String productId, int amount);

    public boolean set(String productId,int amount);

    public boolean increase(String productId,int amount);

    public boolean decrease(String productId,int amount);

    public boolean del(String productId);

    public  void clear();

    public List<Product> products();
}
