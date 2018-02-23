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
import static core.YandexSpellerConstants.Languages;
import static core.YandexSpellerConstants.Languages.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class YandexSpellerLocalisationTests {

    @DataProvider
    public static Object[][] simpleWordsWithMistake() {
        return new Object[][]{
                {new WordLocalisation("issu", EN, RU), "issue"},
                {new WordLocalisation("мышы", RU, EN), "мышь"},
                {new WordLocalisation("квартирко", RU, EN), "квартира"},
                {new WordLocalisation("перидмістя", UK, EN), "передмістя"},
                {new WordLocalisation("перидмістя", UK, RU), "передмістя"},
        };
    }

    @Test(
            dataProvider = "simpleWordsWithMistake",
            description = "Method should provide us with correct words in case if we sent word with mistake with appropriate language"
    )
    public void localisationPositiveTest(WordLocalisation wordLocalisation, String correctWord) {
        List<YandexSpellerAnswer> answers = getYandexSpellerAnswers(with()
                .language(wordLocalisation.getLanguageCorrect())
                .text(wordLocalisation.getWord()).callApi());

        assertThat(answers, both(hasSize(1))
                .and(contains(hasProperty("word", equalTo(wordLocalisation.getWord()))))
                .and(contains(hasProperty("s", hasItem(correctWord)))));
    }

    @Test(
            dataProvider = "simpleWordsWithMistake",
            description = "Method should provide us empty response in case if we sent word with wrong language parameter"
    )
    public void localisationNegativeTest(WordLocalisation wordLocalisation, String correctWord) {
        List<YandexSpellerAnswer> answers = getYandexSpellerAnswers(with()
                .language(wordLocalisation.getLanguageWrong())
                .text(wordLocalisation.getWord()).callApi());

        assertThat(answers, empty());
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class WordLocalisation {
        private String word;
        private Languages languageCorrect;
        private Languages languageWrong;
    }

}
