package br.com.fiap.postech.medsync.scheduling.domain.gateways;

import br.com.fiap.postech.medsync.scheduling.domain.entities.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AppointmentGateway {
    /**
     * Salva uma nova consulta no banco de dados
     */
    Appointment save(Appointment appointment);

    /**
     * Atualiza uma consulta existente no banco de dados
     */
    Appointment update(Appointment appointment);

    /**
     * Busca uma consulta pelo ID
     */
    Optional<Appointment> findById(Long id);

    /**
     * Lista consultas com filtros opcionais (paginação)
     */
    Page<Appointment> findAll(Long patientId, Long doctorId, String status, Pageable pageable);
}
