package br.com.fiap.postech.medsync.scheduling.domain.entities;

import br.com.fiap.postech.medsync.scheduling.application.dtos.CreateAppointmentDTO;
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
    private Integer durationMinutes;
    private boolean urgent;
    private String cancellationReason;
    private String clinicalData;

    public static Appointment fromCreateDTO(CreateAppointmentDTO request) {
        Appointment appointment = new Appointment();
        appointment.setPatientUserId(request.getPatientUserId());
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
        // Campos opcionais
        appointment.setUrgent(false);
        appointment.setCancellationReason(null);
        appointment.setClinicalData(null);
        // Você pode configurar outros campos padrão aqui caso necessário

        return appointment;
    }

    // Getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPatientUserId() { return patientUserId; }
    public void setPatientUserId(Long patientUserId) { this.patientUserId = patientUserId; }

    public Long getDoctorUserId() { return doctorUserId; }
    public void setDoctorUserId(Long doctorUserId) { this.doctorUserId = doctorUserId; }

    public Long getMedicalSpecialtyId() { return medicalSpecialtyId; }
    public void setMedicalSpecialtyId(Long medicalSpecialtyId) { this.medicalSpecialtyId = medicalSpecialtyId; }

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

    public String getClinicalData() { return clinicalData; }
    public void setClinicalData(String clinicalData) { this.clinicalData = clinicalData; }

    public void addMedicalData(String chiefComplaint, String diagnosis, String prescription, String notes) {
        StringBuilder sb = new StringBuilder();
        if (chiefComplaint != null) sb.append("Queixa principal: ").append(chiefComplaint).append("\n");
        if (diagnosis != null) sb.append("Diagnóstico: ").append(diagnosis).append("\n");
        if (prescription != null) sb.append("Prescrição: ").append(prescription).append("\n");
        if (notes != null) sb.append("Notas: ").append(notes).append("\n");
        this.clinicalData = sb.toString();
    }
}
