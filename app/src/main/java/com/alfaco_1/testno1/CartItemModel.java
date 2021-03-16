package com.alfaco_1.testno1;

class CartItemModel {

    public static final int CART_ITEM = 0;
    public static final int TOTAL_AMOUNT = 1;

    private int type;

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    /////////// cart
    private String productID;
    private String productImage;
    private long freeCoupens;
    private String productTitle;
    private String productPrice;
    private String cuttedPrice;
    private long productQuantity;
    private long offersApplied;
    private long coupensApplied;
    private boolean inStock;


    public CartItemModel(int type,String productID, String productImage,String productTitle, Long freeCoupens,String productPrice,String cuttedPrice, Long productQuantity, Long offersApplied, Long coupensApplied, boolean inStock) {
        this.type = type;
        this.productID = productID;
        this.productImage = productImage;
        this.freeCoupens = freeCoupens;
        this.productQuantity = productQuantity;
        this.offersApplied = offersApplied;
        this.coupensApplied = coupensApplied;
        this.productTitle = productTitle;
        this.productPrice = productPrice;
        this.cuttedPrice = cuttedPrice;
        this.inStock = inStock;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public long getFreeCoupens() {
        return freeCoupens;
    }

    public void setFreeCoupens(long freeCoupens) {
        this.freeCoupens = freeCoupens;
    }

    public long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public long getOffersApplied() {
        return offersApplied;
    }

    public void setOffersApplied(long offersApplied) {
        this.offersApplied = offersApplied;
    }

    public long getCoupensApplied() {
        return coupensApplied;
    }

    public void setCoupensApplied(long coupensApplied) {
        this.coupensApplied = coupensApplied;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getCuttedPrice() {
        return cuttedPrice;
    }

    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }
//    public String getProductID() {
//        return productID;
//    }
//    public void setProductID(String productID) {
//        this.productID = productID;
//    }
//
//    public String getProductImage() {
//        return productImage;
//    }
//    public void setProductImage(String productImage) {
//        this.productImage = productImage;
//    }
//    public Long getFreeCoupens() {
//        return freeCoupens;
//    }
//    public void setFreeCoupens(Long freeCoupens) {
//        this.freeCoupens = freeCoupens;
//    }
//    public Long getProductQuantity() {
//        return productQuantity;
//    }
//    public void setProductQuantity(Long productQuantity) {
//        this.productQuantity = productQuantity;
//    }
//    public Long getOffersApplied() {
//        return offersApplied;
//    }
//    public void setOffersApplied(Long offersApplied) {
//        this.offersApplied = offersApplied;
//    }
//    public Long getCoupensApplied() {
//        return coupensApplied;
//    }
//    public void setCoupensApplied(Long coupensApplied) {
//        this.coupensApplied = coupensApplied;
//    }
//    public String getProductTitle() {
//        return productTitle;
//    }
//    public void setProductTitle(String productTitle) {
//        this.productTitle = productTitle;
//    }
//    public String getProductPrice() {
//        return productPrice;
//    }
//    public void setProductPrice(String productPrice) {
//        this.productPrice = productPrice;
//    }
//    public String getCuttedPrice() {
//        return cuttedPrice;
//    }
//    public void setCuttedPrice(String cuttedPrice) {
//        this.cuttedPrice = cuttedPrice;
//    }
///////////cart


    /////////////// cart total

    public CartItemModel(int type) {
        this.type = type;
    }

    /////////////// cart total

}
