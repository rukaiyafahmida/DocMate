package docmate.model;

public class PatientMedicine {

    private int prescriptionId;
    private int medicineId;
    private String dosageForm;
    private String brandName;
    private String strength;
    private String drugDuration;
    private String drugDosages;
    private String drugAdvice;

    public PatientMedicine() {
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getDrugAdvice() {
        return drugAdvice;
    }

    public void setDrugAdvice(String drugAdvice) {
        this.drugAdvice = drugAdvice;
    }

    public String getDrugDosages() {
        return drugDosages;
    }

    public void setDrugDosages(String drugDosages) {
        this.drugDosages = drugDosages;
    }

    public String getDrugDuration() {
        return drugDuration;
    }

    public void setDrugDuration(String drugDuration) {
        this.drugDuration = drugDuration;
    }

    @Override
    public String toString() {
        return "PatientMedicine{" +
                "prescriptionId=" + prescriptionId +
                ", medicineId=" + medicineId +
                ", brandName='" + brandName + '\'' +
                ", dosageForm='" + dosageForm + '\'' +
                ", strength='" + strength + '\'' +
                ", drugAdvice='" + drugAdvice + '\'' +
                ", drugDosages='" + drugDosages + '\'' +
                ", drugDuration='" + drugDuration + '\'' +
                '}';
    }
}

