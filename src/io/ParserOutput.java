package io;

import java.util.ArrayList;

public class ParserOutput {
    private static ArrayList<Output> outputs = new ArrayList<>();
    public ParserOutput() {}

    public static void addOutput(Output output) {
        outputs.add(output);
    }

    public static void Print() {
        for (Output output : outputs) {
            output.Print();
        }
    }
}
