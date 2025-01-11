package com.ajtraders.product.enums;

import com.ajtraders.product.exception.ServiceException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public enum Category {
    ELECTRONICS("Electronics"),
    CLOTHING("Clothing"),
    HOME_AND_GARDEN("Home and Garden"),
    SPORTS_AND_OUTDOORS("Sports & Outdoors");

    private final String mappedValue;

    Category(String mappedValue) {
        this.mappedValue = mappedValue;
    }


    public static Category fromMappedValue(String mappedValue) {
        if (mappedValue == null || mappedValue.isBlank()) {
            return null;
        }
        for (Category transactionType : Category.values()) {
            if (transactionType.mappedValue.equalsIgnoreCase(mappedValue)) {
                return transactionType;
            }
        }
        throw new ServiceException("Unsupported type: " + mappedValue, "Bad request", HttpStatus.BAD_REQUEST);
    }


    public String getMappedValue() {
        return mappedValue;
    }

    public static List<String> getAll() {
        List<String> list = new ArrayList<>();
        for (Category transactionType : Category.values()) {
            list.add(transactionType.mappedValue);
        }
        return list;
    }
}
