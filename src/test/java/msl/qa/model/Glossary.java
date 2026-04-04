package msl.qa.model;

import com.google.gson.annotations.SerializedName;

public record Glossary(
        String title,
        @SerializedName("ID")
        Integer id,
        GlossaryInner glossary) {
}
