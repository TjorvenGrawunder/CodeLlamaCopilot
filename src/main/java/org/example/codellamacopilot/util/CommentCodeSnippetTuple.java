package org.example.codellamacopilot.util;

/**
 * Tuple of a comment and a code snippet.
 */
public class CommentCodeSnippetTuple {
    private String comment;
    private CodeSnippet codeSnippet;

    public CommentCodeSnippetTuple(String commentary, CodeSnippet codeSnippet) {
        this.comment = commentary;
        this.codeSnippet = codeSnippet;
    }

    public String getComment() {
        return comment;
    }

    public CodeSnippet getCodeSnippet() {
        return codeSnippet;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCodeSnippet(CodeSnippet codeSnippet) {
        this.codeSnippet = codeSnippet;
    }

}
