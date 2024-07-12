package br.com.sample.auth0.domain.usuario;

import jakarta.validation.constraints.NotNull;

public record SignUp(
        @NotNull
        String login,
        @NotNull
        String senha
) {
}
