DROP TABLE IF EXISTS Credential;

CREATE TABLE Credential (
    User_ID                    VARCHAR(255),
    Secret_Key                 VARCHAR(255),
    Validation_Code            VARCHAR(255),
    Scratch_Codes              VARCHAR(255)
);
