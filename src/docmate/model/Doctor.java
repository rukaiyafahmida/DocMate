package docmate.model;

import java.io.Serializable;

public class Doctor implements Serializable {

    private static Doctor doctor;

    private int id;
    private String name;
    private String email;
    private String phone;
    private String password;

    private Doctor() {
    }

    public static Doctor getInstance() {

        if (doctor == null) {
            doctor = new Doctor();
        }

        return doctor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDoctor(Doctor doctor) {
        this.id = doctor.getId();
        this.name = doctor.getName();
        this.email = doctor.getEmail();
        this.phone = doctor.getPhone();
        this.password = doctor.getPassword();
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
