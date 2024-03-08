package edu.unimagdalena.microg2.repository;

import edu.unimagdalena.microg2.AbstractIntegrationDBTest;
import edu.unimagdalena.microg2.entities.Mensaje;
import edu.unimagdalena.microg2.entities.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MensajeRepositoryTest extends AbstractIntegrationDBTest {

    MensajeRepository mensajeRepository;
    UsuarioRepository usuarioRepository;

    @Autowired
    public MensajeRepositoryTest(MensajeRepository mensajeRepository, UsuarioRepository usuarioRepository) {
        this.mensajeRepository = mensajeRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @BeforeEach
    void setUp() {
        mensajeRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    void initMensajes() {
        Usuario usuario = Usuario.builder()
                .nombre("Juan")
                .apellido("Perez")
                .username("juanperez")
                .password("123")
                .build();
        Usuario userSaved = usuarioRepository.save(usuario);
        usuarioRepository.flush();

        Mensaje mensaje1 =  Mensaje.builder()
                .creador("Juan")
                .destinatario("Carlos")
                .contenido("Hola, ¿cómo estás?")
                .createAt(LocalDateTime.now())
                .usuario(userSaved)
                .build();

        Mensaje mensaje2 =  Mensaje.builder()
                .creador("Pedro")
                .destinatario("Carlos")
                .contenido("Estoy bien, gracias por preguntar.")
                .createAt(LocalDateTime.now())
                .usuario(userSaved)
                .build();

        mensajeRepository.save(mensaje1);
        mensajeRepository.save(mensaje2);
        mensajeRepository.flush();
    }

    @Test
    @DisplayName("Creando un mensaje para insertarlo " +
            "en la base de datos y luego verificando " +
            "que no sea null por medio de su id")
    void saveMensaje() {
        Usuario usuario = Usuario.builder()
                .nombre("Juan")
                .apellido("Perez")
                .username("juanperez")
                .password("123")
                .build();
        Usuario userSaved = usuarioRepository.save(usuario);
        assertThat(userSaved.getId()).isNotNull();

        Mensaje mensaje =  Mensaje.builder()
                .creador("Juan")
                .destinatario("Carlos")
                .contenido("Hola, ¿cómo estás?")
                .createAt(LocalDateTime.now())
                .usuario(userSaved)
                .build();

        Mensaje mensajeSaved = mensajeRepository.save(mensaje);
        assertThat(mensajeSaved.getId()).isNotNull();
    }

    @Test
    void obtenerTodosLosMensajesDeLaBaseDeDatos() {
        this.initMensajes();
        List<Mensaje> mensajes = mensajeRepository.findAll();
        assertThat(mensajes).hasSize(2);
    }

    @Test
    void buscarMensajePorContenido() {
        this.initMensajes();
        List<Mensaje> mensajes = mensajeRepository.findByContenidoContaining("estás");
        assertThat(mensajes).isNotEmpty();
        assertThat(mensajes.get(0).getContenido()).containsIgnoringCase("estás");
    }

    @Test
    void actualizarMensaje() {
        this.initMensajes();
        Mensaje mensaje = mensajeRepository.findAll().get(0);
        assertThat(mensaje).isNotNull();
        mensaje.setContenido("Nueva contenido del mensaje");
        mensajeRepository.flush();
        Mensaje mensajeUpdated = mensajeRepository.findById(mensaje.getId()).orElse(null);
        assertThat(mensajeUpdated).isNotNull();
        assertThat(mensajeUpdated.getContenido()).isEqualTo("Nueva contenido del mensaje");
    }

    @Test
    void eliminarMensaje() {
        this.initMensajes();
        Mensaje mensaje = mensajeRepository.findAll().get(0);
        assertThat(mensaje).isNotNull();
        Long mensajeId = mensaje.getId();
        mensajeRepository.deleteById(mensajeId);
        Optional<Mensaje> mensajePorId = mensajeRepository.findById(mensajeId);
        assertThat(mensajePorId.isPresent()).isFalse();
    }
}
