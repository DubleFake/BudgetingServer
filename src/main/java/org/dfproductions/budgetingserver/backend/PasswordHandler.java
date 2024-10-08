package org.dfproductions.budgetingserver.backend;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHandler {

    // Method to hash the password with a generated salt and return both as a single string
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        // Generate a random salt
        byte[] salt = generateSalt();

        // Hash the password with the salt
        byte[] hashedPassword = hash(password, salt);

        // Combine the salt (in hexadecimal) and the hashed password (in Base64)
        String saltAsString = bytesToHex(salt);
        String hashedPasswordAsString = Base64.getEncoder().encodeToString(hashedPassword);

        // Return the salt and the hashed password, separated by a colon
        return saltAsString + ":" + hashedPasswordAsString;
    }

    // Method to generate a salt using SecureRandom
    private static byte[] generateSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];  // 16 bytes salt
        sr.nextBytes(salt);
        return salt;
    }

    // Method to hash the password with the salt using SHA-256
    private static byte[] hash(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);  // Add the salt to the hash input
        return md.digest(password.getBytes());  // Hash the password
    }

    // Method to convert byte array to a hexadecimal string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // Method to convert a hexadecimal string back to a byte array
    private static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }

    public static boolean verifyPassword(String password, String storedHash) throws NoSuchAlgorithmException {
        // Split the stored salt and hash
        String[] parts = storedHash.split(":");
        byte[] salt = hexToBytes(parts[0]);  // Convert the salt back from hex
        byte[] storedHashedPassword = Base64.getDecoder().decode(parts[1]);

        // Hash the input password with the same salt
        byte[] hashedPassword = hash(password, salt);

        // Compare the hashes
        return MessageDigest.isEqual(hashedPassword, storedHashedPassword);
    }

    public static String generatePassword(int length) {

        SecureRandom random = new SecureRandom();
        String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String LOWER = "abcdefghijklmnopqrstuvwxyz";
        String DIGITS = "0123456789";
        String SPECIAL = "!@#$%^&*()-_+=<>?/{}~|";
        String ALL = UPPER + LOWER + DIGITS + SPECIAL;

        StringBuilder password = new StringBuilder(length);

        // Ensure at least one character from each category
        password.append(UPPER.charAt(random.nextInt(UPPER.length())));
        password.append(LOWER.charAt(random.nextInt(LOWER.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        // Fill the remaining characters randomly from all categories
        for (int i = 4; i < length; i++) {
            password.append(ALL.charAt(random.nextInt(ALL.length())));
        }

        // Shuffle to avoid predictable patterns (optional but recommended)
        return shuffleString(password.toString(), random);
    }

    private static String shuffleString(String input, SecureRandom random) {
        char[] characters = input.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            int randomIndex = random.nextInt(characters.length);
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }
        return new String(characters);
    }

}
