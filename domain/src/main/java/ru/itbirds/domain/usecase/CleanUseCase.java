package ru.itbirds.domain.usecase;

import java.text.ParseException;
import java.util.Date;

import io.reactivex.disposables.Disposable;

public interface CleanUseCase {
    Disposable clean();

    boolean isOldDate(Date currentDate) throws ParseException;
}
