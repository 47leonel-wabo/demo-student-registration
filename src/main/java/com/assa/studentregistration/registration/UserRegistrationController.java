package com.assa.studentregistration.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/registration")
public class UserRegistrationController {

    private final RegistrationService mRegistrationService;

    @Autowired
    public UserRegistrationController(final RegistrationService registrationService) {
        mRegistrationService = registrationService;
    }

    @PostMapping
    public String register(@RequestBody final RegistrationRequest request) {
        return mRegistrationService.register(request);
    }

    @GetMapping(path = "/confirm")
    public String confirmRegistration(@RequestParam(name = "token") final String token){
        return mRegistrationService.confirmToken(token);
    }

}
