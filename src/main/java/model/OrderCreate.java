package model;

import java.util.List;

public class OrderCreate {
    public OrderCreate(List<Object> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Object> ingredients;

}