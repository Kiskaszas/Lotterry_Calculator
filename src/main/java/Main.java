import calculation.WinnerCalculator;

import java.util.concurrent.TimeUnit;

public class Main {

    private static final String WINNER_CALCULATION_TIME_TEXT = "Winners calculation time is ";
    private static final String SECOND_TEXT = " second.";

    private static final String INPUT_FILE_MISSING_TEXT = "Input file is bad.";

    public static void main(String[] args) {
        try {
            WinnerCalculator winnerCalculator = new WinnerCalculator(args[0]);
            winnerCalculator.generateLotteryNumbers();
            final long startTime = System.currentTimeMillis();
            winnerCalculator.startCalculation();
            final long endTime = System.currentTimeMillis();
            System.out.println(WINNER_CALCULATION_TIME_TEXT
                    + TimeUnit.MILLISECONDS.toSeconds(endTime - startTime)
                    + SECOND_TEXT);
        } catch (ArrayIndexOutOfBoundsException ex){
            throw new ArrayIndexOutOfBoundsException(INPUT_FILE_MISSING_TEXT);
        }
    }

}
