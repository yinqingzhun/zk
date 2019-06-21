package qs.model.vo;

import lombok.Data;

@Data
public class CarPriceCard {
    private String factoryPrice;
    private String dealPrice;
    private String priceoff;
    private int priceoffValue;
    private String specName;
    private String promotionCondition;
     
}