package calculation;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class NumberGenerator {

    private static final String LOTTERRY_NUMBERS_TEXT = "Lotterry numbers: ";
    private int[] lotteryRandomNumbers;

    public NumberGenerator() {
    }

    /**
     * Az 5 lottószám generálása 1-től 90-ig.
     * Ha egy generált szám már létezik a tömben akkor generál új számot.
     */
    protected int[] generateLotteryNumbers() {
        System.out.println(LOTTERRY_NUMBERS_TEXT);
        lotteryRandomNumbers = ThreadLocalRandom.current().ints(1, 90).distinct().limit(5).toArray();
        Arrays.stream(lotteryRandomNumbers).forEach(System.out::println);
        return lotteryRandomNumbers;
    }
}
