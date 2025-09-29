package br.com.fiap.postech.medsync.history.infrastructure.resolvers;

import br.com.fiap.postech.medsync.history.application.dtos.responses.MedicalRecordResponse;
import br.com.fiap.postech.medsync.history.application.usecases.GetAppointmentsByStatusUseCase;
import br.com.fiap.postech.medsync.history.application.usecases.GetMedicalRecordByAppointmentIdUseCase;
import br.com.fiap.postech.medsync.history.application.usecases.GetPatientHistoryUseCase;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MedicalRecordQueryResolver {

    private final GetMedicalRecordByAppointmentIdUseCase getMedicalRecordByAppointmentIdUseCase;
    private final GetPatientHistoryUseCase getPatientHistoryUseCase;
    private final GetAppointmentsByStatusUseCase getAppointmentsByStatusUseCase;

    public MedicalRecordQueryResolver(GetMedicalRecordByAppointmentIdUseCase getMedicalRecordByAppointmentIdUseCase, GetPatientHistoryUseCase historyUC,
                                      GetAppointmentsByStatusUseCase appointmentsUC) {
        this.getMedicalRecordByAppointmentIdUseCase = getMedicalRecordByAppointmentIdUseCase;
        this.getPatientHistoryUseCase = historyUC;
        this.getAppointmentsByStatusUseCase = appointmentsUC;
    }

    @QueryMapping
    public List<MedicalRecordResponse> getPatientHistory(@Argument("patientUserId") Long patientUserId) {
        return getPatientHistoryUseCase.execute(patientUserId);
    }

    @QueryMapping
    public List<MedicalRecordResponse> getAppointmentsByStatus(
            @Argument("patientId") Long patientId,
            @Argument("status") String status) {
        return getAppointmentsByStatusUseCase.execute(patientId, status);
    }

    @QueryMapping
    public MedicalRecordResponse getMedicalRecordByAppointmentId(@Argument("appointmentId") Long appointmentId) {
        return getMedicalRecordByAppointmentIdUseCase.execute(appointmentId);
    }
}