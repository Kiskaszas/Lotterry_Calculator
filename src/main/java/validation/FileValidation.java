package validation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileValidation {

    private boolean INPUT_FILE_MISSING;
    private final String INPUT_FILE_MISSING_TEXT = "Input file is bad or missing. only 'q' key is exit.";

    public FileValidation() {
    }

    public String[] hasInputFile(String[] inputArray) {
        INPUT_FILE_MISSING = (inputArray.length == 0);
        Scanner scanner = new Scanner(System.in);
        while (INPUT_FILE_MISSING) {
            System.out.println(INPUT_FILE_MISSING_TEXT + "\tWrite input file: ");
            isExitMark(scanner.next());
            inputArray = Stream.concat(Stream.of(scanner.nextLine()), Arrays.stream(inputArray)).toArray(String[]::new);
            INPUT_FILE_MISSING = (inputArray.length == 0 ? true : false);
        }
        INPUT_FILE_MISSING = !filePathIsValid(inputArray[0]);
        return inputArray;
    }

    private void isExitMark(String lineText){
        if (lineText.length() == 1 && lineText.contains("q")) {
            System.exit(-1);
        }
    }

    private Boolean filePathIsValid(String inputFilePath) {
        try {
            return Files.list(
                    Paths.get("."))
                    .filter(Files::isRegularFile)
                    .filter(filePath -> inputFilePath.equals(filePath.getFileName().toString()))
                    .collect(Collectors.toList()).size() == 1;
        } catch (IOException e) {
            return false;
        }
    }
}
