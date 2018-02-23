package com.homework;

import beans.YandexSpellerAnswer;
import core.YandexSpellerApi;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static core.YandexSpellerApi.with;
import static core.YandexSpellerConstants.ErrorCode;
import static core.YandexSpellerConstants.ErrorCode.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class YandexSpellerErrorCodesTests {


    @DataProvider
    public static Object[][] wordWithError() {
        return new Object[][]{
                {"сабака", ERROR_UNKNOWN_WORD},
                {"сабака сабака", ERROR_REPEAT_WORD},
                {"арктику", ERROR_CAPITALIZATION},
                {"сабака атрпавила дамой тилиграму", ERROR_TOO_MANY_ERRORS}
        };
    }

    @Test(dataProvider = "wordWithError")
    public void checkErrorCodeTest(String content, ErrorCode error) {
        List<YandexSpellerAnswer> answers = YandexSpellerApi.getYandexSpellerAnswers(with().text(content).callApi());

        assertThat(answers, both(hasSize(1))
                .and(contains(hasProperty("word", equalTo(content))))
                .and(contains(hasProperty("code", equalTo(error.getValue())))));

    }

}
