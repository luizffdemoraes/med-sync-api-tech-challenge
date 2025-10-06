package br.com.fiap.postech.medsync.scheduling.infrastructure.controllers;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.CancelAppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.CreateAppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.MedicalDataRequestDTO;
import br.com.fiap.postech.medsync.scheduling.application.usecases.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final CreateAppointmentUseCase createAppointmentUseCase;
    private final AddMedicalDataUseCase addMedicalDataUseCase;
    private final CompleteAppointmentUseCase completeAppointmentUseCase;
    private final GetAppointmentUseCase getAppointmentUseCase;
    private final ListAppointmentsUseCase listAppointmentsUseCase;
    private final CancelAppointmentUseCase cancelAppointmentUseCase;

    public AppointmentController(CreateAppointmentUseCase createAppointmentUseCase,
                                 AddMedicalDataUseCase addMedicalDataUseCase,
                                 CompleteAppointmentUseCase completeAppointmentUseCase,
                                 GetAppointmentUseCase getAppointmentUseCase,
                                 ListAppointmentsUseCase listAppointmentsUseCase,
                                 CancelAppointmentUseCase cancelAppointmentUseCase) {
        this.createAppointmentUseCase = createAppointmentUseCase;
        this.addMedicalDataUseCase = addMedicalDataUseCase;
        this.completeAppointmentUseCase = completeAppointmentUseCase;
        this.getAppointmentUseCase = getAppointmentUseCase;
        this.listAppointmentsUseCase = listAppointmentsUseCase;
        this.cancelAppointmentUseCase = cancelAppointmentUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE')")
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody @Valid CreateAppointmentDTO request) {
        AppointmentDTO appointment = createAppointmentUseCase.execute(request);
        return ResponseEntity.ok(appointment);
    }

    @PatchMapping("/{id}/medical-data")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<AppointmentDTO> addMedicalData(@PathVariable Long id,
                                                         @RequestBody @Valid MedicalDataRequestDTO request) {
        AppointmentDTO appointment = addMedicalDataUseCase.execute(id, request);
        return ResponseEntity.ok(appointment);
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<AppointmentDTO> completeAppointment(@PathVariable Long id,
                                                              @RequestParam Long updatedBy) {
        AppointmentDTO appointment = completeAppointmentUseCase.execute(id, updatedBy);
        return ResponseEntity.ok(appointment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id,
                                                  @RequestBody @Valid CancelAppointmentDTO request) {
        cancelAppointmentUseCase.execute(id, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE')")
    public ResponseEntity<AppointmentDTO> getAppointment(@PathVariable Long id) {
        AppointmentDTO appointment = getAppointmentUseCase.execute(id);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE')")
    public ResponseEntity<Page<AppointmentDTO>> listAppointments(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) String status,
            @PageableDefault Pageable pageable) {

        Page<AppointmentDTO> appointments = listAppointmentsUseCase.execute(patientId, doctorId, status, pageable);
        return ResponseEntity.ok(appointments);
    }
}