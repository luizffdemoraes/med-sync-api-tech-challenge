package br.com.fiap.postech.medsync.scheduling.infrastructure.controllers;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.CancelAppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.CreateAppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.UpdateAppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.usecases.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final CreateAppointmentUseCase createAppointmentUseCase;
    private final UpdateAppointmentUseCase updateAppointmentUseCase;
    private final GetAppointmentUseCase getAppointmentUseCase;
    private final ListAppointmentsUseCase listAppointmentsUseCase;
    private final CancelAppointmentUseCase cancelAppointmentUseCase;

    public AppointmentController(CreateAppointmentUseCase createAppointmentUseCase,
                                 UpdateAppointmentUseCase updateAppointmentUseCase,
                                 GetAppointmentUseCase getAppointmentUseCase,
                                 ListAppointmentsUseCase listAppointmentsUseCase,
                                 CancelAppointmentUseCase cancelAppointmentUseCase) {
        this.createAppointmentUseCase = createAppointmentUseCase;
        this.updateAppointmentUseCase = updateAppointmentUseCase;
        this.getAppointmentUseCase = getAppointmentUseCase;
        this.listAppointmentsUseCase = listAppointmentsUseCase;
        this.cancelAppointmentUseCase = cancelAppointmentUseCase;
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody CreateAppointmentDTO request) {
        AppointmentDTO appointment = createAppointmentUseCase.execute(request);
        return ResponseEntity.ok(appointment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable Long id,
                                                            @RequestBody UpdateAppointmentDTO request) {
        AppointmentDTO appointment = updateAppointmentUseCase.execute(id, request);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointment(@PathVariable Long id) {
        AppointmentDTO appointment = getAppointmentUseCase.execute(id);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping
    public ResponseEntity<Page<AppointmentDTO>> listAppointments(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) String status,
            @PageableDefault Pageable pageable) {

        Page<AppointmentDTO> appointments = listAppointmentsUseCase.execute(patientId, doctorId, status, pageable);
        return ResponseEntity.ok(appointments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppointmentDTO> cancelAppointment(@PathVariable Long id,
                                                            @RequestBody CancelAppointmentDTO request) {
        AppointmentDTO appointment = cancelAppointmentUseCase.execute(id, request);
        return ResponseEntity.ok(appointment);
    }
}