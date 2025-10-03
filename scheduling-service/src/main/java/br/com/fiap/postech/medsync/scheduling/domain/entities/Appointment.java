package br.com.fiap.postech.medsync.scheduling.domain.entities;

import br.com.fiap.postech.medsync.scheduling.domain.enums.AppointmentStatus;
import br.com.fiap.postech.medsync.scheduling.domain.enums.AppointmentType;

import java.time.LocalDateTime;

public class Appointment {
    private Long id;
    private Long patientUserId;
    private Long doctorUserId;
    private Long medicalSpecialtyId;
    private LocalDateTime appointmentDate;
    private AppointmentStatus status;
    private AppointmentType type;
    private String notes;
    private Integer durationMinutes; // em minutos
    private boolean urgent;
    private String cancellationReason;
    private String clinicalData;

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

    public Long getMedicalSpecialtyId() {
        return medicalSpecialtyId;
    }

    public void setMedicalSpecialtyId(Long medicalSpecialtyId) {
        this.medicalSpecialtyId = medicalSpecialtyId;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }
}
