package com.morgan.andy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.morgan.andy.repository.UserRepository;
import com.morgan.andy.service.MailService;
import com.morgan.andy.service.UserService;
import com.morgan.andy.web.rest.vm.ModelVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * REST controller for managing a created model
 */
@RestController
@RequestMapping("/api")
public class SimulateResource {

    private final Logger log = LoggerFactory.getLogger(SimulateResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private MailService mailService;

    private ModelVM exampleModel;

    /**
     * POST  /register : register the user.
     *
     * @param modelVM the managed user View Model
     * @param request the HTTP request
     * @return the ResponseEntity with status 201 (Created) if the user is registered or 400 (Bad Request) if the login or e-mail is already in use
     */
    @RequestMapping(value = "/simulate",
                    method = RequestMethod.POST,
                    produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Timed
    public ResponseEntity<?> simulate(@Valid @RequestBody ModelVM modelVM, HttpServletRequest request) {
        log.info("Received a model: ", modelVM);
        exampleModel = modelVM;
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/load",
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ModelVM> load() {
        return new ResponseEntity<ModelVM>(exampleModel, HttpStatus.OK);
    }
}
