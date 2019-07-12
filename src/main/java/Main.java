import calculation.WinnerCalculator;
import validation.FileValidation;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.function.IntFunction;
import java.util.stream.Stream;

public class Main {

    private static final String WINNER_CALCULATION_TIME_TEXT = "Winners calculation time is ";
    private static final String SECOND_TEXT = " second.";

    private static final String INPUT_FILE_MISSING_TEXT = "Input file is bad or missing.";

    public static void main(String[] args) {
        FileValidation fileValidation = new FileValidation();
        try {
            WinnerCalculator winnerCalculator = new WinnerCalculator(fileValidation.hasInputFile(args)[0]);
            winnerCalculator.generateLotteryNumbers();
            final long startTime = System.currentTimeMillis();
            winnerCalculator.startCalculation();
            final long endTime = System.currentTimeMillis();
            System.out.println(WINNER_CALCULATION_TIME_TEXT
                    + TimeUnit.MILLISECONDS.toSeconds(endTime - startTime)
                    + SECOND_TEXT);
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new ArrayIndexOutOfBoundsException(INPUT_FILE_MISSING_TEXT);
        }
    }

}
