package com.example.webpos.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Cart {

    private List<Item> items = new ArrayList<>();
    private int total=0;

    public boolean addItem(Item item) {
        boolean ret= items.add(item);
        if(ret){
            total+=item.getProduct().getPrice();
        }
        return ret;
    }

    @Override
    public String toString() {
        if (items.size() ==0){
            return "Empty Cart";
        }
        double total = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cart -----------------\n"  );

        for (int i = 0; i < items.size(); i++) {
            stringBuilder.append(items.get(i).toString()).append("\n");
            total += items.get(i).getQuantity() * items.get(i).getProduct().getPrice();
        }
        stringBuilder.append("----------------------\n"  );

        stringBuilder.append("Total...\t\t\t" + total );

        return stringBuilder.toString();
    }

    public boolean set(String productId,int amount){
        for (Item item:
             items) {
            if(item.getProduct().equals(productId)){
                total-=item.getQuantity()*item.getProduct().getPrice();
                item.setQuantity(amount);
                total+=item.getQuantity()*item.getQuantity()*item.getProduct().getPrice();
                return true;
            }
        }
        return false;
    }

    public boolean del(String productId){
        Item toDel=null;
        for (Item item:
                items) {
            if(item.getProduct().getId().equals(productId)){
                toDel=item;
            }
        }
        if(toDel!=null){
            total-=toDel.getQuantity()*toDel.getProduct().getPrice();
            items.remove(toDel);
            return true;
        }
        return false;
    }

    public boolean increase(String productId,int amount){
        for (Item item:
                items) {
            if(item.getProduct().getId().equals(productId)){
                total+=item.getProduct().getPrice();
                item.setQuantity(item.getQuantity()+amount);
                return true;
            }
        }
        return false;
    }

    public void clear(){
        total=0;
        items.clear();
    }

    public boolean decrease(String productId,int amount){
        Item toDel=null;
        boolean del=false;
        for (Item item:
                items) {
            if(item.getProduct().getId().equals(productId)){
                if(amount<item.getQuantity()){
                    item.setQuantity(item.getQuantity()-amount);
                    total-=item.getProduct().getPrice()*amount;
                    return true;
                }else{
                    del=true;
                    toDel=item;
                }
            }
        }
        if(del){
            total-=toDel.getQuantity()*toDel.getProduct().getPrice();
            items.remove(toDel);
            return true;
        }
        return false;
    }

}
