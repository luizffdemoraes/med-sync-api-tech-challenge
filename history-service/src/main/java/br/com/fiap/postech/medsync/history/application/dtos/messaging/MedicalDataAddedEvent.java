package br.com.fiap.postech.medsync.history.application.dtos.messaging;

public class MedicalDataAddedEvent {
    private final String eventId;
    private final String eventType;
    private final String timestamp;
    private final Long appointmentId;
    private final ClinicalData clinicalData;
    private final Long updatedBy;

    public MedicalDataAddedEvent(String eventId, String eventType, String timestamp, Long appointmentId, ClinicalData clinicalData, Long updatedBy) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.appointmentId = appointmentId;
        this.clinicalData = clinicalData;
        this.updatedBy = updatedBy;
    }

    public String getEventId() { return eventId; }
    public String getEventType() { return eventType; }
    public String getTimestamp() { return timestamp; }
    public Long getAppointmentId() { return appointmentId; }
    public ClinicalData getClinicalData() { return clinicalData; }
    public Long getUpdatedBy() { return updatedBy; }

    public static class ClinicalData {
        private final String chiefComplaint;
        private final String diagnosis;
        private final String prescription;
        private final String notes;

        public ClinicalData(String chiefComplaint, String diagnosis, String prescription, String notes) {
            this.chiefComplaint = chiefComplaint;
            this.diagnosis = diagnosis;
            this.prescription = prescription;
            this.notes = notes;
        }

        public String getChiefComplaint() { return chiefComplaint; }
        public String getDiagnosis() { return diagnosis; }
        public String getPrescription() { return prescription; }
        public String getNotes() { return notes; }
    }
}