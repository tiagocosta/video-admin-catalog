package com.tcs.admin.catalog.infrastructure.video.persistence;

import com.tcs.admin.catalog.domain.video.Rating;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RatingConverter implements AttributeConverter<Rating, String> {

    @Override
    public String convertToDatabaseColumn(Rating rating) {
        if (rating == null) return null;
        return rating.getName();
    }

    @Override
    public Rating convertToEntityAttribute(String s) {
        if (s == null) return null;
        return Rating.of(s).orElse(null);
    }
}
