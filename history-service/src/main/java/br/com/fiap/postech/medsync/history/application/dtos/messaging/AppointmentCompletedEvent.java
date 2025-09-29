package br.com.fiap.postech.medsync.history.application.dtos.messaging;

public class AppointmentCompletedEvent {
    private final String eventId;
    private final String eventType;
    private final String timestamp;
    private final Appointment appointment;

    public AppointmentCompletedEvent(String eventId, String eventType, String timestamp, Appointment appointment) {
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
        private final String status;

        public Appointment(Long id, String status) {
            this.id = id;
            this.status = status;
        }

        public Long getId() { return id; }
        public String getStatus() { return status; }
    }
}