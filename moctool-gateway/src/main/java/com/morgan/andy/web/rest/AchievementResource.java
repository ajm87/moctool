package com.morgan.andy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.morgan.andy.domain.PersistAutomaton;
import com.morgan.andy.domain.User;
import com.morgan.andy.repository.FiniteAutomatonRepository;
import com.morgan.andy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * REST controller for managing a created model
 */
@RestController
@RequestMapping("/api")
public class AchievementResource {

    private final Logger log = LoggerFactory.getLogger(AchievementResource.class);

    @Inject
    private UserRepository userRepository;

    @RequestMapping(value = "/achievements/{achievementKey}/status",
        method = RequestMethod.GET,
        produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Timed
    public ResponseEntity<?> getAchievementStatusForUser(@PathVariable("achievementKey") String achievementKey, HttpServletRequest request, Principal principal) {
        Long currentUserId = userRepository.findOneByLogin(principal.getName()).get().getId();

        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @RequestMapping(value = "/persist/load",
//                    method = RequestMethod.GET)
//    public ResponseEntity<?> load(HttpServletRequest request, Principal principal) {
//        List<PersistAutomaton> saved = finiteAutomatonRepository.findByUserid(userRepository.findOneByLogin(principal.getName()).get().getId());
//        if(saved.isEmpty()) {
//            // no saved automata
//            return new ResponseEntity<Object>(saved, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<Object>(saved, HttpStatus.OK);
//    }

}
