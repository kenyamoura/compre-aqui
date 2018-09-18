package com.github.kenyamoura.compreaqui.cliente;

import org.springframework.data.repository.CrudRepository;

/*
 * Usa a interface CrudRepository para implementar métodos padrão de CRUD.
 */
public interface ClienteRepositorio extends CrudRepository<Cliente, String> {
}
