package br.com.fiap.postech.medsync.scheduling.application.usecases;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.domain.entities.Appointment;
import br.com.fiap.postech.medsync.scheduling.domain.gateways.AppointmentGateway;

public class GetAppointmentUseCaseImp implements GetAppointmentUseCase {

    private final AppointmentGateway appointmentGateway;

    public GetAppointmentUseCaseImp(AppointmentGateway appointmentGateway) {
        this.appointmentGateway = appointmentGateway;
    }

    @Override
    public AppointmentDTO execute(Long id) {
        // 1. Busca a consulta pelo ID
        Appointment appointment = appointmentGateway.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));

        // 2. Converte a entidade do domínio para DTO
        return AppointmentDTO.fromDomain(appointment);
    }
}
