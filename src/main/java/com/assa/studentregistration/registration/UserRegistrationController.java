package com.assa.studentregistration.registration;

import com.assa.studentregistration.appuser.AppUserDetail;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/registration")
public class UserRegistrationController {

    private RegistrationService mRegistrationService;

    public String register(@RequestBody RegistrationRequest request){
        return mRegistrationService.register(request);
    }

}
