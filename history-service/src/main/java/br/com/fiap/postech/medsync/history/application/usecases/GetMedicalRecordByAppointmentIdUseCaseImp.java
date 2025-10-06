package br.com.fiap.postech.medsync.history.application.usecases;

import br.com.fiap.postech.medsync.history.application.dtos.responses.MedicalRecordResponse;
import br.com.fiap.postech.medsync.history.domain.gateways.MedicalRecordRepositoryGateway;
import br.com.fiap.postech.medsync.history.infrastructure.exceptions.MedicalRecordNotFoundException;
import br.com.fiap.postech.medsync.history.infrastructure.exceptions.PatientAccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class GetMedicalRecordByAppointmentIdUseCaseImp implements GetMedicalRecordByAppointmentIdUseCase {

    private final MedicalRecordRepositoryGateway repositoryGateway;

    public GetMedicalRecordByAppointmentIdUseCaseImp(MedicalRecordRepositoryGateway repositoryGateway) {
        this.repositoryGateway = repositoryGateway;
    }

    @Override
    public MedicalRecordResponse execute(Long appointmentId) {
        var record = repositoryGateway.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical record not found for appointmentId: " + appointmentId));

        enforcePatientOwnership(record.getPatientUserId());
        return MedicalRecordResponse.fromDomain(record);
    }

    private void enforcePatientOwnership(Long recordPatientUserId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) {
            return; // sem JWT ou outro fluxo; config global já exige auth
        }
        var authorities = jwt.getClaimAsStringList("authorities");
        if (authorities == null || !authorities.contains("ROLE_PATIENT")) {
            return; // médicos/enfermeiros têm acesso
        }
        Number userIdClaim = jwt.getClaim("user_id");
        if (userIdClaim == null) {
            throw new PatientAccessDeniedException("Invalid token: missing user_id claim");
        }
        Long tokenPatientId = userIdClaim.longValue();
        if (!recordPatientUserId.equals(tokenPatientId)) {
            throw new PatientAccessDeniedException("Access denied: patient can only access their own data");
        }
    }
}