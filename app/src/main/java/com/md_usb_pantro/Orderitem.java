package com.md_usb_pantro;

public class Orderitem {
    String Itemname,ItemPrice,ItemId;
    Integer Itemquantity;

    public Orderitem(String itemname, String itemPrice, String itemId, int itemquantity) {
        Itemname = itemname;
        ItemPrice = itemPrice;
        ItemId = itemId;
        Itemquantity = itemquantity;
    }

    public String getItemname() {
        return Itemname;
    }

    public void setItemname(String itemname) {
        Itemname = itemname;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public Integer getItemquantity() {
        return Itemquantity;
    }

    public void setItemquantity(Integer itemquantity) {
        Itemquantity = itemquantity;
    }
}
