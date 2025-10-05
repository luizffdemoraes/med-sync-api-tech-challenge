package br.com.fiap.postech.medsync.scheduling.domain.entities;

import br.com.fiap.postech.medsync.scheduling.application.dtos.CreateAppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.domain.enums.AppointmentStatus;
import br.com.fiap.postech.medsync.scheduling.domain.enums.AppointmentType;

import java.time.LocalDateTime;

public class Appointment {
    private Long id;
    private Long patientUserId;
    private String patientEmail;
    private Long doctorUserId;
    private LocalDateTime appointmentDate;
    private AppointmentStatus status;
    private AppointmentType type;
    private String notes;
    private Integer durationMinutes;
    private boolean urgent;
    private String cancellationReason;
    // ADICIONAR CAMPOS CLÍNICOS INDIVIDUAIS
    private String chiefComplaint;
    private String diagnosis;
    private String prescription;
    private String clinicalNotes;
    private Long updatedBy;

    public static Appointment fromCreateDTO(CreateAppointmentDTO request) {
        Appointment appointment = new Appointment();
        appointment.setPatientUserId(request.getPatientUserId());
        appointment.setPatientEmail(request.getPatientEmail());
        appointment.setDoctorUserId(request.getDoctorUserId());
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setType(
                request.getType() != null
                        ? AppointmentType.valueOf(request.getType())
                        : null
        );
        appointment.setDurationMinutes(request.getDurationMinutes());
        appointment.setNotes(request.getNotes());
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        // Campos opcionais - PARA CRIAÇÃO, OS CAMPOS CLÍNICOS SERÃO SEMPRE NULL
        appointment.setUrgent(false);
        appointment.setCancellationReason(null);

        // ✅ CORREÇÃO: Campos clínicos sempre null na criação
        appointment.setChiefComplaint(null);
        appointment.setDiagnosis(null);
        appointment.setPrescription(null);
        appointment.setClinicalNotes(null);
        appointment.setUpdatedBy(null);

        return appointment;
    }

    // Getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPatientUserId() { return patientUserId; }
    public void setPatientUserId(Long patientUserId) { this.patientUserId = patientUserId; }

    public String getPatientEmail() {
        return patientEmail;
    }
    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public Long getDoctorUserId() { return doctorUserId; }
    public void setDoctorUserId(Long doctorUserId) { this.doctorUserId = doctorUserId; }

    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }

    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }

    public AppointmentType getType() { return type; }
    public void setType(AppointmentType type) { this.type = type; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public boolean isUrgent() { return urgent; }
    public void setUrgent(boolean urgent) { this.urgent = urgent; }

    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }

    public String getChiefComplaint() { return chiefComplaint; }
    public void setChiefComplaint(String chiefComplaint) { this.chiefComplaint = chiefComplaint; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { this.prescription = prescription; }

    public String getClinicalNotes() { return clinicalNotes; }
    public void setClinicalNotes(String clinicalNotes) { this.clinicalNotes = clinicalNotes; }

    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }

    // Método para completar a consulta
    public void complete() {
        this.status = AppointmentStatus.COMPLETED;
    }

    // Método para cancelar a consulta
    public void cancel(String cancellationReason) {
        this.status = AppointmentStatus.CANCELLED;
        this.cancellationReason = cancellationReason;
    }

    // ATUALIZAR MÉTODO addMedicalData
    public void addMedicalData(String chiefComplaint, String diagnosis, String prescription,
                               String clinicalNotes, Long updatedBy) {
        this.chiefComplaint = chiefComplaint;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.clinicalNotes = clinicalNotes;
        this.updatedBy = updatedBy;
    }
}
