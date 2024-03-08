package edu.unimagdalena.microg2.repository;


import edu.unimagdalena.microg2.AbstractIntegrationDBTest;
import edu.unimagdalena.microg2.entities.Partida;
import edu.unimagdalena.microg2.entities.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PartidaRepositoryTest extends AbstractIntegrationDBTest {
    PartidaRepository partidaRepository;
    UsuarioRepository usuarioRepository;

    @Autowired
    public PartidaRepositoryTest(PartidaRepository partidaRepository,
                                 UsuarioRepository usuarioRepository) {
        this.partidaRepository = partidaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @BeforeEach
    void setUp() {
        partidaRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    void initPartidas() {
        Usuario usuario = Usuario.builder()
                .nombre("Juan")
                .apellido("Perez")
                .username("juanperez")
                .password("123")
                .build();
        Usuario userSaved = usuarioRepository.save(usuario);
        usuarioRepository.flush();

        Partida partida1 = Partida.builder()
                .creador("Juan")
                .ciudad("Bogotá")
                .deporte("Fútbol")
                .provincia("Cundinamarca")
                .fecha(LocalDateTime.now())
                .horaComienzo(LocalTime.of(18, 0))
                .horaFinal(LocalTime.of(20, 0))
                .participantes(10)
                .suplentes(5)
                .comentarios("Partida de prueba")
                .usuarios(List.of(userSaved))
                .build();

        Partida partida2 = Partida.builder()
                .creador("Juan")
                .ciudad("Medellín")
                .deporte("Baloncesto")
                .provincia("Antioquia")
                .fecha(LocalDateTime.now())
                .horaComienzo(LocalTime.of(16, 0))
                .horaFinal(LocalTime.of(18, 0))
                .participantes(8)
                .suplentes(3)
                .comentarios("Partida de prueba 2")
                .usuarios(List.of(userSaved))
                .build();

        Partida partidaSaved1 = partidaRepository.save(partida1);
        Partida partidaSaved2 = partidaRepository.save(partida2);
        partidaRepository.flush();
    }

    @Test
    @DisplayName("Creando una partida para insertarla " +
            "en la base de datos y luego verificando " +
            "que no sea null por medio de su id")
    void savePartida() {
        Usuario usuario = Usuario.builder()
                .nombre("Juan")
                .apellido("Perez")
                .username("juanperez")
                .password("123")
                .build();
        Usuario userSaved = usuarioRepository.save(usuario);
        assertThat(userSaved.getId()).isNotNull();

        Partida partida = Partida.builder()
                .creador("Juan")
                .ciudad("Barranquilla")
                .deporte("Tenis")
                .provincia("Atlántico")
                .fecha(LocalDateTime.now())
                .horaComienzo(LocalTime.of(14, 0))
                .horaFinal(LocalTime.of(16, 0))
                .participantes(4)
                .suplentes(2)
                .comentarios("Partida de prueba 3")
                .usuarios(List.of(userSaved))
                .build();

        Partida partidaSaved = partidaRepository.save(partida);
        assertThat(partidaSaved.getId()).isNotNull();
    }

    @Test
    void obtenerTodasLasPartidasDeLaBaseDeDatos() {
        this.initPartidas();
        List<Partida> partidas = partidaRepository.findAll();
        assertThat(partidas).hasSize(2);
    }


    @Test
    void actualizarPartida() {
        this.initPartidas();
        Partida partida = partidaRepository.findAll().get(0);
        assertThat(partida).isNotNull();
        partida.setCreador("Nuevo creador");
        partidaRepository.flush();
        Partida partidaUpdated = partidaRepository.findById(partida.getId()).orElse(null);
        assertThat(partidaUpdated).isNotNull();
        assertThat(partidaUpdated.getCreador()).isEqualTo("Nuevo creador");
    }

    @Test
    void eliminarPartida() {
        this.initPartidas();
        Partida partida = partidaRepository.findAll().get(0);
        assertThat(partida).isNotNull();
        Long partidaId = partida.getId();
        partidaRepository.deleteById(partidaId);
        Optional<Partida> partidaPorId = partidaRepository.findById(partidaId);
        assertThat(partidaPorId.isPresent()).isFalse();
    }
}
