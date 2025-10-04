package br.com.fiap.postech.medsync.scheduling.application.usecases;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.UpdateAppointmentDTO;

public class AddMedicalDataUseCaseImp implements AddMedicalDataUseCase {
    @Override
    public AppointmentDTO execute(Long appointmentId, UpdateAppointmentDTO medicalData) {
        return null;
    }
}
