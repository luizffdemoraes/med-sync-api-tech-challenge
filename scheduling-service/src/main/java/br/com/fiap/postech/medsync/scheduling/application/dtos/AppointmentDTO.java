package br.com.fiap.postech.medsync.scheduling.application.dtos;

import br.com.fiap.postech.medsync.scheduling.domain.entities.Appointment;
import br.com.fiap.postech.medsync.scheduling.domain.enums.AppointmentStatus;
import br.com.fiap.postech.medsync.scheduling.domain.enums.AppointmentType;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class AppointmentDTO {

    private Long id;
    private Long patientUserId;
    private Long doctorUserId;
    private String patientEmail;
    private LocalDateTime appointmentDate;
    private AppointmentStatus status;
    private AppointmentType type;
    private Integer durationMinutes;
    private String notes;
    private String cancellationReason;

    // Clinical data
    private String chiefComplaint;
    private String diagnosis;
    private String prescription;
    private String clinicalNotes;

    private Long updatedBy;

    public AppointmentDTO() {
    }

    public AppointmentDTO(Long id, Long patientUserId, Long doctorUserId, String patientEmail,  LocalDateTime appointmentDate, AppointmentStatus status, AppointmentType type, Integer durationMinutes, String notes, String cancellationReason, String chiefComplaint, String diagnosis, String prescription, String clinicalNotes, Long updatedBy) {
        this.id = id;
        this.patientUserId = patientUserId;
        this.doctorUserId = doctorUserId;
        this.patientEmail = patientEmail;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.type = type;
        this.durationMinutes = durationMinutes;
        this.notes = notes;
        this.cancellationReason = cancellationReason;
        this.chiefComplaint = chiefComplaint;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.clinicalNotes = clinicalNotes;
        this.updatedBy = updatedBy;
    }

    // ... existing code ...
    public static AppointmentDTO fromDomain(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setPatientUserId(appointment.getPatientUserId());
        dto.setPatientEmail(appointment.getPatientEmail());
        dto.setDoctorUserId(appointment.getDoctorUserId());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setStatus(appointment.getStatus());
        dto.setType(appointment.getType());
        dto.setDurationMinutes(appointment.getDurationMinutes());
        dto.setNotes(appointment.getNotes());
        dto.setCancellationReason(appointment.getCancellationReason());

        // ✅ CAMPOS CLÍNICOS CORRETOS
        dto.setChiefComplaint(appointment.getChiefComplaint());
        dto.setDiagnosis(appointment.getDiagnosis());
        dto.setPrescription(appointment.getPrescription());
        dto.setClinicalNotes(appointment.getClinicalNotes());
        dto.setUpdatedBy(appointment.getUpdatedBy());

        return dto;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public AppointmentType getType() {
        return type;
    }

    public void setType(AppointmentType type) {
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

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
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

    public String getClinicalNotes() {
        return clinicalNotes;
    }

    public void setClinicalNotes(String clinicalNotes) {
        this.clinicalNotes = clinicalNotes;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
}