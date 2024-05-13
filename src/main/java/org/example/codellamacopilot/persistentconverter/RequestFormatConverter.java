package org.example.codellamacopilot.persistentconverter;

import com.intellij.util.xml.ConvertContext;

import com.intellij.util.xmlb.Converter;
import org.example.codellamacopilot.llamaconnection.HuggingFaceRequestFormat;
import org.example.codellamacopilot.llamaconnection.RequestFormat;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RequestFormatConverter extends Converter<RequestFormat> {

    @Override
    public @Nullable RequestFormat fromString(@NotNull String value) {
        if (value.equals("CodeLlama-7b-hf")) {
            return new HuggingFaceRequestFormat();
        }else{
            return null;
        }
    }

    @Override
    public @Nullable String toString(@NotNull RequestFormat value) {
        return value.toString();
    }
}
