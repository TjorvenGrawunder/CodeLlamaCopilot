package org.example.codellamacopilot.completionutil;

import org.apache.http.annotation.Experimental;

import java.util.ArrayList;
import java.util.List;

@Experimental
public class CompletionPropositionsStorage {
    private static List<String> propositions = new ArrayList<>();

    public static void setPropositions(List<String> propositions) {
        CompletionPropositionsStorage.propositions = propositions;
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
}
