package docmate.util;

import docmate.model.Doctor;

import java.io.*;

public class LoginConfig {

    private final String loginConfigUrl = "src/docmate/login.config";

    private static LoginConfig loginConfig;

    private LoginConfig() {

    }

    public static LoginConfig getInstance() {
        if (loginConfig == null) {
            loginConfig = new LoginConfig();
        }

        return loginConfig;
    }

    public Doctor readLoginConfig() {
        Doctor doctor = null;

        try {
            FileInputStream fileInputStream = new FileInputStream(new File(loginConfigUrl));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            // Reading object from file
            doctor = (Doctor) objectInputStream.readObject();

        } catch (Exception exception) {
            clearLoginConfig();
//            exception.printStackTrace();
        }

        return doctor;
    }

    public void writeLoginConfig(Doctor doctor) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(loginConfigUrl));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            // Writing object to file
            objectOutputStream.writeObject(doctor);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void clearLoginConfig() {
        try {
            // Overwriting the file with new one
            new FileOutputStream(new File(loginConfigUrl));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
