package ru.itbirds.domain.use_case;

import java.text.ParseException;
import java.util.Date;

public interface CleanUseCase {
    void clean();

    boolean isOldDate(Date currentDate) throws ParseException;
}
