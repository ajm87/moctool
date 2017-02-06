package com.morgan.andy.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author Andy Morgan (ajm87)
 */
@Entity
@Table(name = "achievements")
public class Achievement {

    @Id
    @Column(name = "achievement_key")
    private String achievementKey;

    @Column(name = "achievement_name")
    @NotNull
    private String achievementName;

    @Column(name = "unlock_progress")
    private Long unlockProgress;

    public Long getUnlockProgress() {
        return unlockProgress;
    }

    public void setUnlockProgress(Long unlockProgress) {
        this.unlockProgress = unlockProgress;
    }

    public String getAchievementKey() {
        return achievementKey;
    }

    public void setAchievementKey(String achievementKey) {
        this.achievementKey = achievementKey;
    }

    public String getAchievementName() {
        return achievementName;
    }

    public void setAchievementName(String achievementName) {
        this.achievementName = achievementName;
    }
}
