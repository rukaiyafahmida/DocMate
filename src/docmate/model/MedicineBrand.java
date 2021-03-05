package docmate.model;

public class MedicineBrand {

    private int medicineId;
    private String brandName;
    private int genericId;
    private String genericName;
    private int manufacturerId;
    private String manufacturerName;
    private String strength;
    private String dosageForm;

    public MedicineBrand() {
    }

    public MedicineBrand(int medicineId, String brandName, int genericId, String genericName, int manufacturerId,
                         String manufacturerName, String strength, String dosageForm) {
        this.medicineId = medicineId;
        this.brandName = brandName;
        this.genericId = genericId;
        this.genericName = genericName;
        this.manufacturerId = manufacturerId;
        this.manufacturerName = manufacturerName;
        this.strength = strength;
        this.dosageForm = dosageForm;
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

    public int getGenericId() {
        return genericId;
    }

    public void setGenericId(int genericId) {
        this.genericId = genericId;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public int getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "medicineId=" + medicineId +
                ", brandName='" + brandName + '\'' +
                ", genericId=" + genericId +
                ", genericName='" + genericName + '\'' +
                ", manufacturerId=" + manufacturerId +
                ", manufacturerName='" + manufacturerName + '\'' +
                ", strength='" + strength + '\'' +
                ", dosageForm='" + dosageForm + '\'' +
                '}';
    }
}
