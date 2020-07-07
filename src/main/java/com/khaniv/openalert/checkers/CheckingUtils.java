package com.khaniv.openalert.checkers;

import com.khaniv.openalert.documents.BaseDocument;
import com.khaniv.openalert.errors.exceptions.MaxCountExcessException;
import lombok.experimental.UtilityClass;

import java.util.Collection;

@UtilityClass
public class CheckingUtils {
    public final int MAX_COUNT = 100000;

    public void checkMaxSize(Collection<?> collection, Class<? extends BaseDocument> documentClass) {
        if (collection.size() > MAX_COUNT)
            throw new MaxCountExcessException(documentClass, MAX_COUNT);
    }
}
