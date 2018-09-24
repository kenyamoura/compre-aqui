package com.github.kenyamoura.compreaqui.cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

/*
 * Controlador que mapeia ações do cliente através da API Rest.
 * Todos os outros endpoints estão dentro da URL /clientes.
 */
@RestController
@RequestMapping("/clientes")
public class ClienteControlador {

    private ClienteRepositorio repositorio;

    /*
     * Automaticamente injetamos o repositório de clientes.
     */
    @Autowired
    public ClienteControlador(ClienteRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    /*
     * Endpoint que permite autenticar um cliente.
     * Retorna 403 se o cliente não existir ou a senha não for correta.
     *
     * POST /clientes/autenticar
     */
    @PostMapping("/autenticar")
    public ResponseEntity<Cliente> autenticar(@RequestBody @Valid Credenciais credenciais) {
        Optional<Cliente> clienteOptional = repositorio.findById(credenciais.getEmail());
        if (clienteOptional.isPresent()) {
            final Cliente cliente = clienteOptional.get();
            // Se a senha for igual
            if (cliente.getSenha().equals(credenciais.getSenha())) {
                // Retorna as informações do cliente.
                return ResponseEntity.ok(cliente);
            }
        }

        // Retorna erro 403
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    /*
     * Endpoint que permite criar um cliente.
     * @Valid valida que o cliente está conforme as validações dentro da classe.
     * Retorna 201, se sucesso.
     * Retorna 409, se email já existir no banco.
     *
     * POST /clientes/
     */
    @PostMapping("/")
    public ResponseEntity<Cliente> cadastrar(@RequestBody @Valid Cliente novoCliente) {

        // se existir, retorna erro.
        boolean existe = repositorio.existsById(novoCliente.getEmail());
        if (existe) {
            // erro 409
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Se não existir no banco, salva novo cliente e código 201.
        Cliente clienteSalvo = repositorio.save(novoCliente);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteSalvo);
    }

    /*
     * Endpoint que permite recuperar um cliente pelo email.
     * Retorna 404 se não o cliente não existir.
     *
     * GET /clientes/{email}
     */
    @GetMapping("/{email}")
    public ResponseEntity<Cliente> recuperar(@PathVariable String email) {
        final Optional<Cliente> clienteOptional = repositorio.findById(email);
        if (clienteOptional.isPresent()) {
            return ResponseEntity.ok(clienteOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    /*
     * Endpoint que permite apagar um cliente pelo email.
     * Retorna 404 se não o cliente não existir.
     *
     * DELETE /clientes/{email}
     */
    @DeleteMapping("/{email}")
    public ResponseEntity<Cliente> apagar(@PathVariable String email) {
        Optional<Cliente> clienteOptional = repositorio.findById(email);
        if (clienteOptional.isPresent()) {
            repositorio.delete(clienteOptional.get());
            return ResponseEntity.noContent().build();
        }

        // Se não existir, retorna erro 404.
        return ResponseEntity.notFound().build();
    }


    /*
     * Endpoint que permite atualizar um cliente pelo email.
     * Retorna 404 se não o cliente não existir previamente no banco.
     *
     * PUT /clientes/{email}
     */
    @PutMapping("/{email}")
    public ResponseEntity<Cliente> atualizar(@PathVariable String email, @RequestBody @Valid Cliente cliente) {
        // Se existir no banco, salva
        boolean existe = repositorio.existsById(email);
        if (existe) {
            repositorio.save(cliente);
            return ResponseEntity.status(HttpStatus.OK).body(cliente);
        }

        // se não existir, retorna erro
        return ResponseEntity.notFound().build();
    }
}
