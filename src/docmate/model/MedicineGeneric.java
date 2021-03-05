package docmate.model;

public class MedicineGeneric {

    private int genericId;
    private String genericName;
    private String therapeuticClass;
    private String indications;
    private String sideEffects;
    private int brands;

    public MedicineGeneric() {

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

    public String getTherapeuticClass() {
        return therapeuticClass;
    }

    public void setTherapeuticClass(String therapeuticClass) {
        this.therapeuticClass = therapeuticClass;
    }

    public String getIndications() {
        return indications;
    }

    public void setIndications(String indications) {
        this.indications = indications;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    public int getBrands() {
        return brands;
    }

    public void setBrands(int brands) {
        this.brands = brands;
    }

    @Override
    public String toString() {
        return "MedicineGeneric{" +
                "genericId=" + genericId +
                ", genericName='" + genericName + '\'' +
                ", therapeuticClass='" + therapeuticClass + '\'' +
                ", indications='" + indications + '\'' +
                ", sideEffects='" + sideEffects + '\'' +
                ", brands=" + brands +
                '}';
    }
}
