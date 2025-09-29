package br.com.fiap.postech.medsync.history.application.dtos.responses;

import br.com.fiap.postech.medsync.history.domain.entities.MedicalRecord;

import java.time.LocalDateTime;

public class MedicalRecordResponse {
    private Long id;
    private Long appointmentId;
    private Long patientUserId;
    private Long doctorUserId;
    private LocalDateTime appointmentDate;
    private String status;
    private String type;
    private String chiefComplaint;
    private String diagnosis;
    private String prescription;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Construtor vazio
    public MedicalRecordResponse() {}

    // Construtor a partir da entidade de domínio
    public static MedicalRecordResponse fromDomain(MedicalRecord record) {
        MedicalRecordResponse response = new MedicalRecordResponse();
        response.setId(record.getId());
        response.setAppointmentId(record.getAppointmentId());
        response.setPatientUserId(record.getPatientUserId());
        response.setDoctorUserId(record.getDoctorUserId());
        response.setAppointmentDate(record.getAppointmentDate());
        response.setStatus(record.getStatus());
        response.setType(record.getType());
        response.setChiefComplaint(record.getChiefComplaint());
        response.setDiagnosis(record.getDiagnosis());
        response.setPrescription(record.getPrescription());
        response.setNotes(record.getNotes());
        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());
        return response;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChiefComplaint() {
        return chiefComplaint;
    }

    public void setChiefComplaint(String chiefComplaint) {
        this.chiefComplaint = chiefComplaint;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
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
}