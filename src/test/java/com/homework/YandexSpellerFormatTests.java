package com.homework;

import beans.YandexSpellerAnswer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static core.YandexSpellerApi.getYandexSpellerAnswers;
import static core.YandexSpellerApi.with;
import static core.YandexSpellerConstants.Format.HTML;
import static core.YandexSpellerConstants.Format.PLAIN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class YandexSpellerFormatTests {

    @DataProvider
    public static Object[][] positiveRequestFormat() {
        return new Object[][]{
                {new RequestFormat("retorn", "<span class='keywurd'>%s</span>"), "return"},
                {new RequestFormat("mous", "<span class='keywurd'>%s</span>"), "mouse"},
                {new RequestFormat("isue", "<span class='keywurd'>%s</span>"), "issue"},
                {new RequestFormat("квартирко", "<span class='keywurd'>%s</span>"), "квартира"},
                {new RequestFormat("сабака", "<span class='keywurd'>%s</span>"), "собака"},
                {new RequestFormat("мышы", "<span class='keywurd'>%s</span>"), "мышь"},

        };
    }

    @DataProvider
    public static Object[][] negativeRequestFormat() {
        return new Object[][]{
                {new RequestFormat("retorn", "<p class='%s'></p>"), "return"},
                {new RequestFormat("keywurd", "<li class='%s'></li>"), "keyword"},
                {new RequestFormat("tabl", "<%s clas='raw'>"), "table"},
                {new RequestFormat("bodi", "<%s clas='raw'>"), "body"},
                {new RequestFormat("сабака", "<span class='%s'></span>"), "собака"}
        };
    }

    @Test(
            dataProvider = "positiveRequestFormat",
            description = "Method should return a response with suggestions for the word between tags"
    )
    public void htmlFormatTest(RequestFormat request, String correctWord) {
        List<YandexSpellerAnswer> answers = getYandexSpellerAnswers(with()
                .text(request.getHtml()).format(HTML).callApi());

        assertThat(answers, both(hasSize(1))
                .and(contains(hasProperty("word", equalTo(request.getWord()))))
                .and(contains(hasProperty("s", hasItem(correctWord)))));
    }

    @Test(
            dataProvider = "negativeRequestFormat",
            description = "Method should return suggestions for any incorrect word"
    )
    public void plainFormatTest(RequestFormat requestFormat, String correctWord) {
        List<YandexSpellerAnswer> answers = getYandexSpellerAnswers(with()
                .text(requestFormat.getHtml()).format(PLAIN).callApi());
        List<String> allSuggestions = answers.stream().map(YandexSpellerAnswer::getS).flatMap(Collection::stream).collect(Collectors.toList());

        MatcherAssert.assertThat(answers.size(), Matchers.greaterThan(1));
        MatcherAssert.assertThat(allSuggestions, hasItem(containsString(correctWord)));
    }

    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class RequestFormat {

        @Getter
        private String word;
        private String html;

        String getHtml() {
            return String.format(html, word);
        }

    }
}
