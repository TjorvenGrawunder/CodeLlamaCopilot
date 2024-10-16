package org.example.codellamacopilot.completionutil;

import org.apache.http.annotation.Experimental;
import org.example.codellamacopilot.util.CodeSnippet;

import java.util.ArrayList;
import java.util.List;

@Experimental
public class CompletionPropositionsStorage {
    private static List<String> propositions = new ArrayList<>();
    private static String currentMessage;
    private static String currentLine;
    private static int offset;
    private static boolean valid = false;
    private static boolean isInstruct = false;
    private static CodeSnippet codeSnippet;

    public static void setPropositions(List<String> propositions) {
        CompletionPropositionsStorage.propositions = propositions;
    }

    public static String getCurrentMessage() {
        return currentMessage;
    }

    public static void setCurrentMessage(String currentMessage) {
        CompletionPropositionsStorage.currentMessage = currentMessage;
    }

    public static String getCurrentLine() {
        return currentLine;
    }

    public static void setCurrentLine(String currentLine) {
        CompletionPropositionsStorage.currentLine = currentLine;
    }

    public static void addProposition(String proposition) {
        propositions.add(proposition);
    }

    public static List<String> getPropositions() {
        return propositions;
    }

    public static void clearPropositions() {
        propositions.clear();
    }

    public static boolean isEmpty() {
        return propositions.isEmpty();
    }

    public static boolean isValid() {
        return valid;
    }

    public static void setValid(boolean valid) {
        CompletionPropositionsStorage.valid = valid;
    }

    public static void setOffset(int offset) {
        CompletionPropositionsStorage.offset = offset;
    }

    public static boolean isInstruct() {
        return isInstruct;
    }

    public static void setInstruct(boolean instruct) {
        CompletionPropositionsStorage.isInstruct = instruct;
    }

    public static CodeSnippet getCodeSnippet() {
        return codeSnippet;
    }

    public static void setCodeSnippet(CodeSnippet codeSnippet) {
        CompletionPropositionsStorage.codeSnippet = codeSnippet;
    }
}
