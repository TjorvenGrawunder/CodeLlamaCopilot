package org.example.codellamacopilot.persistentconverter;

import com.intellij.util.xmlb.Converter;
import org.example.codellamacopilot.llamaconnection.HuggingFaceRequestFormat;
import org.example.codellamacopilot.llamaconnection.CompletionRequestFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RequestFormatConverter extends Converter<CompletionRequestFormat> {

    @Override
    public @Nullable CompletionRequestFormat fromString(@NotNull String value) {
        if (value.equals("HuggingFace")) {
            return new HuggingFaceRequestFormat();
        }else{
            return null;
        }
    }

    @Override
    public @Nullable String toString(@NotNull CompletionRequestFormat value) {
        return value.toString();
    }
}
