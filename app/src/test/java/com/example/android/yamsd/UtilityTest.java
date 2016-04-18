package com.example.android.yamsd;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Тестирование функций из класса Utility
 */
public class UtilityTest {

    @Test
    public void utilityTestPluralize() throws Exception {

        int[] counts = {
                512,
                330,
                729,
                121,
                243
        };

        String[] resultsOfAlbumsExpected = {
                "альбомов",
                "альбомов",
                "альбомов",
                "альбом",
                "альбома"
        };

        String[] resultsOfTracksExpected = {
                "песен",
                "песен",
                "песен",
                "песня",
                "песни"
        };

        for (int i = 0; i < counts.length; i++) {
            assertEquals(
                    resultsOfAlbumsExpected[i],
                    Utility.pluralize(counts[i], "альбом")
            );

            assertEquals(
                    resultsOfTracksExpected[i],
                    Utility.pluralize(counts[i], "песня")
            );
        }
    }

    @Test
    public void utilityTestGetAlbumsAndTracksAsSingleString()
        throws Exception {
        //Функция pluralize уже была протестирована
        //в предыдущем тесте, смысла тестировать ее здесь нет

        assertEquals(
                "123 альбома, 456 песен",
                Utility.getAlbumsAndTracksAsString(123, 456)
        );
    }

    @Test
    public void utilityTestGetGenresAsSingleString()
        throws Exception {

        String[] manyGenres = {
                "pop",
                "rock",
                "techno"
        };
        String manyGenresSingleString =
                "pop, rock, techno";
        assertEquals(
                manyGenresSingleString,
                Utility.getGenresAsSingleString(manyGenres)
        );

        String[] oneGenre = {
                "rnb"
        };
        String oneGenreSingleString =
                "rnb";
        assertEquals(
                oneGenreSingleString,
                Utility.getGenresAsSingleString(oneGenre)
        );

        String[] noGenre = {};
        String noGenreSingleString = "";
        assertEquals(
                noGenreSingleString,
                Utility.getGenresAsSingleString(noGenre)
        );

        //Следующий тест проверяет работу исключения
        String[] invalidGenre = null;
        Utility.getGenresAsSingleString(invalidGenre);
    }

}
