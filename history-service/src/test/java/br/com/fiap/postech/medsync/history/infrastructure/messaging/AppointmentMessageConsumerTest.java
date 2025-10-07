package br.com.fiap.postech.medsync.history.infrastructure.messaging;


import br.com.fiap.postech.medsync.history.application.dtos.messaging.AppointmentCancelledEvent;
import br.com.fiap.postech.medsync.history.application.dtos.messaging.AppointmentCompletedEvent;
import br.com.fiap.postech.medsync.history.application.dtos.messaging.AppointmentCreatedEvent;
import br.com.fiap.postech.medsync.history.application.dtos.messaging.MedicalDataAddedEvent;
import br.com.fiap.postech.medsync.history.application.usecases.AddMedicalDataUseCase;
import br.com.fiap.postech.medsync.history.application.usecases.CreateMedicalRecordUseCase;
import br.com.fiap.postech.medsync.history.application.usecases.UpdateAppointmentStatusUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AppointmentMessageConsumerTest {

    private CreateMedicalRecordUseCase createMedicalRecordUseCase;
    private UpdateAppointmentStatusUseCase updateAppointmentStatusUseCase;
    private AddMedicalDataUseCase addMedicalDataUseCase;
    private ObjectMapper objectMapper;
    private AppointmentMessageConsumer consumer;

    @BeforeEach
    void setUp() {
        createMedicalRecordUseCase = mock(CreateMedicalRecordUseCase.class);
        updateAppointmentStatusUseCase = mock(UpdateAppointmentStatusUseCase.class);
        addMedicalDataUseCase = mock(AddMedicalDataUseCase.class);
        objectMapper = new ObjectMapper(); // 🔥 Use ObjectMapper real, não mock
        consumer = new AppointmentMessageConsumer(
                createMedicalRecordUseCase,
                updateAppointmentStatusUseCase,
                addMedicalDataUseCase,
                objectMapper
        );
    }

    @Test
    void testReceiveAppointmentCreated() throws Exception {
        String messageBody = """
            {
                "eventId": "b5d9c7a0-8e2f-4b5a-9c3d-2e1f6a8b4c9d",
                "eventType": "APPOINTMENT_CREATED",
                "timestamp": "2024-03-10T14:30:00Z",
                "appointmentId": 1001,
                "appointment": {
                    "id": 1001,
                    "patientUserId": 101,
                    "doctorUserId": 201,
                    "appointmentDate": "2024-04-01T14:30:00",
                    "status": "SCHEDULED",
                    "type": "CONSULTA",
                    "durationMinutes": 30,
                    "notes": "Primeira consulta de rotina"
                }
            }
            """;

        Message message = new Message(messageBody.getBytes());
        consumer.receive(message);

        verify(createMedicalRecordUseCase).execute(any(AppointmentCreatedEvent.class));
    }

    @Test
    void testReceiveAppointmentCompleted() throws Exception {
        String messageBody = """
            {
                "eventId": "c8e2d5a1-9f3b-4c6d-8e7a-1b3d5f7a9c2e",
                "eventType": "APPOINTMENT_COMPLETED",
                "timestamp": "2024-03-15T15:15:00Z",
                "appointmentId": 1001,
                "appointment": {
                    "id": 1001,
                    "status": "COMPLETED"
                }
            }
            """;

        Message message = new Message(messageBody.getBytes());
        consumer.receive(message);

        verify(updateAppointmentStatusUseCase).execute(any(AppointmentCompletedEvent.class));
    }

    @Test
    void testReceiveMedicalDataAdded() throws Exception {
        String messageBody = """
            {
                "eventId": "d9f3e6b2-0a4c-5d7e-9f1a-2c4e6a8b0d3f",
                "eventType": "MEDICAL_DATA_ADDED",
                "timestamp": "2024-03-15T15:30:00Z",
                "appointmentId": 1001,
                "clinicalData": {
                    "chiefComplaint": "Pressão alta e dor de cabeça frequente",
                    "diagnosis": "Hipertensão arterial estágio 1",
                    "prescription": "Captopril 25mg - 1 comprimido 2x ao dia por 30 dias",
                    "notes": "Paciente orientado sobre redução de sal e atividade física"
                },
                "updatedBy": 201
            }
            """;

        Message message = new Message(messageBody.getBytes());
        consumer.receive(message);

        verify(addMedicalDataUseCase).execute(any(MedicalDataAddedEvent.class));
    }

    @Test
    void testReceiveAppointmentCancelled() throws Exception {
        String messageBody = """
                {
                  "eventId": "e1a4f7c3-1b5d-6e8f-0a2b-3d5f7a9c1e4f",
                  "eventType": "APPOINTMENT_CANCELLED",
                  "timestamp": "2024-03-12T09:00:00Z",
                  "appointmentId": 1001,
                  "appointment": {
                    "id": 1001,
                    "status": "CANCELLED",
                    "cancellationReason": "Paciente adoeceu"
                  }
                }
            """;

        Message message = new Message(messageBody.getBytes());
        consumer.receive(message);

        verify(updateAppointmentStatusUseCase).execute(any(AppointmentCancelledEvent.class));
    }

    @Test
    void testReceiveInvalidEventTypeThrowsException() throws Exception {
        String messageBody = """
            {
                "eventId": "test-id",
                "eventType": "UNKNOWN_EVENT",
                "timestamp": "2024-03-10T14:30:00Z",
                "appointmentId": 1001
            }
            """;

        Message message = new Message(messageBody.getBytes());

        assertThrows(AmqpRejectAndDontRequeueException.class, () -> consumer.receive(message));
    }

    @Test
    void testReceiveMissingEventTypeThrowsException() throws Exception {
        String messageBody = """
            {
                "eventId": "test-id",
                "timestamp": "2024-03-10T14:30:00Z",
                "appointmentId": 1001
            }
            """;

        Message message = new Message(messageBody.getBytes());

        assertThrows(AmqpRejectAndDontRequeueException.class, () -> consumer.receive(message));
    }

    @Test
    void testReceiveJsonParseErrorThrowsException() throws Exception {
        Message message = new Message("invalid json".getBytes());

        assertThrows(AmqpRejectAndDontRequeueException.class, () -> consumer.receive(message));
    }

    @Test
    void testReceiveAppointmentCreatedWithMissingDataThrowsException() throws Exception {
        // Mensagem sem o campo "appointment" necessário
        String messageBody = """
            {
                "eventId": "test-id",
                "eventType": "APPOINTMENT_CREATED",
                "timestamp": "2024-03-10T14:30:00Z",
                "appointmentId": 1001
            }
            """;

        Message message = new Message(messageBody.getBytes());

        assertThrows(AmqpRejectAndDontRequeueException.class, () -> consumer.receive(message));
    }
}