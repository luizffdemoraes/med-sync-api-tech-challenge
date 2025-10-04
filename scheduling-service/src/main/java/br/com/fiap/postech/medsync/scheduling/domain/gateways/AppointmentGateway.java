package br.com.fiap.postech.medsync.scheduling.domain.gateways;

import br.com.fiap.postech.medsync.scheduling.domain.entities.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
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

    /**
     * Verifica se existe conflito de agendamento para um médico em um horário
     */
    boolean existsByDoctorAndTime(Long doctorId, java.time.LocalDateTime startTime, java.time.LocalDateTime endTime);

    /**
     * Verifica se existe conflito de agendamento para um paciente em um horário
     */
    boolean existsByPatientAndTime(Long patientId, java.time.LocalDateTime startTime, java.time.LocalDateTime endTime);

    /**
     * Busca consultas por paciente
     */
    List<Appointment> findByPatientId(Long patientId);

    /**
     * Busca consultas por médico
     */
    List<Appointment> findByDoctorId(Long doctorId);

    /**
     * Deleta uma consulta pelo ID
     */
    void deleteById(Long id);
}
