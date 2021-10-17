package eu.pollux28.achievementplates.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import eu.pollux28.achievementplates.AchievementPlates;
import eu.pollux28.achievementplates.advancement.PlayerTrophyManager;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.advancement.Advancement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.Collection;

public class Commands {
    public static void init(){
        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> {
            LiteralArgumentBuilder<ServerCommandSource> pre = CommandManager.literal("achievement_plates")
                    .requires(executor -> executor.hasPermissionLevel(0));
            pre.then(CommandManager.literal("debug").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(4)).executes(commandContext -> {
                return execute((ServerCommandSource) commandContext.getSource());
            }));
            pre.then(CommandManager.literal("claim").executes(commandContext ->{
                return executeC(commandContext.getSource(), commandContext.getSource().getPlayer());
            }));
            dispatcher.register(pre);
        }));

    }

    private static int executeC(ServerCommandSource source, ServerPlayerEntity player) {
        PlayerTrophyManager playerTrophyManager = AchievementPlates.trophyManagers.get(player);
        if(playerTrophyManager !=null){
            playerTrophyManager.giveTrophies(source);
            return 1;
        }else{
            source.sendFeedback(new TranslatableText("achievement_plates.chat.claim_message_no_trophy").styled(style -> style.withColor(Formatting.RED)),false);
            return 1;
        }
    }

    private static int execute(ServerCommandSource commandSource){
        try {
            commandSource.getMinecraftServer().getPlayerManager().getAdvancementTracker(commandSource.getPlayer());
            Collection<Advancement> advancements = MinecraftClient.getInstance().getNetworkHandler().getAdvancementHandler().getManager().getAdvancements();
            advancements.forEach(advancement -> {
                if(advancement.getDisplay() !=null && advancement.getDisplay().shouldShowToast()){
                    commandSource.sendFeedback(new LiteralText(advancement.getId().toString()), false);
                }
            });
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }


        return 1;
    }
}
