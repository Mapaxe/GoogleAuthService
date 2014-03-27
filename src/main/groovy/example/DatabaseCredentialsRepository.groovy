package example

import com.warrenstrange.googleauth.ICredentialRepository
import com.warrenstrange.googleauth.NoSuchUserException
import groovy.sql.Sql
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

import java.sql.ResultSet

////////////////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2014, Suncorp Metway Limited. All rights reserved.
//
// This is unpublished proprietary source code of Suncorp Metway Limited.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
////////////////////////////////////////////////////////////////////////////////

@Repository
class DatabaseCredentialsRepository implements ICredentialRepository {

    @Autowired Sql sql

    @Override
    String getSecretKey(String userId) {
        def result = sql.firstRow("""
            select secret_key
            from credential
            where user_id = ?""", userId)
        if (result == null) {
            throw new NoSuchUserException("No user with id ${userId}")
        }
        return result.secret_key
    }

    @Override
    void saveUserCredentials(String userName,
            String secretKey,
            int validationCode,
            List<Integer> scratchCodes) {

        sql.query("select count(*) from credential where user_id = ${userName}") { ResultSet rs ->
            rs.next()
            if (rs.getInt(1) == 0) {
                sql.executeInsert("""
                    insert into credential (
                        user_id,
                        secret_key,
                        validation_code,
                        scratch_codes)
                    values (${userName}, ${secretKey}, ${validationCode}, ${scratchCodes.join(",")})""")

            } else {
                sql.executeUpdate("""
                    update credential
                        set secret_key = ${secretKey},
                        set validation_code = ${validationCode},
                        set scratch_codes = ${scratchCodes.join(",")}
                    where user_id = ${userName}""")

            }

        }
    }
}
