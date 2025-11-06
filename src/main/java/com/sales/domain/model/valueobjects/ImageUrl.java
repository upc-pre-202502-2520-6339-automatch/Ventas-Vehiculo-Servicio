package com.sales.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class ImageUrl {
    private String value;
    protected ImageUrl() {}
    public ImageUrl(String value){
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Image URL required");
        if (!(value.startsWith("http://") || value.startsWith("https://")))
            throw new IllegalArgumentException("Image URL must start with http:// or https://");
        if (value.length() > 2048) throw new IllegalArgumentException("Image URL too long");
        this.value = value;
    }
    @Override public String toString(){ return value; }
}
