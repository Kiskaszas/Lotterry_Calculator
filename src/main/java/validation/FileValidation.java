package validation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileValidation {

    private boolean INPUT_FILE_MISSING;
    private final String INPUT_FILE_MISSING_TEXT = "Input file is bad or missing. only 'q' key is exit.";
    private static final String BAD_INPUT_FILE_PATH_TEXT = "Input file is bad.";
    private static final String CHECKING_TEXT = "Checking database...";
    private static final String FILE_EXCEPTION_MESSAGE = "File is not exceeding 10 million players.";
    private static final String READY_TEXT = "READY";

    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 90;
    private static final int MAX_LINES = 10000000;

    private List<ArrayList<Integer>> linesToArrayList;

    private long countX;

    private static final Pattern TAG_REGEX = Pattern.compile("(\\d{1,2})?(\\d{1,2}( +)?){5}", Pattern.DOTALL);
    private ArrayList<String> badLines;

    public FileValidation() {
        this.badLines = new ArrayList<>();
        this.linesToArrayList = new ArrayList<>();
    }

    public String[] hasInputFile(String[] inputArray) {
        INPUT_FILE_MISSING = (inputArray.length == 0);
        Scanner scanner = new Scanner(System.in);
        while (INPUT_FILE_MISSING) {
            System.out.println(INPUT_FILE_MISSING_TEXT + "\tWrite input file: ");
            String line = scanner.nextLine();
            isExitMark(line);
            inputArray = Stream.concat(Stream.of(line), Arrays.stream(inputArray)).toArray(String[]::new);
            if (inputArray.length == 0){
                INPUT_FILE_MISSING = true;
            } else{
                INPUT_FILE_MISSING = !filePathIsValid(inputArray);
            }

        }

        return inputArray;
    }

    private void isExitMark(String lineText) {
        if (lineText.length() == 1 && lineText.contains("q")) {
            System.exit(-1);
        }
    }

    private Boolean filePathIsValid(String[] inputFilePath) {
        try {
            return Files.list(
                    Paths.get("."))
                    .filter(Files::isRegularFile)
                    .filter(filePath -> inputFilePath[0].equals(filePath.getFileName().toString()))
                    .collect(Collectors.toList()).size() == 1;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * A beadott fájl vizsgálálása.
     * Előszört a fájl hosszát méri le és ha nem nagyobb mint 10 millió akkor elkezdődik a sorok vizsgálása.
     */
    public void checkInputFile(String inputFilePath) {
        System.out.println(CHECKING_TEXT);
        try {
            Files.readAllLines(this.getFilePath(inputFilePath), StandardCharsets.US_ASCII)
                    .forEach(line -> this.checkLine(line));
            if (countX > MAX_LINES) {
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
     *
     * @param inputFilePath
     * @return
     * @throws IOException
     */
    public Path getFilePath(String inputFilePath) throws IOException {
        try {
            return Files.list(Paths.get("."))
                    .filter(Files::isRegularFile)
                    .filter(filePath -> inputFilePath.equals(filePath.getFileName().toString()))
                    .collect(Collectors.toList()).get(0);
        } catch (IndexOutOfBoundsException ex) {
            throw new IndexOutOfBoundsException(BAD_INPUT_FILE_PATH_TEXT);
        }
    }

    /**
     * A sort megn&eacute;zi, hogy a Matcherben a felt&eacute;teleknek megfelel majd levizsg&aacute;lja, hogy nincs benne 90-es sz&aacute;mn&aacute;l nagyobb
     * vagy 0 vagy ann&aacute;l kissebb.
     * Illetve nem csak 4 sz&aacute;m van megadva.
     * vagy 1-es sz&aacute;mn&aacute;l kissebb.
     *
     * @param line
     * @return boolean
     */
    public boolean checkLine(String line) {
        countX += 1;
        if (TAG_REGEX.matcher(line).matches()
                && line.split("\\D+").length == 5
                && numberBetweenCheck(line.split("\\D+"))) {
            linesToArrayList.add(Arrays.asList(line.split("\\s+")).stream()
                    .mapToInt(value -> Integer.valueOf(value)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
            return true;
        } else {
            badLines.add(line);
            return false;
        }
    }

    /**
     * Számok megnézése hogy 1 és 90 között van.
     *
     * @param lineRegexArray
     * @return
     */
    private boolean numberBetweenCheck(String[] lineRegexArray) {
        return Arrays.stream(lineRegexArray).noneMatch(numbers -> Integer.valueOf(numbers) < MIN_NUMBER)
                &&
                Arrays.stream(lineRegexArray).noneMatch(numbers -> Integer.valueOf(numbers) > MAX_NUMBER);
    }

    public List<ArrayList<Integer>> getLinesToArrayList() {
        return linesToArrayList;
    }
}
