package edu.unimagdalena.microg2.repository;

import edu.unimagdalena.microg2.entities.Sugerencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SugerenciaRepository extends JpaRepository<Sugerencia, Long> {


    List<Sugerencia> findByDescripcionContaining(String descripcion);


}
