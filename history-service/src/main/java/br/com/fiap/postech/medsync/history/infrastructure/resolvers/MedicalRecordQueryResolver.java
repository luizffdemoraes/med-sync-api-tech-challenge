package br.com.fiap.postech.medsync.history.infrastructure.resolvers;

import br.com.fiap.postech.medsync.history.application.dtos.responses.MedicalRecordResponse;
import br.com.fiap.postech.medsync.history.application.usecases.GetAppointmentsByStatusUseCase;
import br.com.fiap.postech.medsync.history.application.usecases.GetMedicalRecordByAppointmentIdUseCase;
import br.com.fiap.postech.medsync.history.application.usecases.GetPatientHistoryUseCase;
import br.com.fiap.postech.medsync.history.infrastructure.exceptions.PatientAccessDeniedException;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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

    @PreAuthorize("hasAnyRole('DOCTOR','NURSE','PATIENT')")
    @QueryMapping
    public List<MedicalRecordResponse> getPatientHistory(
            @Argument("patientUserId") Long patientUserId,
            @AuthenticationPrincipal Jwt jwt) {

        enforcePatient(jwt, patientUserId);
        return getPatientHistoryUseCase.execute(patientUserId);
    }

    @PreAuthorize("hasAnyRole('DOCTOR','NURSE','PATIENT')")
    @QueryMapping
    public List<MedicalRecordResponse> getAppointmentsByStatus(
            @Argument("patientId") Long patientId,
            @Argument("status") String status,
            @AuthenticationPrincipal Jwt jwt) {

        enforcePatient(jwt, patientId);
        return getAppointmentsByStatusUseCase.execute(patientId, status);
    }

    @PreAuthorize("hasAnyRole('DOCTOR','NURSE','PATIENT')")
    @QueryMapping
    public MedicalRecordResponse getMedicalRecordByAppointmentId
            (@Argument("appointmentId") Long appointmentId,
             @AuthenticationPrincipal Jwt jwt) {
        return getMedicalRecordByAppointmentIdUseCase.execute(appointmentId);
    }

    private void enforcePatient(Jwt jwt, Long requestedPatientId) {
        var authorities = jwt.getClaimAsStringList("authorities");
        if (authorities != null && authorities.contains("ROLE_PATIENT")) {
            Number userIdClaim = jwt.getClaim("user_id");
            if (userIdClaim == null) {
                throw new PatientAccessDeniedException("Invalid token: missing user_id claim");
            }
            Long tokenPatientId = userIdClaim.longValue();
            if (!requestedPatientId.equals(tokenPatientId)) {
                throw new PatientAccessDeniedException("Access denied: patient can only access their own data");
            }
        }
    }
}