package edu.unimagdalena.microg2.repository;

import edu.unimagdalena.microg2.AbstractIntegrationDBTest;
import edu.unimagdalena.microg2.entities.Sugerencia;
import edu.unimagdalena.microg2.entities.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SugerenciaRepositoryTest extends AbstractIntegrationDBTest {
    SugerenciaRepository sugerenciaRepository;
    UsuarioRepository usuarioRepository;

    @Autowired
    public SugerenciaRepositoryTest(SugerenciaRepository sugerenciaRepository,
                                    UsuarioRepository usuarioRepository) {
        this.sugerenciaRepository = sugerenciaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @BeforeEach
    void setUp() {
        sugerenciaRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    void initSugerencias() {
        Usuario usuario = Usuario.builder()
                .nombre("Juan")
                .apellido("Perez")
                .username("juanperez")
                .password("123")
                .build();
        Usuario userSaved = usuarioRepository.save(usuario);
        usuarioRepository.flush();

        Sugerencia sugerencia1 = Sugerencia.builder()
                .descripcion("Hacer mejoras en la interfaz de usuario")
                .createdAt(LocalDateTime.now())
                .usuarios(userSaved)
                .build();

        Sugerencia sugerencia2 = Sugerencia.builder()
                .descripcion("Agregar una nueva funcionalidad al sistema")
                .createdAt(LocalDateTime.now())
                .usuarios(userSaved)
                .build();

        Sugerencia sugerenciaSaved1 = sugerenciaRepository.save(sugerencia1);
        Sugerencia sugerenciaSaved2 = sugerenciaRepository.save(sugerencia2);
        sugerenciaRepository.flush();
    }

    @Test
    @DisplayName("Creando una sugerencia para insertarla " +
            "en la base de datos y luego verificando " +
            "que no sea null por medio de su id")
    void saveSugerencia() {
        Usuario usuario = Usuario.builder()
                .nombre("Juan")
                .apellido("Perez")
                .username("juanperez")
                .password("123")
                .build();
        Usuario userSaved = usuarioRepository.save(usuario);
        assertThat(userSaved.getId()).isNotNull();

        Sugerencia sugerencia = Sugerencia.builder()
                .descripcion("Actualizar el sistema de notificaciones")
                .createdAt(LocalDateTime.now())
                .usuarios(userSaved)
                .build();

        Sugerencia sugerenciaSaved = sugerenciaRepository.save(sugerencia);
        assertThat(sugerenciaSaved.getId()).isNotNull();
    }

    @Test
    void obtenerTodasLasSugerenciasDeLaBaseDeDatos() {
        this.initSugerencias();
        List<Sugerencia> sugerencias = sugerenciaRepository.findAll();
        assertThat(sugerencias).hasSize(2);
    }

    @Test
    void buscarSugerenciaPorDescripcion() {
        this.initSugerencias();
        List<Sugerencia> sugerencias = sugerenciaRepository.findByDescripcionContaining("mejoras");
        assertThat(sugerencias).isNotEmpty();
        assertThat(sugerencias.get(0).getDescripcion()).containsIgnoringCase("mejoras");
    }

    @Test
    void actualizarSugerencia() {
        this.initSugerencias();
        Sugerencia sugerencia = sugerenciaRepository.findAll().get(0);
        assertThat(sugerencia).isNotNull();
        sugerencia.setDescripcion("Nueva descripción");
        sugerenciaRepository.flush();
        Sugerencia sugerenciaUpdated = sugerenciaRepository.findById(sugerencia.getId()).orElse(null);
        assertThat(sugerenciaUpdated).isNotNull();
        assertThat(sugerenciaUpdated.getDescripcion()).isEqualTo("Nueva descripción");
    }

    @Test
    void eliminarSugerencia() {
        this.initSugerencias();
        Sugerencia sugerencia = sugerenciaRepository.findAll().get(0);
        assertThat(sugerencia).isNotNull();
        Long sugerenciaId = sugerencia.getId();
        sugerenciaRepository.deleteById(sugerenciaId);
        Optional<Sugerencia> sugerenciaPorId = sugerenciaRepository.findById(sugerenciaId);
        assertThat(sugerenciaPorId.isPresent()).isFalse();
    }
}
