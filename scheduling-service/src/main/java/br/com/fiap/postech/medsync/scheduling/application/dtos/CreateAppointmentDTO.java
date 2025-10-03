package br.com.fiap.postech.medsync.scheduling.application.dtos;

import java.time.LocalDateTime;

public class CreateAppointmentDTO {
    private Long patientUserId;
    private Long doctorUserId;
    private LocalDateTime appointmentDate;
    private String type;
    private Integer durationMinutes;
    private String notes;

    public CreateAppointmentDTO() {}

    public CreateAppointmentDTO(Long patientUserId, Long doctorUserId, LocalDateTime appointmentDate,
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