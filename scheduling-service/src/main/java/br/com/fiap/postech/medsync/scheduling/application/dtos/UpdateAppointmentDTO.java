package br.com.fiap.postech.medsync.scheduling.application.dtos;

import java.time.LocalDateTime;

public class UpdateAppointmentDTO {
    private LocalDateTime appointmentDate;
    private String type;
    private Integer durationMinutes;
    private String notes;

    public UpdateAppointmentDTO() {
    }

    public UpdateAppointmentDTO(LocalDateTime appointmentDate, String type,
                                Integer durationMinutes, String notes) {
        this.appointmentDate = appointmentDate;
        this.type = type;
        this.durationMinutes = durationMinutes;
        this.notes = notes;
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