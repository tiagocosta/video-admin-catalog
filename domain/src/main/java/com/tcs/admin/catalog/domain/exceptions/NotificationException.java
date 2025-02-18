package com.tcs.admin.catalog.domain.exceptions;

import com.tcs.admin.catalog.domain.validation.Error;
import com.tcs.admin.catalog.domain.validation.handler.Notification;

import java.util.List;

public class NotificationException extends DomainException {

    public NotificationException(final String aMessage, final Notification notification) {
        super(aMessage, notification.getErrors());
    }
}
