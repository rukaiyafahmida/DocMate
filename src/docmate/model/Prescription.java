package docmate.model;

import java.time.LocalDate;

public class Prescription {

    private int prescriptionId;
    private LocalDate date;
    private int patientId;
    private String patientName;
    private int patientAge;
    private String patientSex;
    private String symptoms;
    private String observation;
    private String tests;
    private String diagnosis;
    private String advice;
    private String visitAgain;

    public Prescription() {
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(int patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientSex() {
        return patientSex;
    }

    public void setPatientSex(String patientSex) {
        this.patientSex = patientSex;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getTests() {
        return tests;
    }

    public void setTests(String tests) {
        this.tests = tests;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public String getVisitAgain() {
        return visitAgain;
    }

    public void setVisitAgain(String visitAgain) {
        this.visitAgain = visitAgain;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "prescriptionId=" + prescriptionId +
                ", date=" + date +
                ", patientId=" + patientId +
                ", patientName='" + patientName + '\'' +
                ", patientAge=" + patientAge +
                ", patientSex='" + patientSex + '\'' +
                ", symptoms='" + symptoms + '\'' +
                ", observation='" + observation + '\'' +
                ", tests='" + tests + '\'' +
                ", diagnosis='" + diagnosis + '\'' +
                ", advice='" + advice + '\'' +
                ", visitAgain='" + visitAgain + '\'' +
                '}';
    }
}
