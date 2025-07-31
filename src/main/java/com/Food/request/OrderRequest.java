package com.Food.request;

import com.Food.models.Address;
import lombok.Data;

@Data
public class OrderRequest {
    private Long resturantId;
    private Address deliveryAddress;
}
