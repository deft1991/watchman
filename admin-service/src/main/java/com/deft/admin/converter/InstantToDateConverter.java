package com.deft.admin.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * @author Sergey Golitsyn
 * created on 31.01.2024
 */
public class InstantToDateConverter implements Converter<LocalDate, Instant> {

    public InstantToDateConverter() {
    }

    public Result<Instant> convertToModel(LocalDate localDate, ValueContext context) {
        return localDate == null ? Result.ok((Instant) null) : Result.ok(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public LocalDate convertToPresentation(Instant instant, ValueContext valueContext) {
        return instant == null ? null : instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
