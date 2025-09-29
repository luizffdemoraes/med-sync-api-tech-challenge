package br.com.fiap.postech.medsync.history.application.dtos.messaging;

import java.time.LocalDateTime;

public class AppointmentCreatedEvent {
    private final String eventId;
    private final String eventType;
    private final String timestamp;
    private final Appointment appointment;

    public AppointmentCreatedEvent(String eventId, String eventType, String timestamp, Appointment appointment) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.appointment = appointment;
    }

    public String getEventId() { return eventId; }
    public String getEventType() { return eventType; }
    public String getTimestamp() { return timestamp; }
    public Appointment getAppointment() { return appointment; }

    public static class Appointment {
        private final Long id;
        private final Long patientUserId;
        private final Long doctorUserId;
        private final LocalDateTime appointmentDate;
        private final String status;
        private final String type;
        private final Integer durationMinutes;
        private final String notes;

        public Appointment(Long id, Long patientUserId, Long doctorUserId, LocalDateTime appointmentDate, String status,
                           String type, Integer durationMinutes, String notes) {
            this.id = id;
            this.patientUserId = patientUserId;
            this.doctorUserId = doctorUserId;
            this.appointmentDate = appointmentDate;
            this.status = status;
            this.type = type;
            this.durationMinutes = durationMinutes;
            this.notes = notes;
        }

        public Long getId() { return id; }
        public Long getPatientUserId() { return patientUserId; }
        public Long getDoctorUserId() { return doctorUserId; }
        public LocalDateTime getAppointmentDate() { return appointmentDate; }
        public String getStatus() { return status; }
        public String getType() { return type; }
        public Integer getDurationMinutes() { return durationMinutes; }
        public String getNotes() { return notes; }
    }
}