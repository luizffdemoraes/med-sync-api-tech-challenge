package br.com.fiap.postech.medsync.history.infrastructure.persistence.entity;

import br.com.fiap.postech.medsync.history.domain.entities.AppointmentStatus;
import br.com.fiap.postech.medsync.history.domain.entities.MedicalRecord;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "medical_records", schema = "history")
public class MedicalRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_id", nullable = false)
    private Long appointmentId;

    @Column(name = "patient_user_id", nullable = false)
    private Long patientUserId;

    @Column(name = "doctor_user_id", nullable = false)
    private Long doctorUserId;

    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "chief_complaint")
    private String chiefComplaint;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "prescription")
    private String prescription;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static MedicalRecordEntity fromDomain(MedicalRecord domain) {
        MedicalRecordEntity entity = new MedicalRecordEntity();
        entity.setAppointmentId(domain.getAppointmentId());
        entity.setPatientUserId(domain.getPatientUserId());
        entity.setDoctorUserId(domain.getDoctorUserId());
        entity.setAppointmentDate(domain.getAppointmentDate());
        entity.setStatus(domain.getStatus() != null ? domain.getStatus().name() : null);
        entity.setType(domain.getType());
        entity.setChiefComplaint(domain.getChiefComplaint());
        entity.setDiagnosis(domain.getDiagnosis());
        entity.setPrescription(domain.getPrescription());
        entity.setNotes(domain.getNotes());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public MedicalRecord toDomain() {
        MedicalRecord domain = new MedicalRecord();
        domain.setAppointmentId(this.getAppointmentId());
        domain.setPatientUserId(this.getPatientUserId());
        domain.setDoctorUserId(this.getDoctorUserId());
        domain.setAppointmentDate(this.getAppointmentDate());
        domain.setStatus(this.getStatus() != null ? AppointmentStatus.valueOf(this.getStatus()) : null);
        domain.setType(this.getType());
        domain.setChiefComplaint(this.getChiefComplaint());
        domain.setDiagnosis(this.getDiagnosis());
        domain.setPrescription(this.getPrescription());
        domain.setNotes(this.getNotes());
        domain.setCreatedAt(this.getCreatedAt());
        domain.setUpdatedAt(this.getUpdatedAt());
        return domain;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

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

    public Optional<MedicalRecord> map(Object toDomain) {
        if (toDomain instanceof MedicalRecordEntity entity) {
            return Optional.ofNullable(entity.toDomain());
        }
        return Optional.empty();
    }
}
