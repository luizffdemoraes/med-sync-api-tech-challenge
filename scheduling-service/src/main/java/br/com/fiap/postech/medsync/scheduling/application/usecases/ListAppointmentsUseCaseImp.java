package br.com.fiap.postech.medsync.scheduling.application.usecases;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.domain.entities.Appointment;
import br.com.fiap.postech.medsync.scheduling.domain.gateways.AppointmentGateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListAppointmentsUseCaseImp implements ListAppointmentsUseCase {

    private final AppointmentGateway appointmentGateway;

    public ListAppointmentsUseCaseImp(AppointmentGateway appointmentGateway) {
        this.appointmentGateway = appointmentGateway;
    }

    @Override
    public Page<AppointmentDTO> execute(Long patientId, Long doctorId, String status, Pageable pageable) {
        // 1. Busca as consultas com os filtros aplicados
        Page<Appointment> appointments = appointmentGateway.findAll(patientId, doctorId, status, pageable);

        // 2. Converte as entidades do domínio para DTOs
        return appointments.map(AppointmentDTO::fromDomain);
    }
}