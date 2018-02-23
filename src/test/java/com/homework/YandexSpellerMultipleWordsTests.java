package com.homework;

import beans.YandexSpellerAnswer;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static core.YandexSpellerApi.getYandexSpellerAnswers;
import static core.YandexSpellerApi.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

public class YandexSpellerMultipleWordsTests {

    @DataProvider
    public static Object[][] severalWordsRequest() {
        return new Object[][]{
                {
                        Arrays.asList("сабака", "аткрыть", "квартиро"),
                        Arrays.asList("собака", "открыть", "квартира")
                },
                {
                        Arrays.asList("mous", "isue", "retorn"),
                        Arrays.asList("mouse", "issue", "return")
                },
                {
                        Arrays.asList("keywurd", "fligt"),
                        Arrays.asList("keyword", "flight")
                }
        };
    }

    @Test(dataProvider = "severalWordsRequest", description = "Method should return a list of suggestions for every word")
    public void requestMultipleTest(List<String> request, List<String> results) {
        List<YandexSpellerAnswer> answers = getYandexSpellerAnswers(with()
                .text(request.stream().collect(Collectors.joining(" "))).callApi());
        List<String> collect = answers.stream().map(YandexSpellerAnswer::getS).flatMap(Collection::stream).collect(Collectors.toList());

        assertThat(answers, hasSize(results.size()));
        assertThat(collect, hasItems((String[]) results.toArray()));
    }
}
