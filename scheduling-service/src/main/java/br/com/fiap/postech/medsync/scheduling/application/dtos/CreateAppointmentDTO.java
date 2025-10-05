package br.com.fiap.postech.medsync.scheduling.application.dtos;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class CreateAppointmentDTO {
    @NotNull(message = "Patient user ID is required")
    @Positive(message = "Patient user ID must be positive")
    private Long patientUserId;

    @NotBlank(message = "Patient email is required")
    @Email(message = "Patient email must be valid")
    private String patientEmail;

    @NotNull(message = "Doctor user ID is required")
    @Positive(message = "Doctor user ID must be positive")
    private Long doctorUserId;

    @NotNull(message = "Appointment date is required")
    @Future(message = "Appointment date must be in the future")
    private LocalDateTime appointmentDate;

    @NotBlank(message = "Appointment type is required")
    @Pattern(regexp = "^(CONSULTATION|EXAM|SURGERY|FOLLOW_UP|EMERGENCY|ROUTINE_CHECKUP)$",
            message = "Appointment type must be one of: CONSULTATION, EXAM, SURGERY, FOLLOW_UP, EMERGENCY, ROUTINE_CHECKUP")
    private String type;

    @NotNull(message = "Duration is required")
    @Min(value = 15, message = "Duration must be at least 15 minutes")
    @Max(value = 480, message = "Duration cannot exceed 480 minutes (8 hours)")
    private Integer durationMinutes;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;

    public CreateAppointmentDTO() {}

    public CreateAppointmentDTO(Long patientUserId, String patientEmail, Long doctorUserId, LocalDateTime appointmentDate,
                                String type, Integer durationMinutes, String notes) {
        this.patientUserId = patientUserId;
        this.doctorUserId = doctorUserId;
        this.appointmentDate = appointmentDate;
        this.type = type;
        this.durationMinutes = durationMinutes;
        this.notes = notes;
    }

    public Long getPatientUserId() {
        return patientUserId;
    }

    public void setPatientUserId(Long patientUserId) {
        this.patientUserId = patientUserId;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public Long getDoctorUserId() {
        return doctorUserId;
    }

    public void setDoctorUserId(Long doctorUserId) {
        this.doctorUserId = doctorUserId;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}