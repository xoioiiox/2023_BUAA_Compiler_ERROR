package error;

public class Error implements Comparable<Error> {
    int lineNum;
    ErrorType errorType;

    public Error(int lineNum, ErrorType errorType) {
        this.lineNum = lineNum + 1;
        this.errorType = errorType;
    }

    @Override
    public int compareTo(Error o) {
        return Integer.compare(this.lineNum, o.lineNum);
    }
}
