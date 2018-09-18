package com.github.kenyamoura.compreaqui.cliente;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/*
 * Classe para receber credenciais do usuário.
 */
public class Credenciais {
    
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String senha;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
