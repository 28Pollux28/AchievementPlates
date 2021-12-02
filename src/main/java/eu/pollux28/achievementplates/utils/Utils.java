package eu.pollux28.achievementplates.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eu.pollux28.achievementplates.AchievementPlates;
import eu.pollux28.achievementplates.advancement.PlayerTrophyManager;
import eu.pollux28.achievementplates.init.ModBlocks;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Utils {

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @NotNull
    public static ItemStack getPlateItemStack(Text playerName, AdvancementDisplay display) {
        NbtCompound nbt1 = new NbtCompound();
        NbtCompound nbt2 = new NbtCompound();
        writeDisplayToNbt(nbt2, display);
        nbt2.putString("player_name", playerName.asString());
        nbt1.put("BlockEntityTag", nbt2);
        ItemStack itemStack = ModBlocks.PLATE_ITEM.getDefaultStack();
        itemStack.setNbt(nbt1);
        itemStack.setCustomName(playerName.shallowCopy().setStyle(Style.EMPTY.withColor(Formatting.AQUA)).append(new LiteralText("'s ").setStyle(Style.EMPTY.withColor(Formatting.GRAY))).append(display.getTitle().shallowCopy().setStyle(Style.EMPTY.withColor(display.getFrame().getTitleFormat()))).append(new LiteralText(" trophy plate").setStyle(Style.EMPTY.withColor(Formatting.GRAY))));
        return itemStack;
    }

    public static void writeDisplayToNbt(NbtCompound nbt, AdvancementDisplay display) {
        NbtCompound nbtCompound = new NbtCompound();
        if (display.getTitle() instanceof TranslatableText title) {
            nbtCompound.putString("display_title", title.getKey());
        } else {
            nbtCompound.putString("display_title", display.getTitle().asString());
        }
        if (display.getDescription() instanceof TranslatableText desc) {
            nbtCompound.putString("display_desc", desc.getKey());
        } else {
            nbtCompound.putString("display_desc", display.getDescription().asString());
        }

        if (display.getBackground() != null) {
            nbtCompound.putString("display_background", display.getBackground().toString());
        }
        nbtCompound.put("display_icon", display.getIcon().writeNbt(new NbtCompound()));
        nbtCompound.putString("display_frame_id", display.getFrame().getId());
        nbtCompound.putBoolean("display_toast", display.shouldShowToast());
        nbtCompound.putBoolean("display_chat", display.shouldAnnounceToChat());
        nbtCompound.putBoolean("display_hidden", display.isHidden());
        nbt.put("display", nbtCompound);
    }

    public static void shouldGiveAdvancement(Map<Advancement, AdvancementProgress> advancementToProgress, ServerPlayerEntity playerEntity, Map<Advancement, Boolean> shouldGive) {
        for (Map.Entry<Advancement, AdvancementProgress> advancementAdvancementProgressEntry : advancementToProgress.entrySet()) {
            if (advancementAdvancementProgressEntry.getValue().isDone()) {
                Advancement advancement = advancementAdvancementProgressEntry.getKey();
                AdvancementDisplay display = advancement.getDisplay();
                if (display != null && display.shouldShowToast()) {
                    boolean flag = true;
                    if (AchievementPlates.CONFIG.useWhitelist) {
                        Map<String, ArrayList<String>> whitelist = AchievementPlates.CONFIG.whitelist;
                        boolean namespace = false;
                        boolean id = false;
                        boolean type = false;
                        if (whitelist.containsKey("namespace") && whitelist.get("namespace").contains(advancement.getId().getNamespace())) {
                            namespace = true;
                        }
                        if (whitelist.containsKey("identifier") && whitelist.get("identifier").contains(advancement.getId().toString())) {
                            id = true;
                        }
                        if (whitelist.containsKey("type") && whitelist.get("type").contains(display.getFrame().getId())) {
                            type = true;
                        }
                        flag = flag && (namespace || id || type);
                    }
                    if (AchievementPlates.CONFIG.useBlacklist && flag) {
                        Map<String, ArrayList<String>> blacklist = AchievementPlates.CONFIG.blacklist;
                        boolean namespace = true;
                        boolean id = true;
                        boolean type = true;
                        if (blacklist.containsKey("namespace") && blacklist.get("namespace").contains(advancement.getId().getNamespace())) {
                            namespace = false;
                        }
                        if (blacklist.containsKey("identifier") && blacklist.get("identifier").contains(advancement.getId().toString())) {
                            id = false;
                        }
                        if (blacklist.containsKey("type") && blacklist.get("type").contains(display.getFrame().getId())) {
                            type = false;
                        }
                        flag = flag && (namespace && id && type);
                    }
                    PlayerTrophyManager playerTrophyManager= PlayerTrophyManager.create(playerEntity);
                    flag = flag&&  !playerTrophyManager.containsTrophy(advancement.getId());
                    shouldGive.put(advancement, flag);
                }
            }
        }
        checkJson(playerEntity, shouldGive);
    }

    public static boolean giveItemToPlayer(ServerPlayerEntity playerEntity, ItemStack itemStack) {
        if (playerEntity.giveItemStack(itemStack)) {
            playerEntity.world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((playerEntity.getRandom().nextFloat() - playerEntity.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            return true;
        } else {
            return false;
        }
    }

    private static Path checkPlayerFile(ServerPlayerEntity playerEntity) {
        UUID uuid = playerEntity.getUuid();
        MinecraftServer server = playerEntity.getServer();
        if (server == null) {
            throw new IllegalStateException("I don't know how you got there...");
        }
        Path path = server.getSavePath(WorldSavePath.ROOT);
        Path path2 = Paths.get(path.toString(), "achievement_plates");
        if (!Files.isDirectory(path2)) {
            path2.toFile().mkdirs();
        }
        Path playerFile = Paths.get(path2.toString(), uuid.toString().toLowerCase(Locale.ROOT) + ".json");
        if (!Files.exists(playerFile)) {
            try {
                Files.createFile(playerFile);
                BufferedWriter writer = new BufferedWriter(new FileWriter(playerFile.toFile()));
                writer.write(gson.toJson(new JsonObject()));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return playerFile;
    }

    public static void checkJson(ServerPlayerEntity playerEntity, Map<Advancement, Boolean> shouldGive) {
        Path playerFile = checkPlayerFile(playerEntity);
        try {
            JsonObject obj = new JsonParser().parse(new FileReader(playerFile.toFile())).getAsJsonObject();
            shouldGive.forEach((advancement, aBoolean) -> {
                if (aBoolean) {
                    String advancementID = advancement.getId().toUnderscoreSeparatedString();
                    if (obj.has(advancementID)) {
                        if (!obj.getAsJsonPrimitive(advancementID).getAsBoolean()) {
//                            obj.addProperty(advancementID, true);
                            shouldGive.replace(advancement, true);
                        }
                        shouldGive.replace(advancement, false);

                    } else {
//                        obj.add(advancementID, new JsonPrimitive(true));
                        shouldGive.replace(advancement, true);
                    }
                }
            });
//            try {
//                BufferedWriter writer = new BufferedWriter(new FileWriter(playerFile.toFile()));
//                writer.write(gson.toJson(obj));
//                writer.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void writeToJson(ServerPlayerEntity playerEntity, JsonObject JsonToAdd) {
        try{
            Path playerFile = checkPlayerFile(playerEntity);
            JsonObject obj = new JsonParser().parse(new FileReader(playerFile.toFile())).getAsJsonObject();
            JsonToAdd.entrySet().forEach(stringJsonElementEntry -> obj.add(stringJsonElementEntry.getKey(),stringJsonElementEntry.getValue()));
            BufferedWriter writer = new BufferedWriter(new FileWriter(playerFile.toFile()));
            writer.write(gson.toJson(obj));
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public static void removeAdvancement(ServerPlayerEntity playerEntity, Set<Identifier> set) {
        PlayerTrophyManager playerTrophyManager = PlayerTrophyManager.create(playerEntity);
        for (Identifier id : set){
            playerTrophyManager.removeTrophy(id);
        }
        UUID uuid = playerEntity.getUuid();
        Path playerFile = checkPlayerFile(playerEntity);
        if (playerFile == null) {
            AchievementPlates.logger.error("Couldn't create file for {} with UUID {}", playerEntity.getName(), uuid);
        }
        try {
            JsonObject obj = new JsonParser().parse(new FileReader(playerFile.toFile())).getAsJsonObject();
            for (Identifier id : set) {
                obj.remove(id.toUnderscoreSeparatedString());
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(playerFile.toFile()));
            writer.write(gson.toJson(obj));
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
