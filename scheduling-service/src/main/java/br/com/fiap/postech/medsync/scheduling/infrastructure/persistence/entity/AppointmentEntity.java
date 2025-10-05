package br.com.fiap.postech.medsync.scheduling.infrastructure.persistence.entity;

import br.com.fiap.postech.medsync.scheduling.domain.entities.Appointment;
import br.com.fiap.postech.medsync.scheduling.domain.enums.AppointmentStatus;
import br.com.fiap.postech.medsync.scheduling.domain.enums.AppointmentType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments", schema = "scheduling")
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ CAMPOS QUE EXISTEM NO BANCO
    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "patient_email", nullable = false)
    private String patientEmail;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate;

    @Column(name = "status")
    private String status;

    @Column(name = "type")
    private String type;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "notes")
    private String notes;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    // ✅ CAMPOS CLÍNICOS
    @Column(name = "chief_complaint")
    private String chiefComplaint;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "prescription")
    private String prescription;

    @Column(name = "clinical_notes")
    private String clinicalNotes;

    @Column(name = "updated_by")
    private Long updatedBy;

    // ✅ CAMPOS DE CONTROLE
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    public AppointmentEntity() {
    }

    public AppointmentEntity(Long patientId, Long id, String patientEmail, Long doctorId, LocalDateTime appointmentDate, String status, String type, Integer durationMinutes, String notes, String cancellationReason, String chiefComplaint, String diagnosis, String prescription, String clinicalNotes, Long updatedBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.patientId = patientId;
        this.id = id;
        this.patientEmail = patientEmail;
        this.doctorId = doctorId;
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
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    // Conversão: Domain -> Entity
    public static AppointmentEntity toEntity(Appointment appointment) {
        if (appointment == null) return null;

        AppointmentEntity entity = new AppointmentEntity();
        entity.setId(appointment.getId());
        entity.setPatientId(appointment.getPatientUserId());
        entity.setPatientEmail(appointment.getPatientEmail()); // ✅ NOVO
        entity.setDoctorId(appointment.getDoctorUserId());
        entity.setAppointmentDate(appointment.getAppointmentDate());
        entity.setStatus(appointment.getStatus() != null ? appointment.getStatus().name() : null);
        entity.setType(appointment.getType() != null ? appointment.getType().name() : null);
        entity.setDurationMinutes(appointment.getDurationMinutes());
        entity.setNotes(appointment.getNotes());
        entity.setCancellationReason(appointment.getCancellationReason());

        // ✅ CAMPOS CLÍNICOS
        entity.setChiefComplaint(appointment.getChiefComplaint());
        entity.setDiagnosis(appointment.getDiagnosis());
        entity.setPrescription(appointment.getPrescription());
        entity.setClinicalNotes(appointment.getClinicalNotes());
        entity.setUpdatedBy(appointment.getUpdatedBy());

        return entity;
    }

    // Conversão: Entity -> Domain
    public Appointment toDomain() {
        Appointment appointment = new Appointment();
        appointment.setId(this.getId());
        appointment.setPatientUserId(this.getPatientId());
        appointment.setPatientEmail(this.getPatientEmail()); // ✅ NOVO
        appointment.setDoctorUserId(this.getDoctorId());
        appointment.setAppointmentDate(this.getAppointmentDate());
        appointment.setStatus(this.getStatus() != null ? AppointmentStatus.valueOf(this.getStatus()) : null);
        appointment.setType(this.getType() != null ? AppointmentType.valueOf(this.getType()) : null);
        appointment.setDurationMinutes(this.getDurationMinutes());
        appointment.setNotes(this.getNotes());
        appointment.setCancellationReason(this.getCancellationReason());

        // ✅ CAMPOS CLÍNICOS
        appointment.setChiefComplaint(this.getChiefComplaint());
        appointment.setDiagnosis(this.getDiagnosis());
        appointment.setPrescription(this.getPrescription());
        appointment.setClinicalNotes(this.getClinicalNotes());
        appointment.setUpdatedBy(this.getUpdatedBy());

        return appointment;
    }


    // Getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
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