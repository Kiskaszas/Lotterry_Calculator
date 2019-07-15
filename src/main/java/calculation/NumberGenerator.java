package calculation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class NumberGenerator {

    private static final String LOTTERRY_NUMBERS_TEXT = "Lotterry numbers: ";
    private Integer[] lotteryRandomNumbers;

    private int actualInputNumber;
    private int count;

    public NumberGenerator() {
        count = 0;
    }

    /**
     * Az 5 lottószám generálása 1-től 90-ig.
     * Ha egy generált szám már létezik a tömben akkor generál új számot.
     */
    protected int[] generateLotteryNumbers() {
        System.out.println(LOTTERRY_NUMBERS_TEXT);
        int [] lotteryRandomNumberArray = ThreadLocalRandom.current().ints(1, 90).distinct()
                .limit(5).toArray();
        Arrays.stream(lotteryRandomNumberArray).forEach(System.out::println);
        return lotteryRandomNumberArray;
    }

    protected Integer[] readLotteryNumbersFromConsole() {
        System.out.println(LOTTERRY_NUMBERS_TEXT);
        ArrayList<Integer> lotteryRandomNumberList = new ArrayList<>(5);
        Scanner scanner = new Scanner(System.in);
        while (count < 5) {
            try {
                checkAndAddNumberForArray(lotteryRandomNumberList ,scanner, count);
            } catch (InputMismatchException e) {
                systemOutBadInputMessage();
                scanner.next();
            }
        }
        lotteryRandomNumbers = lotteryRandomNumberList.toArray(new Integer[0]);
        count = 0;
        System.out.println("-----Lotterry numbers read over-----");
        return lotteryRandomNumbers;
    }

    private void checkAndAddNumberForArray(ArrayList<Integer> lotteryRandomNumberList, Scanner scanner, int count){
        actualInputNumber = scanner.nextInt();
        if ((actualInputNumber > 0 && actualInputNumber < 91) && !lotteryRandomNumberList.contains(actualInputNumber)) {
            lotteryRandomNumberList.add(actualInputNumber);
            this.count += 1;
        } else {
            systemOutBadInputMessage();
        }
    }

    private void systemOutBadInputMessage(){
        System.out.println("Bad input, please try again!");
    }
}
