package com.morgan.andy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.morgan.andy.config.JHipsterProperties;
import com.morgan.andy.domain.Achievement;
import com.morgan.andy.domain.PersistAutomaton;
import com.morgan.andy.domain.User;
import com.morgan.andy.domain.UserAchievements;
import com.morgan.andy.repository.AchievementRepository;
import com.morgan.andy.repository.FiniteAutomatonRepository;
import com.morgan.andy.repository.UserAchievementsRepository;
import com.morgan.andy.repository.UserRepository;
import org.hibernate.annotations.CreationTimestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing a created model
 */
@RestController
@RequestMapping("/api")
public class AchievementResource {

    private final Logger log = LoggerFactory.getLogger(AchievementResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private AchievementRepository achievementRepository;

    @Inject
    private UserAchievementsRepository userAchievementsRepository;

    @RequestMapping(value = "/achievements/{achievementKey}/unlock",
                    method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<?> unlockAchievementForUser(@PathVariable("achievementKey") String achievementKey, HttpServletRequest request, Principal principal) {
        User currentUser = userRepository.findOneByLogin(principal.getName()).get();
        Set<UserAchievements> userAchievements = currentUser.getAchievements();
        Optional<UserAchievements> achievementMatching = userAchievements.stream().filter(a -> a.getAchievement().getAchievementKey().equals(achievementKey)).findFirst();
        Optional<Achievement> achievement = achievementRepository.findOneByAchievementKey(achievementKey);
        if(!achievementMatching.isPresent()) {
            // no progress yet
            UserAchievements ua = new UserAchievements(currentUser, achievement.get(), achievement.get().getUnlockProgress(), true);
            userAchievements.add(ua);
            userAchievementsRepository.saveAndFlush(ua);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            // some progress yet
            achievementMatching.get().setProgress(achievement.get().getUnlockProgress());
            achievementMatching.get().setHasUnlocked(true);
            userAchievementsRepository.saveAndFlush(achievementMatching.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/achievements/{achievementKey}/progress/{progressToAdd}",
        method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<?> updateAchievementProgressForUser(@PathVariable("achievementKey") String achievementKey, @PathVariable("progressToAdd") Long progressToAdd, HttpServletRequest request, Principal principal) {
        User currentUser = userRepository.findOneByLogin(principal.getName()).get();
        Set<UserAchievements> userAchievements = currentUser.getAchievements();
        Optional<UserAchievements> achievementMatching = userAchievements.stream().filter(a -> a.getAchievement().getAchievementKey().equals(achievementKey)).findFirst();
        Optional<Achievement> achievement = achievementRepository.findOneByAchievementKey(achievementKey);
        if(!achievementMatching.isPresent()) {
            UserAchievements ua = new UserAchievements(currentUser, achievement.get(), progressToAdd, false);
            userAchievements.add(ua);
            userAchievementsRepository.saveAndFlush(ua);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            achievementMatching.get().setProgress(achievementMatching.get().getProgress() + progressToAdd);
            userAchievementsRepository.saveAndFlush(achievementMatching.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/achievements/{achievementKey}/progress",
        method = RequestMethod.GET)
    public ResponseEntity<?> getAchievementProgressForUser(@PathVariable("achievementKey") String achievementKey, HttpServletRequest request, Principal principal) {
        User currentUser = userRepository.findOneByLogin(principal.getName()).get();
        Set<UserAchievements> userAchievements = currentUser.getAchievements();
        Optional<UserAchievements> achievementMatching = userAchievements.stream().filter(a -> a.getAchievement().getAchievementKey().equals(achievementKey)).findFirst();
        Optional<Achievement> achievement = achievementRepository.findOneByAchievementKey(achievementKey);
        Map<String, Long> retMap = new HashMap<>();
        if(!achievementMatching.isPresent()) {
            retMap.put("currentProgress", 0L);
            retMap.put("unlockProgress", achievement.get().getUnlockProgress());
        } else {
            retMap.put("currentProgress", achievementMatching.get().getProgress());
            retMap.put("unlockProgress", achievement.get().getUnlockProgress());
        }
        return new ResponseEntity<>(retMap, HttpStatus.OK);
    }

    @RequestMapping(value = "/achievements/{achievementKey}/status",
        method = RequestMethod.GET,
        produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Timed
    public ResponseEntity<?> getAchievementStatusForUser(@PathVariable("achievementKey") String achievementKey, HttpServletRequest request, Principal principal) {
        User currentUser = userRepository.findOneByLogin(principal.getName()).get();
        Set<UserAchievements> userAchievements = currentUser.getAchievements();
        Map<String, Boolean> retMap = new HashMap<>();
        Optional<UserAchievements> achievementMatching = userAchievements.stream().filter(a -> a.getAchievement().getAchievementKey().equals(achievementKey)).findFirst();
        if(!achievementMatching.isPresent()) {
            retMap.put("hasUnlocked", false);
            return new ResponseEntity<>(retMap, HttpStatus.OK);
        }

        Optional<Achievement> achievement = achievementRepository.findOneByAchievementKey(achievementKey);
        if(achievement.isPresent()) {
            retMap.put("hasUnlocked", achievementMatching.get().isHasUnlocked());
            return new ResponseEntity<>(retMap, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/achievements",
                    method = RequestMethod.GET)
    public ResponseEntity<?> getAllAchievementStatusesForUser(HttpServletRequest request, Principal principal) {
        User currentUser = userRepository.findOneByLogin(principal.getName()).get();
        Set<UserAchievements> userAchievements = currentUser.getAchievements();
        Map<String, Map<String, Object>> retMap = new HashMap<>();
        List<Achievement> achievements = achievementRepository.findAll();
        ArrayList<String> keysSeen = new ArrayList<>();
        userAchievements.forEach(u -> {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("currentProgress", u.getProgress());
            dataMap.put("unlockProgress", u.getAchievement().getUnlockProgress());
            dataMap.put("hasUnlocked", u.isHasUnlocked());
            retMap.put(u.getAchievement().getAchievementKey(), dataMap);
            keysSeen.add(u.getAchievement().getAchievementKey());
        });
        achievements.forEach(a -> {
            if(keysSeen.contains(a.getAchievementKey())) {
                return;
            }
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("currentProgress", 0);
            dataMap.put("unlockProgress", a.getUnlockProgress());
            dataMap.put("hasUnlocked", false);
            retMap.put(a.getAchievementKey(), dataMap);
        });
        return new ResponseEntity<>(retMap, HttpStatus.OK);
    }

}
