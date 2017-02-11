package com.morgan.andy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * @author Andy Morgan (ajm87)
 */
@Entity
@Table(name = "user_achievements")
public class UserAchievements {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_achievement_id")
    private Long userAchievementId;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "achievement_key")
    private Achievement achievement;

    @Column(name = "progress")
    private Long progress;

    @Column(name = "has_unlocked")
    private boolean hasUnlocked;

    public UserAchievements(User user, Achievement achievement, Long progress, boolean hasUnlocked) {
        this.user = user;
        this.achievement = achievement;
        this.progress = progress;
        this.hasUnlocked = hasUnlocked;
    }

    public UserAchievements() {
    }

    public boolean isHasUnlocked() {
        return hasUnlocked;
    }

    public void setHasUnlocked(boolean hasUnlocked) {
        this.hasUnlocked = hasUnlocked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Achievement getAchievement() {
        return achievement;
    }

    public void setAchievement(Achievement achievement) {
        this.achievement = achievement;
    }

    public Long getUserAchievementId() {
        return userAchievementId;
    }

    public void setUserAchievementId(Long userAchievementId) {
        this.userAchievementId = userAchievementId;
    }

    public Long getProgress() {
        return progress;
    }

    public void setProgress(Long progress) {
        this.progress = progress;
    }
}
