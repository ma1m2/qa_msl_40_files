package msl.qa.model;

public record User(
        int id,
        String username,
        String email,
        Name name,
        int age,
        String role,
        boolean active,
        String created) {
}
