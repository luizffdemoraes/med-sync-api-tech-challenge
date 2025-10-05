package br.com.fiap.postech.medsync.scheduling.application.usecases;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.MedicalDataRequestDTO;
import br.com.fiap.postech.medsync.scheduling.domain.entities.Appointment;
import br.com.fiap.postech.medsync.scheduling.domain.gateways.AppointmentGateway;
import br.com.fiap.postech.medsync.scheduling.infrastructure.messaging.HistoryEventProducer;
import br.com.fiap.postech.medsync.scheduling.infrastructure.messaging.NotificationEventProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AddMedicalDataUseCaseImpTest {

    private AppointmentGateway appointmentGateway;
    private HistoryEventProducer historyEventProducer;
    private NotificationEventProducer notificationEventProducer;
    private AddMedicalDataUseCaseImp useCase;

    @BeforeEach
    void setUp() {
        appointmentGateway = mock(AppointmentGateway.class);
        historyEventProducer = mock(HistoryEventProducer.class);
        notificationEventProducer = mock(NotificationEventProducer.class);
        useCase = new AddMedicalDataUseCaseImp(appointmentGateway, historyEventProducer, notificationEventProducer);
    }

    @Test
    void deveAtualizarConsultaEPublicarEventosQuandoConsultaExistir() {
        Long appointmentId = 1L;
        Appointment existingAppointment = mock(Appointment.class);
        Appointment updatedAppointment = mock(Appointment.class);

        MedicalDataRequestDTO dto = new MedicalDataRequestDTO(
                "Dor de cabeça",
                "Enxaqueca",
                "Paracetamol",
                "Repouso",
                42L
        );

        when(appointmentGateway.findById(appointmentId)).thenReturn(Optional.of(existingAppointment));
        when(appointmentGateway.update(existingAppointment)).thenReturn(updatedAppointment);

        AppointmentDTO result = useCase.execute(appointmentId, dto);

        assertNotNull(result, "Resultado não deve ser nulo");
        verify(appointmentGateway).findById(appointmentId);
        verify(existingAppointment).addMedicalData(
                dto.getChiefComplaint(),
                dto.getDiagnosis(),
                dto.getPrescription(),
                dto.getClinicalNotes(),
                dto.getUpdatedBy()
        );
        verify(appointmentGateway).update(existingAppointment);
        verify(historyEventProducer).publishMedicalDataAdded(appointmentId, dto);
        verify(notificationEventProducer).publishAppointmentEvent(ArgumentMatchers.any(AppointmentDTO.class), eq("UPDATED"));
    }

    @Test
    void deveLancarQuandoConsultaNaoExistir() {
        Long appointmentId = 99L;
        MedicalDataRequestDTO dto = new MedicalDataRequestDTO(null, null, null, null, 1L);

        when(appointmentGateway.findById(appointmentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> useCase.execute(appointmentId, dto));
        verify(appointmentGateway).findById(appointmentId);
        verifyNoMoreInteractions(appointmentGateway, historyEventProducer, notificationEventProducer);
    }
}