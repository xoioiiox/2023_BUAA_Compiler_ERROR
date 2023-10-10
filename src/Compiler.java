import error.ErrorTable;
import error.SymbolTable;
import io.ParserOutput;
import lexer.Lexer;
import lexer.LexerIterator;
import parser.CompUnit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;

public class Compiler {
    public static void main(String[] args) {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream("testfile.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        /*try {
            PrintStream printStream = new PrintStream("error.txt");
            System.setOut(printStream); //将sout重定向到文件输出
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }*/
        Lexer lexer = new Lexer(inputStream);
        LexerIterator iterator = new LexerIterator(lexer.getTokens());
        SymbolTable symbolTable = new SymbolTable(null);
        CompUnit compUnit = new CompUnit(iterator, symbolTable);
        compUnit.parseCompUnit();
        try {
            PrintStream printStream = new PrintStream("error.txt");
            System.setOut(printStream); //将sout重定向到文件输出
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ErrorTable.outPut();
        //ParserOutput.Print();
    }
}