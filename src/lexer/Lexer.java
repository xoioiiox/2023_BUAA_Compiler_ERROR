package lexer;

import error.Error;
import error.ErrorTable;
import error.ErrorType;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Lexer {
    InputStream inputStream;
    private final ArrayList<String> lines;
    private int curPos;
    private int lineNum;
    private final Map<String, LexType> map = new HashMap<>();
    private final ArrayList<Token> tokens;
    public Lexer(InputStream inputStream) {
        this.inputStream = inputStream;
        this.lines = new ArrayList<>();
        this.tokens = new ArrayList<>();
        this.curPos = 0;
        this.lineNum = 0;
        defMap();
        getLines();
        analysisLexer();
    }

    private void analysisLexer() {
        Token token;
        while ((token = next()) != null) {
            tokens.add(token);
        }
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    private void defMap() {
        map.put("main", LexType.MAINTK);
        map.put("const", LexType.CONSTTK);
        map.put("int", LexType.INTTK);
        map.put("break", LexType.BREAKTK);
        map.put("continue", LexType.CONTINUETK);
        map.put("if", LexType.IFTK);
        map.put("else", LexType.ELSETK);
        map.put("for", LexType.FORTK);
        map.put("getint", LexType.GETINTTK);
        map.put("printf", LexType.PRINTFTK);
        map.put("return", LexType.RETURNTK);
        map.put("void", LexType.VOIDTK);
    }


    private void getLines() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((inputStream)));
        String line;
        try {
            line = bufferedReader.readLine();
            while (line != null) {
                lines.add(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Token next() {
        char c;
        Token token = null;
        LexType type;
        String content1;
        StringBuilder content = new StringBuilder();
        while ((c = getAChar()) != 0) {
            if (Character.isLetter(c) || c == '_') { //Ident
                while (Character.isLetter(c) || Character.isDigit(c) || c == '_') {
                    content.append(c);
                    c = getAChar();
                }
                retract();
                content1 = new String(content);
                type = map.getOrDefault(content1, LexType.IDENFR);
                token = new Token(type, content1, lineNum);
                break;
            }
            else if (Character.isDigit(c)) { //前导0
                while (Character.isDigit(c)) { //遇到结尾 how？
                    content.append(c);
                    c = getAChar();
                }
                retract();
                content1 = new String(content);
                token = new Token(LexType.INTCON, content1, lineNum);
                break;
            }
            else if (c == '"') {
                content.append(c);
                c = getAChar();
                while (c != '"' && c != 0) { //按理不会读到0
                    content.append(c);
                    c = getAChar();
                }
                content.append(c);
                content1 = new String(content);
                token = new Token(LexType.STRCON, content1, lineNum);
                break;
            }
            else if (c == '!') {
                c = getAChar();
                if (c == '=') {
                    content1 = "!=";
                    token = new Token(LexType.NEQ, content1, lineNum);
                    break;
                }
                else {
                    retract();
                    content1 = "!";
                    token = new Token(LexType.NOT, content1, lineNum);
                    break;
                }
            }
            else if (c == '&') {
                c = getAChar();
                if (c == '&') {
                    content1 = "&&";
                    token = new Token(LexType.AND, content1, lineNum);
                    break;
                }
            }
            else if (c == '|') {
                c = getAChar();
                if (c == '|') {
                    content1 = "||";
                    token = new Token(LexType.OR, content1, lineNum);
                    break;
                }
            }
            else if (c == '+') {
                content1 = "+";
                token = new Token(LexType.PLUS, content1, lineNum);
                break;
            }
            else if (c == '-') {
                content1 = "-";
                token = new Token(LexType.MINU, content1, lineNum);
                break;
            }
            else if (c == '*') {
                content1 = "*";
                token = new Token(LexType.MULT, content1, lineNum);
                break;
            }
            else if (c == '/') { //---------------------
                c = getAChar();
                if (c == '/') {
                    c = getAChar();
                    while (c != '\n' && c != 0) {
                        c = getAChar();
                    }
                }
                else if (c == '*') {
                    while (true) {
                        c = getAChar();
                        while (c != '*' && c != 0) {
                            c = getAChar();
                        }
                        if (c == 0) {
                            break;
                        }
                        c = getAChar();
                        if (c == '/') {
                            break;
                        }
                        else {
                            retract();
                        }
                    }
                }
                else {
                    retract();
                    content1 = "/";
                    token = new Token(LexType.DIV, content1, lineNum);
                    break;
                }
            }
            else if (c == '%') {
                content1 = "%";
                token = new Token(LexType.MOD, content1, lineNum);
                break;
            }
            else if (c == '<') {
                c = getAChar();
                if (c == '=') {
                    content1 = "<=";
                    token = new Token(LexType.LEQ, content1, lineNum);
                    break;
                }
                else {
                    retract();
                    content1 = "<";
                    token = new Token(LexType.LSS, content1, lineNum);
                    break;
                }
            }
            else if (c == '>') {
                c = getAChar();
                if (c == '=') {
                    content1 = ">=";
                    token = new Token(LexType.GEQ, content1, lineNum);
                    break;
                }
                else {
                    retract();
                    content1 = ">";
                    token = new Token(LexType.GRE, content1, lineNum);
                    break;
                }
            }
            else if (c == '=') {
                c = getAChar();
                if (c == '=') {
                    content1 = "==";
                    token = new Token(LexType.EQL, content1, lineNum);
                    break;
                }
                else {
                    retract();
                    content1 = "=";
                    token = new Token(LexType.ASSIGN, content1, lineNum);
                    break;
                }
            }
            else if (c == ';') {
                content1 = ";";
                token = new Token(LexType.SEMICN, content1, lineNum);
                break;
            }
            else if (c == ',') {
                content1 = ",";
                token = new Token(LexType.COMMA, content1, lineNum);
                break;
            }
            else if (c == '(') {
                content1 = "(";
                token = new Token(LexType.LPARENT, content1, lineNum);
                break;
            }
            else if (c == ')') {
                content1 = ")";
                token = new Token(LexType.RPARENT, content1, lineNum);
                break;
            }
            else if (c == '[') {
                content1 = "[";
                token = new Token(LexType.LBRACK, content1, lineNum);
                break;
            }
            else if (c == ']') {
                content1 = "]";
                token = new Token(LexType.RBRACK, content1, lineNum);
                break;
            }
            else if (c == '{') {
                content1 = "{";
                token = new Token(LexType.LBRACE, content1, lineNum);
                break;
            }
            else if (c == '}') {
                content1 = "}";
                token = new Token(LexType.RBRACE, content1, lineNum);
                break;
            }
        }
        return token;
    }

    private char getAChar() {
        char c;
        if (lineNum >= lines.size()) {
            return 0;
        }
        if (curPos == lines.get(lineNum).length()) {
            c = '\n';
            curPos = 0;
            lineNum++;
        }
        else {
            c = lines.get(lineNum).charAt(curPos);
            curPos++;
        }
        return c;
    }

    private void retract() {
        if (curPos != 0) {
            curPos--;
        }
        else {
            if (lineNum > 0) {
                lineNum--;
                curPos = lines.get(lineNum).length();
            }
        }
    }

}
