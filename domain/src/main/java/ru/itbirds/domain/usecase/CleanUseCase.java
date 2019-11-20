package ru.itbirds.domain.usecase;

import java.text.ParseException;
import java.util.Date;

public interface CleanUseCase {
    void clean();

    boolean isOldDate(Date currentDate) throws ParseException;
}
