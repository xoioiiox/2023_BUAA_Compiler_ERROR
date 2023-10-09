package error;

import java.util.TreeSet;

public class ErrorTable {
    private static TreeSet<Error> errors = new TreeSet<>();

    public ErrorTable() {}

    public static void addError(Error error) {
        errors.add(error);
    }

    public static void outPut() {
        for (Error error : errors) {
            System.out.println(error.lineNum + " " + error.errorType);
        }
    }
}
