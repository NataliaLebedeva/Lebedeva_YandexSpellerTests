package core;

import lombok.Getter;

/**
 * Created by yulia_atlasova@epam.com on 22/06/2017.
 * Constants of YandexSpeller
 */
public class YandexSpellerConstants {

    //useful constants for API under test
    public static final String YANDEX_SPELLER_API_URI = "https://speller.yandex.net/services/spellservice.json/checkText";
    public static final String PARAM_TEXT = "text";
    public static final String PARAM_OPTIONS = "options";
    public static final String PARAM_LANG = "lang";
    public static final String FORMAT = "format";
    public static final String WRONG_WORD_EN = "requisitee";
    public static final String RIGHT_WORD_EN = "requisite";
    public static final String WRONG_WORD_UK = "питаня";
    public static final String WORD_WITH_WRONG_CAPITAL = "moscow";
    public static final String WORD_WITH_LEADING_DIGITS = "11" + RIGHT_WORD_EN;

    public enum Languages {
        RU("ru"),
        UK("uk"),
        EN("en");
        String languageCode;

        private Languages(String lang) {
            this.languageCode = lang;
        }
    }

    @Getter
    public enum Options {
        IGNORE_DIGITS(2),
        IGNORE_URLS(4),
        FIND_REPEAT_WORDS(8),
        IGNORE_CAPITALIZATION(512);
        private Integer optionsCode;

        Options(Integer optionsCode) {
            this.optionsCode = optionsCode;
        }
    }

    public enum Format {
        PLAIN("plain"),
        HTML("html");
        String formatParam;

        Format(String formatParam) {
            this.formatParam = formatParam;
        }
    }

    @Getter
    public enum ErrorCode {
        ERROR_UNKNOWN_WORD(1),
        ERROR_REPEAT_WORD(2),
        ERROR_CAPITALIZATION(3),
        ERROR_TOO_MANY_ERRORS(4);

        private int value;

        ErrorCode(int value) {
            this.value = value;
        }
    }
}
