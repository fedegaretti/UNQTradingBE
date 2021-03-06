package ar.unq.unqtrading.services.interfaces

import ar.unq.unqtrading.entities.Accion
import ar.unq.unqtrading.entities.Persona
import org.springframework.stereotype.Service

@Service
interface IPersonaService {
    fun save(persona: Persona): Persona
    fun buy(ordenId: Int, usuarioId: Int): Accion
    fun findById(personaId: Int): Persona
    fun findAcciones(personaId: Int): List<Accion>
    fun login(dni: Long, username: String, password: String): Persona
    fun cargarSaldo(dni: Long, saldo: Int)
    fun findSaldo(usuarioId: Int): Int
}