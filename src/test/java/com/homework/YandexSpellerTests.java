package com.homework;


import beans.YandexSpellerAnswer;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static core.YandexSpellerApi.getYandexSpellerAnswers;
import static core.YandexSpellerApi.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class YandexSpellerTests {


    @DataProvider
    public static Object[][] simpleRuEnWordsWithMistake() {
        return new String[][]{
                {"мышы", "мышь"},
                {"квартирко", "квартира"},
                {"сабака", "собака"},
//              {"кредит", "грабеж"},
                {"mous", "mouse"},
                {"isue", "issue"},
        };
    }

    @DataProvider
    public static Object[] simpleWordsCorrect() {
        return new String[]{
                "мышь",
                "собака",
                "mouse",
                "issue",
                "квартира",
                "передмістя",
                "фацdwa32re"
        };
    }

    @Test(
            dataProvider = "simpleWordsCorrect",
            description = "Method should return empty answer in case if we sent correct word"
    )
    public void smokeCorrectSingleDefaultTest(String correctWord) {
        List<YandexSpellerAnswer> answers = getYandexSpellerAnswers(with().text(correctWord).callApi());

        assertThat(answers, empty());
    }


    @Test(
            dataProvider = "simpleRuEnWordsWithMistake",
            description = "Method should provide us with correct words in case if we sent word with mistake"
    )
    public void smokeWrongSingleDefaultTest(String wrongWord, String correctWord) {
        List<YandexSpellerAnswer> answers = getYandexSpellerAnswers(with().text(wrongWord).callApi());

        assertThat(answers, both(hasSize(1))
                .and(contains(hasProperty("word", equalTo(wrongWord))))
                .and(contains(hasProperty("s", hasItem(correctWord)))));
    }


}
