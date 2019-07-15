package calculation;

import validation.FileValidation;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class WinnerCalculator {

    private int[] lotteryRandomNumbers;
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
        lotteryRandomNumbers = new NumberGenerator().generateLotteryNumbers();
    }

//    /**
//        A beadott fájl vizsgálálása.
//        Előszört a fájl hosszát méri le és ha nem nagyobb mint 10 millió akkor elkezdődik a sorok vizsgálása.
//     */
//    private void checkInputFile() {
//        System.out.println(CHECKING_TEXT);
//        try {
//            Files.readAllLines(fileValidation.getFilePath(inputFilePath), StandardCharsets.US_ASCII)
//                    .forEach(line -> fileValidation.checkLine(line));
//            if (fileValidation.getCountX() > MAX_LINES) {
//                throw new IOException(FILE_EXCEPTION_MESSAGE);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.exit(-1);
//        }
//        System.out.println(READY_TEXT);
//    }

//    /**
//     * Vissza adja Beadott file elérési útját Path objektumként
//     * @param inputFilePath
//     * @return
//     * @throws IOException
//     */
//    private Path getFilePath(String inputFilePath) throws IOException {
//        try {
//            return Files.list(Paths.get("."))
//                    .filter(Files::isRegularFile)
//                    .filter(filePath -> inputFilePath.equals(filePath.getFileName().toString()))
//                    .collect(Collectors.toList()).get(0);
//        } catch (IndexOutOfBoundsException ex){
//            throw new IndexOutOfBoundsException(BAD_INPUT_FILE_PATH_TEXT);
//        }
//    }

//    /**
//     * A sort megn&eacute;zi, hogy a Matcherben a felt&eacute;teleknek megfelel majd levizsg&aacute;lja, hogy nincs benne 90-es sz&aacute;mn&aacute;l nagyobb
//     * vagy 0 vagy ann&aacute;l kissebb.
//     * Illetve nem csak 4 sz&aacute;m van megadva.
//     * vagy 1-es sz&aacute;mn&aacute;l kissebb.
//     * @param line
//     * @return boolean
//     */
//    private boolean checkLine(String line) {
//        countX += 1;
//        if(TAG_REGEX.matcher(line).matches()
//                && line.split("\\D+").length == 5
//                && numberBetweenCheck(line.split("\\D+"))){
//            return true;
//        } else {
//            badLines.add(line);
//            return false;
//        }
//    }

//    /**
//     * Számok megnézése hogy 1 és 90 között van.
//     * @param lineRegexArray
//     * @return
//     */
//    private boolean numberBetweenCheck(String[] lineRegexArray) {
//        return Arrays.stream(lineRegexArray).noneMatch(numbers -> Integer.valueOf(numbers) < MIN_NUMBER)
//                &&
//                Arrays.stream(lineRegexArray).noneMatch(numbers -> Integer.valueOf(numbers) > MAX_NUMBER);
//    }

//    /**
//     * Az 5 lottószám generálása 1-től 90-ig.
//     * Ha egy generált szám már létezik a tömben akkor generál új számot.
//     */
//    public void generateLotteryNumbers() {
//        System.out.println(LOTTERRY_NUMBERS_TEXT);
////        lotteryRandomNumbers = ThreadLocalRandom.current().ints(1, 90).distinct().limit(5).toArray();
//        lotteryRandomNumbers = new int[5];
//        lotteryRandomNumbers[0] = 33;
//        lotteryRandomNumbers[1] = 73;
//        lotteryRandomNumbers[2] = 17;
//        lotteryRandomNumbers[3] = 67;
//        lotteryRandomNumbers[4] = 30;
//        Arrays.stream(lotteryRandomNumbers).forEach(System.out::println);
//    }

    /**
     * A nyertesek összeszámálása a generált számok alapján illetve a kész kalkuláció megjelenítése konzolon.
     */
    public void startCalculation() {
        try {
            Files.readAllLines(fileValidation.getFilePath(inputFilePath), StandardCharsets.US_ASCII)
                    .stream()
                    .filter(line -> !badLines.contains(line))
                    .forEach(this::countWinnersByInputData);
            showCalculatedWinners();
            System.out.println(BAD_DATA_LINE_EXCEPTION + badLines.toString());

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * A file sorának a vizsgálata. Ha talál 5 találatos akkor egy üzenetet kiír a konzolra.
     *
     * @param line
     */
    private void countWinnersByInputData(String line) {
        hitsNumbers = Arrays.stream(lotteryRandomNumbers).filter(value -> line.contains(String.valueOf(value))).count();
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