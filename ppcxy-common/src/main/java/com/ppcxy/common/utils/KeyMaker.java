package com.ppcxy.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.UUID;
import java.util.prefs.Preferences;

public class KeyMaker {
    public static String preferenceKeyStart = "JetBrains";
    public static String preferenceKeyEnd = "PermanentLicense";
    public static String prolongation = "1";
    public static String licServer = "0.0.0.0";
    public static String licServerPort = "0";
    public static String ticketId = "100";
    public static String licserverUrl = "http://" + KeyMaker.licServer + ":" + KeyMaker.licServerPort;


    public static void generate(String product, final String user, String keyDir) {
        generate(Product.valueOf(product), user, keyDir);
    }

    public static void generate(Product product, final String user, String keyDir) {
        final String preferenceKey = getPreferenceKey(product.uuid);
        if (!machineIdExists()) {
            generateMachineid();
        }
        final String machineId = getMachineId();
        final Properties licenseProperties = getLicenseProperties(user, machineId, "0");
        writeLicensePreference(licenseProperties, preferenceKey);
        writeLicenseServerFile("URL:" + licserverUrl, keyDir + "/" + product.name.toLowerCase() + ".key");
    }

    private static String getPreferenceKey(final String productUUID) {
        return KeyMaker.preferenceKeyStart + "." + productUUID + "." + KeyMaker.preferenceKeyEnd;
    }

    private static Properties getLicenseProperties(final String user, final String machineId, final String licenseType) {
        final Properties licenseProperties = new Properties();
        licenseProperties.setProperty("server.url", KeyMaker.licserverUrl);
        licenseProperties.setProperty("prolongation.period", KeyMaker.prolongation);
        licenseProperties.setProperty("machine.id", machineId);
        licenseProperties.setProperty("ticket.id", KeyMaker.ticketId);
        licenseProperties.setProperty("licensee", user);
        final String signature = generateLicenseHash(KeyMaker.licserverUrl, machineId, 0L, Long.parseLong(KeyMaker.prolongation), user, KeyMaker.ticketId, licenseType);
        licenseProperties.setProperty("signature", signature);
        licenseProperties.setProperty("licenseType", licenseType);
        return licenseProperties;
    }

    private static boolean machineIdExists() {
        final String machineId = getMachineId();
        return machineId != null && machineId.length() != 0;
    }

    private static String getMachineId() {
        return Preferences.userRoot().get("JetBrains.UserIdOnMachine", null);
    }

    private static void generateMachineid() {
        Preferences.userRoot().put("JetBrains.UserIdOnMachine", UUID.randomUUID().toString());
    }

    public static String generateLicenseHash(String licserverUrl,
                                             String machineId,
                                             long unkown,
                                             long prolongation,
                                             String user,
                                             String ticketId,
                                             String licenseType) {
        final StringBuilder sb = new StringBuilder();
        sb.append(licserverUrl);
        sb.append(prolongation);
        if (unkown != 0L) {
            sb.append(unkown);
        }
        sb.append(user);
        sb.append(machineId);
        sb.append(ticketId);
        sb.append(licenseType);
        final int i1 = sb.toString().hashCode();
        return Integer.toHexString(i1);
    }

    public static void writeLicensePreference(final Properties localProperties, final String preferenceKey) {
        try {
            Preferences.userRoot().remove(preferenceKey);
            final ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localProperties.store(localByteArrayOutputStream, "License server ticket information. Please do not alter this data");
            localByteArrayOutputStream.close();
            Preferences.userRoot().putByteArray(preferenceKey, localByteArrayOutputStream.toByteArray());
            Preferences.userRoot().flush();
        }
        catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static void writeLicenseServerFile(final String licenseServer, final String filename) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filename);
            fos.write(255);
            fos.write(255);
            writeLicenseServer(fos, licenseServer);
            fos.flush();
        }
        catch (Throwable ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            }
            catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void writeLicenseServer(final OutputStream paramOutputStream, final String paramString) throws IOException {
        for (int i = 0; i < paramString.length(); ++i) {
            final int j = paramString.charAt(i);
            final int k = (byte)(j & 0xFF);
            final int m = (byte)(j >> 8 & 0xFF);
            paramOutputStream.write(k);
            paramOutputStream.write(m);
        }
    }

    public enum Product {
        IDEA        ("49c202d4-ac56-452b-bb84-735056242fb3", "IDEA", "IDEA"),
        RubyMine    ("b27b2de6-cc3c-4e75-a0a6-d3aead9c2d8b", "RubyMine", "RUBYMINE"),
        WebStorm    ("342e66b2-956c-4384-81da-f50365b990e9", "WebStorm", "WEBSTORM"),
        PhpStorm    ("0d85f2cc-b84f-44c7-b319-93997d080ac9", "PhpStorm", "PHPSTORM"),
        PyCharm     ("e8d15448-eecd-440e-bbe9-1e5f754d781b", "PyCharm", "PYCHARM"),
        AppCode     ("8a00c148-759c-4289-80ae-63fe83cb14f9", "AppCode", "APPCODE"),
        DataGrip    ("94ed896e-599e-4e2c-8724-204935e593ff", "DataGrip", "DATAGRIP"),
        CLion       ("cfc7082d-ae43-4978-a2a2-46feb1679405", "CLion", "CLION"),
        Rider       ("c9e1fa2c-9f19-4ad7-935c-481ca0c2d23c", "Rider", "RIDER"),
        GoLand      ("6ca374ac-f547-4984-be94-adb3e47b580c", "GoLand", "GOLAND");

        public String uuid;
        public String name;
        public String code;

        Product(String uuid, String name, String code) {
            this.uuid = uuid;
            this.name = name;
            this.code = code;
        }
    }

    public static void main(String[] args) {
        generate(Product.IDEA, "weep",
                "/Users/weep/Library/Preferences/IntelliJIdea2018.1"
        );
    }
}
