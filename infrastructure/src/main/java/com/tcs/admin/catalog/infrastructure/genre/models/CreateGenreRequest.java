package com.tcs.admin.catalog.infrastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public record CreateGenreRequest(
        String name,
        @JsonProperty("is_active") Boolean active,
        @JsonProperty("categories_id") List<String> categories
) {

    public boolean isActive() {
        return active != null ? active : true;
    }

    public List<String> categories() {
        return this.categories != null ? this.categories : Collections.emptyList();
    }

}
