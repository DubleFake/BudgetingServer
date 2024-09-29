-- Step 1: Create the database
CREATE DATABASE IF NOT EXISTS budgeting;

-- Switch to the newly created database
USE budgeting;

-- Step 2: Create the users table
CREATE TABLE IF NOT EXISTS users (
    ID INT NOT NULL AUTO_INCREMENT,
    Name VARCHAR(256),
    Email VARCHAR(256) NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT unique_email UNIQUE (Email)  -- Ensure email is unique
);

-- Step 3: Create the passwords table with a UserID column that references the users table
CREATE TABLE IF NOT EXISTS passwords (
    ID INT NOT NULL AUTO_INCREMENT,
    PasswordHash VARCHAR(512),
    PasswordSalt VARCHAR(128),
    UserID INT NOT NULL,  -- Reference to users table
    PRIMARY KEY (ID),
    CONSTRAINT unique_user_password UNIQUE (UserID),  -- Ensure each password is linked to only one user
    CONSTRAINT fk_user_password FOREIGN KEY (UserID) REFERENCES users(ID) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Step 4: Modify the users table to reference PasswordID
ALTER TABLE users
    ADD PasswordID INT NULL,
    ADD CONSTRAINT unique_passwordid UNIQUE (PasswordID),  -- Ensure each user has only one password ID
    ADD CONSTRAINT fk_password FOREIGN KEY (PasswordID) REFERENCES passwords(ID) ON DELETE CASCADE ON UPDATE CASCADE;

-- Step 5: Create the records table with UserID as a foreign key from the users table
CREATE TABLE IF NOT EXISTS records (
    ID INT NOT NULL AUTO_INCREMENT,
    Category VARCHAR(128) NOT NULL,
    Date VARCHAR(50) NOT NULL,
    Price DOUBLE NOT NULL,
    Place VARCHAR(256) NOT NULL,
    Note TEXT,  -- TEXT type for large text storage
    UserID INT NOT NULL,  -- Reference to users table
    PRIMARY KEY (ID),
    CONSTRAINT fk_user FOREIGN KEY (UserID) REFERENCES users(ID) ON DELETE CASCADE ON UPDATE CASCADE
);
