package br.com.fiap.postech.medsync.scheduling.application.usecases;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListAppointmentsUseCaseImp implements ListAppointmentsUseCase{
    @Override
    public Page<AppointmentDTO> execute(Long patientId, Long doctorId, String status, Pageable pageable) {
        return null;
    }
}
