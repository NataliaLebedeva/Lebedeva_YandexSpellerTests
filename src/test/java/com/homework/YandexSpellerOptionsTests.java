package com.homework;

import beans.YandexSpellerAnswer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static core.YandexSpellerApi.getYandexSpellerAnswers;
import static core.YandexSpellerApi.with;
import static core.YandexSpellerConstants.Options.IGNORE_DIGITS;
import static core.YandexSpellerConstants.Options.IGNORE_URLS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class YandexSpellerOptionsTests {

    @DataProvider
    public static Object[][] wordsWithDigits() {
        return new Object[][]{
                {new RequestStringConstruct("сабака", "авфы6", "https://speller.yandex.net"), "собака"},
                {new RequestStringConstruct("сабака", "ав8фы6", "reedmi.txt"), "собака"},
                {new RequestStringConstruct("mous", "isu78", "readme.txt"), "mouse"},
                {new RequestStringConstruct("mous", "is45u", "readme.txt"), "mouse"},
                {new RequestStringConstruct("аткрыть", "мыши23", "localName@mail.ru"), "открыть"},
                {new RequestStringConstruct("аткрыть", "мы24ши", "localName@mail.ru"), "открыть"},
                {new RequestStringConstruct("isue", "play23", "lokalName@mael.ru"), "issue"},
                {new RequestStringConstruct("isue", "pl45ay", "lokalName@mael.ru"), "issue"},
                {new RequestStringConstruct("retorn", "gdfg12", "localName@mail.ru"), "return"},
                {new RequestStringConstruct("retorn", "req4est", "localName@mail.ru"), "return"}
        };
    }

    @Test(dataProvider = "wordsWithDigits")
    public void ignoreDigitOptionsTest(RequestStringConstruct request, String correctWord) {
        List<YandexSpellerAnswer> answers = getYandexSpellerAnswers(with()
                .options(IGNORE_DIGITS)
                .text(request.getSimpleString()).callApi());

        assertThat(answers, both(hasSize(1))
                .and(contains(hasProperty("word", equalTo(request.getWrongWord()))))
                .and(contains(hasProperty("s", hasItem(correctWord)))));
    }

    @Test(dataProvider = "wordsWithDigits", description = "Method should return suggestions just for one word")
    public void combiOptionsTest(RequestStringConstruct request, String correctWord) {
        List<YandexSpellerAnswer> answers = getYandexSpellerAnswers(with()
                .options(IGNORE_DIGITS, IGNORE_URLS)
                .text(request.getComplexString()).callApi());

        assertThat(answers, both(hasSize(1))
                .and(contains(hasProperty("word", equalTo(request.getWrongWord()))))
                .and(contains(hasProperty("s", hasItem(correctWord)))));
    }

    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class RequestStringConstruct {
        @Getter
        private String wrongWord;
        private String withDigits;
        private String url;

        String getSimpleString() {
            return String.format("%s %s", wrongWord, withDigits);
        }

        String getComplexString() {
            return String.format("%s %s %s", wrongWord, withDigits, url);
        }
    }
}
