package edu.unimagdalena.microg2.repository;

import edu.unimagdalena.microg2.entities.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {


    List<Mensaje> findByContenidoContaining(String contenido);
}
