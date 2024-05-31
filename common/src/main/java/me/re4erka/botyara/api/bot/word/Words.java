package me.re4erka.botyara.api.bot.word;

import lombok.Getter;
import me.re4erka.botyara.api.bot.word.cache.CacheWords;
import me.re4erka.botyara.api.bot.word.search.SearchWords;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;
import java.util.OptionalInt;

public class Words {
    private final String[] formattedWords;
    private final String[] originalWords;

    @Getter
    private final boolean userCalledBot;
    @Getter
    private final boolean isPrivate;
    @Getter
    private final boolean isEdited;

    /* Специфичные символы, которые будут удаляться. */
    private static final String[] REPLACEMENTS = {",", ".", "!", "?", "@", "(", ")", ":", "-", "\"", "'", "`"};
    /* Разделение предложения на слова, удаляя пробелы и пропуски строк */
    public static final String SEPARATOR = " " + System.lineSeparator();

    private static final String[] WORDS_TO_CALL_BOT = new String[] {
            "ботяра", "бот", "ботя", "робот", "ботик", "ботиха", "ботярище", "botyara", "bot", "robot"
    };

    private Words(
            String[] formattedWords,
            String[] originalWords,
            boolean userCalledBot,
            boolean isPrivate,
            boolean isEdited
    ) {
        this.formattedWords = formattedWords;
        this.originalWords = originalWords;

        this.userCalledBot = userCalledBot;
        this.isPrivate = isPrivate;
        this.isEdited = isEdited;
    }

    public String get(int index) {
        return formattedWords[index];
    }

    public String getRaw(int index) {
        return originalWords[index];
    }

    public boolean equals(int index, String word) {
        return StringUtils.equals(formattedWords[index], word);
    }

    public boolean equals(Words words) {
        if (words.size() != formattedWords.length) {
            return false;
        }

        for (int i = 0; i < formattedWords.length; i++) {
            final String word = formattedWords[i];
            final String anotherWord = words.get(i);

            if (!word.equals(anotherWord)) {
                return false;
            }
        }

        return true;
    }

    public boolean contains(String targetWord) {
        for (String word : formattedWords) {
            if (targetWord.equals(word)) {
                return true;
            }
        }

        return false;
    }

    public boolean contains(String[] searchWordArray) {
        for (String searchWord : searchWordArray) {
            if (contains(searchWord)) {
                return true;
            }
        }

        return false;
    }

//    public boolean containsIgnoreCase(String[] searchWords) {
//        for (String searchWord : searchWords) {
//            if (containsIgnoreCase(searchWord)) {
//                return true;
//            }
//        }
//
//        return false;
//    }

//    public boolean containsIgnoreCase(String targetWord) {
//        for (String word : formattedWords) {
//            if (targetWord.equalsIgnoreCase(word)) {
//                return true;
//            }
//        }
//
//        return false;
//    }

    public boolean matchesAny(SearchWords matchesWords) {
        for (int i = 0; i < matchesWords.size(); i++) {
            final Words words = matchesWords.get(i);

            if (words == null) {
                return false;
            }

            if (equals(words)) {
                return true;
            }
        }

        return false;
    }

    public boolean containsAny(SearchWords searchWords) {
        for (int i = 0; i < searchWords.size(); i++) {
            final Words words = searchWords.get(i);

            if (words == null) {
                return false;
            }

            if (contains(words)) {
                return true;
            }
        }

        return false;
    }

    public boolean contains(Words searchWords) {
        int index = getFirstEqualsWordIndex(
                searchWords.get(0)
        );

        if (index == -1) {
            return false;
        }

        if ((formattedWords.length - index) + 1 < searchWords.formattedWords.length) {
            return false;
        }

        for (int i = 1; i < searchWords.formattedWords.length; i++) {
            final String searchWord = searchWords.get(i);
            final String word = get(index);

            if (searchWord.equals(word)) {
                index++;
            } else {
                return false;
            }
        }

        return true;
    }

    public OptionalInt find(String searchWord) {
        final int index = getFirstEqualsWordIndex(searchWord);

        return index == -1 ? OptionalInt.empty() : OptionalInt.of(index);
    }

    public OptionalInt find(String[] searchWordArray) {
        for (String word : searchWordArray) {
            final OptionalInt optionalIndex = find(word);

            if (optionalIndex.isPresent()) {
                return optionalIndex;
            }
        }

        return OptionalInt.empty();
    }

    private int getFirstEqualsWordIndex(String searchWord) {
        for (int index = 0; index < formattedWords.length; index++) {
            final String word = get(index);

            if (searchWord.equals(word)) {
                return index + 1;
            }
        }

        return -1;
    }

    public int size() {
        return formattedWords == null ? 0 : formattedWords.length;
    }

    public int sizeRaw() {
        return originalWords == null ? 0 : originalWords.length;
    }

    @Override
    public String toString() {
        return StringUtils.join(formattedWords, ' ');
    }

    public CacheWords toCache(long id) {
        return new CacheWords(id, toString());
    }

    public static Words of(String[] formattedWords) {
        return new Words(formattedWords, null, false, false, false);
    }

    public static Words create(String content, boolean isPrivate, boolean isEdited) {
        /* Сохраняем сообщение без каких-либо изменений с поделением на слова в массив */
        final String[] original = StringUtils.split(content, SEPARATOR);

        /*
         * Удаляет специфичные символы, чтобы увеличить способы
         * обращения к боту.
         *
         * К примеру "Бот, как дела" - запятая будет удалена, что позволит проверку на слово "бот".
         *
         * Так же проверяет пустое (blank) ли сообщение.
         *  */
        for (final String replacement : REPLACEMENTS) {
            content = StringUtils.replaceChars(
                    content,
                    replacement,
                    StringUtils.EMPTY
            );

            if (StringUtils.isBlank(content)) {
                return new Words(
                        null,
                        null,
                        false,
                        isPrivate,
                        isEdited
                );
            }
        }

        /* Заменяем букву Ё на Е для удобства проверок слов. */
        content = content.replace('ё', 'е');

        /*
         * Делаем текст всегда нижнего регистра для удобства.
         *
         * Помимо этого, это убирает лишние возможные взаимодействия
         * с текстом у прослушивателей при обратке текста. (listener)
         *
         * Проще говоря, метод необходим, для чуть большой оптимизации.
         * */
        content = content.toLowerCase(Locale.ROOT);

        /* Делим предложение на слова в массив с помощью пробелов */
        final String[] words = StringUtils.split(content, SEPARATOR);

        for (int i = 0; i < words.length; i++) {
            final String word = words[i];

            /* Проверяем содержится ли в предложении слова синонимы к слову "Бот" */
            for (final String botWord : WORDS_TO_CALL_BOT) {
                if (word.equals(botWord)) {
                    if (words.length == 1) {
                        return new Words(null, original, true, isPrivate, isEdited);
                    }

                    /* Words без слова "Бот" так как оно бесполезно в этом случае. */
                    final String[] wordsWithoutBot = ArrayUtils.remove(words, i);

                    return new Words(wordsWithoutBot, original, true, isPrivate, isEdited);
                }
            }
        }

        return new Words(words, original, false, isPrivate, isEdited);
    }
}
