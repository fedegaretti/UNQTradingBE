package ar.unq.unqtrading.services

import ar.unq.unqtrading.entities.Accion
import ar.unq.unqtrading.entities.Usuario
import ar.unq.unqtrading.repositories.AccionRepository
import ar.unq.unqtrading.repositories.UsuarioRepository
import ar.unq.unqtrading.services.exceptions.UsuarioNoEncontradoException
import ar.unq.unqtrading.services.interfaces.IUsuarioService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UsuarioService : IUsuarioService {
    @Autowired lateinit var usuarioRepository: UsuarioRepository
    @Autowired lateinit var ordenService: OrdenDeVentaService
    @Autowired lateinit var accionRepository: AccionRepository
    override fun save(usuario: Usuario) : Usuario = usuarioRepository.save(usuario)

    override fun buy(ordenId: Int, usuarioId: Int): Accion {
        var usuario: Usuario = findById(usuarioId)
        var orden = ordenService.findById(ordenId)
        var accion = usuario.buy(orden)
        usuarioRepository.save(usuario)
        return accion
    }

    override fun findById(usuarioId: Int): Usuario {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow{ UsuarioNoEncontradoException("El usuario con Id $usuarioId no existe")}
    }

    override fun findAcciones(usuarioId: Int): List<Accion> {
        return accionRepository.findByUsuarioId(usuarioId)
    }

}