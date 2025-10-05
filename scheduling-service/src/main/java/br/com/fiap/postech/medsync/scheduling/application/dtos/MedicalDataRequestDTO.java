package br.com.fiap.postech.medsync.scheduling.application.dtos;

import jakarta.validation.constraints.NotNull;

public class MedicalDataRequestDTO {
    private String chiefComplaint;
    private String diagnosis;
    private String prescription;
    private String clinicalNotes;
    @NotNull(message = "UpdatedBy is required")
    private Long updatedBy;

    // Construtores, getters e setters
    public MedicalDataRequestDTO() {}

    public MedicalDataRequestDTO(String chiefComplaint, String diagnosis, String prescription,
                                 String clinicalNotes, Long updatedBy) {
        this.chiefComplaint = chiefComplaint;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.clinicalNotes = clinicalNotes;
        this.updatedBy = updatedBy;
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
