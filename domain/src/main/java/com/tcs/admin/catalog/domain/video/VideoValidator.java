package com.tcs.admin.catalog.domain.video;

import com.tcs.admin.catalog.domain.validation.Error;
import com.tcs.admin.catalog.domain.validation.ValidationHandler;
import com.tcs.admin.catalog.domain.validation.Validator;

public class VideoValidator extends Validator {

    private static final int TITLE_MAX_LENGTH = 255;
    private static final int TITLE_MIN_LENGTH = 1;
    private static final int DESCRIPTION_MAX_LENGTH = 4000;
    private static final int DESCRIPTION_MIN_LENGTH = 1;
    private final Video video;

    public VideoValidator(final Video aVideo, final ValidationHandler aHandler) {
        super(aHandler);
        this.video = aVideo;
    }

    @Override
    public void validate() {
        checkTitleConstraints();
        checkDescriptionConstraints();
        checkLaunchedAtConstraints();
        checkRatingConstraints();
    }

    private void checkTitleConstraints() {
        final var title = this.video.getTitle();
        if (title == null) {
            this.validationHandler().append(new Error("'title' should not be null"));
            return;
        }

        if (title.isBlank()) {
            this.validationHandler().append(new Error("'title' should not be empty"));
            return;
        }

        int length = title.trim().length();
        if (length > TITLE_MAX_LENGTH || length < TITLE_MIN_LENGTH) {
            this.validationHandler().append(
                    new Error("'title' must be between %s and %s characters"
                            .formatted(TITLE_MIN_LENGTH, TITLE_MAX_LENGTH))
            );
        }
    }

    private void checkDescriptionConstraints() {
        final var description = this.video.getDescription();
        if (description == null) {
            this.validationHandler().append(new Error("'description' should not be null"));
            return;
        }

        if (description.isBlank()) {
            this.validationHandler().append(new Error("'description' should not be empty"));
            return;
        }

        int length = description.trim().length();
        if (length > DESCRIPTION_MAX_LENGTH || length < DESCRIPTION_MIN_LENGTH) {
            this.validationHandler().append(
                    new Error("'description' must be between %s and %s characters"
                            .formatted(DESCRIPTION_MIN_LENGTH, DESCRIPTION_MAX_LENGTH))
            );
        }
    }

    private void checkLaunchedAtConstraints() {
        final var launchedAt = this.video.getLaunchedAt();
        if (launchedAt == null) {
            this.validationHandler().append(new Error("'launchedAt' should not be null"));
        }
    }

    private void checkRatingConstraints() {
        final var rating = this.video.getRating();
        if (rating == null) {
            this.validationHandler().append(new Error("'rating' should not be null"));
        }
    }
}
