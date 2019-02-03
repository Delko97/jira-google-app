package com.zive.it.controllers;

import com.zive.it.models.User;
import com.zive.it.services.JWTService;
import com.zive.it.services.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private JWTService jwtService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<User> loginAuthentification(@RequestBody Map<String, String> map) throws IOException, NoSuchAlgorithmException {
        ResponseEntity<HashMap<String,Object>> loginResponse = loginService.authenticateUser(map.get("username"), map.get("password"));
        if (loginResponse.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity(loginResponse.getStatusCode());
        }
        loginResponse.getHeaders();
        return new ResponseEntity(new User(map.get("username"), jwtService.createUriWithJwt(loginResponse.getBody())),HttpStatus.OK);
    }

}
