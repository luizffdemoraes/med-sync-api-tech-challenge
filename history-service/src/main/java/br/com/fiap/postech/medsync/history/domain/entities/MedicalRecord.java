package br.com.fiap.postech.medsync.history.domain.entities;

import java.time.LocalDateTime;

public class MedicalRecord {
    private Long id;
    private Long appointmentId;
    private Long patientUserId;
    private Long doctorUserId;
    private LocalDateTime appointmentDate;
    private AppointmentStatus status;
    private String type;
    private Integer durationMinutes;
    private String chiefComplaint;
    private String diagnosis;
    private String prescription;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MedicalRecord(
            Long id,
            Long appointmentId,
            Long patientUserId,
            Long doctorUserId,
            LocalDateTime appointmentDate,
            AppointmentStatus status,
            String type,
            String notes
    ) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.patientUserId = patientUserId;
        this.doctorUserId = doctorUserId;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.type = type;
        this.notes = notes;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public MedicalRecord() {
    }

    public MedicalRecord(Long id, Long patientUserId, Long doctorUserId, LocalDateTime appointmentDate, AppointmentStatus appointmentStatus, String type, String notes) {
        this.id = id;
        this.patientUserId = patientUserId;
        this.doctorUserId = doctorUserId;
        this.appointmentDate = appointmentDate;
        this.status = appointmentStatus;
        this.type = type;
        this.notes = notes;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getDoctorUserId() {
        return doctorUserId;
    }

    public void setDoctorUserId(Long doctorUserId) {
        this.doctorUserId = doctorUserId;
    }

    public Long getPatientUserId() {
        return patientUserId;
    }

    public void setPatientUserId(Long patientUserId) {
        this.patientUserId = patientUserId;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return status.toString();
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
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

    public String getChiefComplaint() {
        return chiefComplaint;
    }

    public void setChiefComplaint(String chiefComplaint) {
        this.chiefComplaint = chiefComplaint;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}