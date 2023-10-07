package error;

public class Error {
    int lineNum;
    ErrorType errorType;

    public Error(int lineNum, ErrorType errorType) {
        this.lineNum = lineNum;
        this.errorType = errorType;
    }
}
