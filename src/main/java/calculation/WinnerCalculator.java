package calculation;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WinnerCalculator {

    private static final int NUMBERS = 5;
    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 90;

    private int[] lotteryRandomNumbers;
    private HashMap<Integer, Integer> winners;

    private static final String NUMBER_MATCHING_TEXT = "Numbers matching";
    private static final String WINNERS_TEXT = "Winners";
    private static final String READY_TEXT = "READY";
    private static final String FILE_EXCEPTION_MESSAGE = "File is not exceeding 10 million players.";
    private static final String BAD_DATA_LINE_EXCEPTION = "Bad data line at: ";
    private static final String CHECKING_TEXT = "Checking database...";
    private static final String LOTTERRY_NUMBERS_TEXT = "Lotterry numbers: ";
    private static final String BAD_INPUT_FILE_PATH_TEXT = "Input file is bad.";

    private static final String WINNER_DATA_LINE_TEXT = "Winner players number is next ";

    private String inputFilePath;
    private static final int MAX_LINES = 10000000;
    private long count;
    private Long hitsNumbers;

    private static final Pattern TAG_REGEX = Pattern.compile("(\\d{1,2})?(\\d{1,2}( +)?){5}", Pattern.DOTALL);

    public WinnerCalculator(String inputFilePath) {
        this.inputFilePath = inputFilePath;
        winners = new HashMap<>();
        winners.put(2, 0);
        winners.put(3, 0);
        winners.put(4, 0);
        winners.put(5, 0);
        checkInputFile();
    }

    /**
        A beadott fájl vizsgálálása.
        Előszört a fájl hosszát méri le és ha nem nagyobb mint 10 millió akkor elkezdődik a sorok vizsgálása.
     */
    private void checkInputFile() {
        System.out.println(CHECKING_TEXT);
        try {
            count = Files.readAllLines(getFilePath(inputFilePath), StandardCharsets.US_ASCII).stream().count();
            if (count <= MAX_LINES) {
                Files.readAllLines(getFilePath(inputFilePath), StandardCharsets.US_ASCII)
                        .forEach(line -> checkLine(line));
            } else {
                throw new IOException(FILE_EXCEPTION_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.out.println(READY_TEXT);
    }

    /**
     * Vissza adja Beadott file elérési útját Path objektumként
     * @param inputFilePath
     * @return
     * @throws IOException
     */
    private Path getFilePath(String inputFilePath) throws IOException {
        try {
            return Files.list(Paths.get("."))
                    .filter(Files::isRegularFile)
                    .filter(filePath -> inputFilePath.equals(filePath.getFileName().toString()))
                    .collect(Collectors.toList()).get(0);
        } catch (IndexOutOfBoundsException ex){
            throw new IndexOutOfBoundsException(BAD_INPUT_FILE_PATH_TEXT);
        }
    }

    /**
     * A sort megnézi, hogy a Matcherben a feltételeknek megfelel majd levizsgálja, hogy nincs benne 90-es számnál nagyobb
     * vagy 0 vagy annál kissebb.
     * Illetve nem csak 4 szám van megadva.
     * vagy 1-es számnál kissebb.
     * @param line
     * @return
     */
    private boolean checkLine(String line) {

        if(TAG_REGEX.matcher(line).matches()
                && line.split("\\D+").length == 5
                && numberBetweenCheck(line.split("\\D+"))){
            return true;
        } else {
            badDataLine(line);
            return false;
        }

        /*if (TAG_REGEX.matcher(line).matches()) {
            String[] lineRegexArray = line.split("\\D+");
            if (lineRegexArray.length == 5) {
                if(numberBetweenCheck(lineRegexArray)) {
                    return true;
                } else {
                    badDataLine(line);
                }
            } else {
                badDataLine(line);
            }
        } else {
            badDataLine(line);
        }
        return false;*/
    }

    /**
     * Száok megnézése hogy 1 és 90 között van.
     * @param lineRegexArray
     * @return
     */
    private boolean numberBetweenCheck(String[] lineRegexArray) {
        return Arrays.stream(lineRegexArray)
                .filter(numbers -> Integer.valueOf(numbers) < MIN_NUMBER)
                .count() == 0
                &&
                Arrays.stream(lineRegexArray)
                .filter(numbers -> Integer.valueOf(numbers) > MAX_NUMBER)
                .count() == 0;
    }

    /**
     * Ha az adott sor nem fel meg a kritériumnak akkor IOException-t dobunk a megfelelő üzenettel.
     * @param line
     */
    private void badDataLine(String line) {
        try {
            throw new IOException(BAD_DATA_LINE_EXCEPTION + line);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Az 5 lottószám generálása 1-től 90-ig.
     * Ha egy generált szám már létezik a tömben akkor generál új számot.
     */
    public void generateLotteryNumbers() {
        lotteryRandomNumbers = new int[NUMBERS];
        System.out.print(LOTTERRY_NUMBERS_TEXT);
        int number;
        boolean goOut = true;
        int count = 0;
        while (goOut){
            number = (int) (Math.random() * MAX_NUMBER) + MIN_NUMBER;
            int finalNumber = number;
            if (!Arrays.stream(lotteryRandomNumbers).anyMatch(value -> value == finalNumber)) {
                this.lotteryRandomNumbers[count] = number;
                System.out.print(number + " ");
                count++;
            }
            if (count == 5){
                goOut = false;
            }
        }
        System.out.println("\n");
    }

    /**
     * A nyertesek összeszámálása a generált számok alapján illetve a kész kalkuláció megjelenítése konzolon.
     */
    public void startCalculation() {
        try {
            Files.readAllLines(getFilePath(inputFilePath), StandardCharsets.US_ASCII)
                    .forEach(line -> countWinnersByInputData(line));
            showCalculatedWinners();

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * A file sorának a vizsgálata. Ha talál 5 találatos akkor egy üzenetet kiír a konzolra.
     * @param line
     */
    private void countWinnersByInputData(String line) {
        hitsNumbers = Arrays.stream(lotteryRandomNumbers).filter(value -> line.contains(String.valueOf(value))).count();
        if (hitsNumbers > 1) {
            if (hitsNumbers==5) {
                System.out.println(WINNER_DATA_LINE_TEXT+line);
            }
            winners.put(hitsNumbers.intValue(), winners.get(hitsNumbers.intValue()) + 1);
        }
    }

    /**
     * A nyerteseknek a kiírása konzolra az elvárt táblázat szerint.
     */
    private void showCalculatedWinners() {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Integer> keys = new ArrayList<Integer>(winners.keySet());

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