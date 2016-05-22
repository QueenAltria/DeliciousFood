package com.jiang.deliciousfood.bean;

/**
 * Created by Administrator on 2016/5/2.
 */
public class CollectionBean {
    private int foodId;
    private String foodName;

    public CollectionBean() {
    }

    public CollectionBean(int foodId, String foodName) {
        this.foodId = foodId;
        this.foodName = foodName;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}
