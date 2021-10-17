package eu.pollux28.achievementplates.config;
//Code used from Simplex Terrain <https://github.com/SuperCoder7979/simplexterrain>, with permission from SuperCoder79

import com.google.common.collect.Lists;
import eu.pollux28.achievementplates.AchievementPlates;

import java.util.ArrayList;
import java.util.Map;

public class MainConfigData {
    public String configVersion = AchievementPlates.VERSION;
    public Map<String, ArrayList<String>> blacklist= Map.of("namespace", Lists.newArrayList("examplenamespace"),"identifier", Lists.newArrayList("modid:advancement_id"),"type", Lists.newArrayList("task | challenge | goal"));
    public Map<String, ArrayList<String>> whitelist= Map.of("namespace", Lists.newArrayList("examplenamespace"),"identifier", Lists.newArrayList("modid:advancement_id"),"type", Lists.newArrayList("task | challenge | goal"));
    public boolean useBlacklist = false;
    public boolean useWhitelist = false;
    public int tickDelayBetweenClaimMessages = 6000;
}