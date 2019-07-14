package calculation;

import validation.FileValidation;
import java.util.*;

public class WinnerCalculator {

    private List<Integer> lotteryRandomNumbers;
    private HashMap<Integer, Integer> winners;

    private static final String NUMBER_MATCHING_TEXT = "Numbers matching";
    private static final String WINNERS_TEXT = "Winners";
    private static final String BAD_DATA_LINE_EXCEPTION = "Bad data lines at: ";

    private static final String WINNER_DATA_LINE_TEXT = "Winner players number is next ";

    private String inputFilePath;
    private Long hitsNumbers;

    private ArrayList<String> badLines;

    private FileValidation fileValidation;

    public WinnerCalculator(String inputFilePath) {
        this.inputFilePath = inputFilePath;
        fileValidation = new FileValidation();
        badLines = new ArrayList<>();
        winners = new HashMap<>();
        winners.put(2, 0);
        winners.put(3, 0);
        winners.put(4, 0);
        winners.put(5, 0);
        fileValidation.checkInputFile(inputFilePath);
        lotteryRandomNumbers = new ArrayList<>();
        lotteryRandomNumbers = Arrays.asList(new NumberGenerator().readLotteryNumbersFromConsole());
    }

    /**
     * A nyertesek összeszámálása a generált számok alapján illetve a kész kalkuláció megjelenítése konzolon.
     */
    public void startCalculation() {
            fileValidation.getLinesToArrayList().stream().filter(integers -> !badLines.contains(integers))
                    .forEach(integers -> countWinnersByInputData(integers));
            showCalculatedWinners();
            System.out.println(BAD_DATA_LINE_EXCEPTION + badLines.toString());
    }

    /**
     * A file sorának a vizsgálata. Ha talál 5 találatos akkor egy üzenetet kiír a konzolra.
     *
     * @param line
     */
    private ArrayList<Integer> lineToIntList = new ArrayList<>();
    private void countWinnersByInputData(ArrayList<Integer> line) {
        hitsNumbers = lotteryRandomNumbers.stream().filter(value -> line.contains(value)).count();
        if (hitsNumbers > 1) {
            if (hitsNumbers == 5) {
                System.out.println(WINNER_DATA_LINE_TEXT + line);
            }
            winners.put(hitsNumbers.intValue(), winners.get(hitsNumbers.intValue()) + 1);
        }
    }

    /**
     * A nyerteseknek a kiírása konzolra az elvárt táblázat szerint.
     */
    private void showCalculatedWinners() {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Integer> keys = new ArrayList<>(winners.keySet());

        stringBuilder
                .append(NUMBER_MATCHING_TEXT)
                .append("\t")
                .append(WINNERS_TEXT)
                .append("\n");
        for (int i = keys.size() - 1; i >= 0; i--) {
            stringBuilder
                    .append(keys.get(i))
                    .append("\t\t\t")
                    .append(winners.get(keys.get(i)))
                    .append("\n");
        }
        System.out.println(stringBuilder.toString());
    }
}