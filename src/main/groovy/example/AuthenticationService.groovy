package example
import com.warrenstrange.googleauth.GoogleAuthenticator
import com.warrenstrange.googleauth.NoSuchUserException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import static org.springframework.http.HttpStatus.FORBIDDEN
import static org.springframework.http.HttpStatus.OK
////////////////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2014, Suncorp Metway Limited. All rights reserved.
//
// This is unpublished proprietary source code of Suncorp Metway Limited.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
////////////////////////////////////////////////////////////////////////////////

// http://tools.ietf.org/html/rfc6238

@Controller
@RequestMapping("/authentication")
class AuthenticationService {

    @Autowired GoogleAuthenticator authenticator

    @ResponseBody
    @RequestMapping(value = "/{userId}/{code}", method = RequestMethod.GET)
    ResponseEntity authenticate(@PathVariable String userId, @PathVariable int code) {
        try {
            def result = authenticator.authorizeUser(userId, code)
            return result ? new ResponseEntity(OK) : new ResponseEntity(FORBIDDEN)
        } catch (NoSuchUserException e) {
            return new ResponseEntity(FORBIDDEN)
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    ResponseEntity register(@PathVariable String userId) {
        def key = authenticator.createCredentials(userId)
        return new ResponseEntity(key, HttpStatus.CREATED)
    }

}
