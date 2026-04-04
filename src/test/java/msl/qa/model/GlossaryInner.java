package msl.qa.model;

import com.google.gson.annotations.SerializedName;

public record GlossaryInner(
        @SerializedName("SortAs")
        String sortAs,
        @SerializedName("GlossTerm")
        String glossTerm,
        @SerializedName("Acronym")
        String acronym) {
}
