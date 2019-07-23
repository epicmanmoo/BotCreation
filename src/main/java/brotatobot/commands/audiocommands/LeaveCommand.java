package brotatobot.commands.audiocommands;

import brotatobot.objects.ICommand;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

import java.util.List;

public class LeaveCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();

        if (!audioManager.isConnected()) {
            channel.sendMessage("I'm not connected to a voice channel.").queue();
            return;
        }

        VoiceChannel vc = audioManager.getConnectedChannel();

        if (!vc.getMembers().contains(event.getMember())) {
            channel.sendMessage("You have to be in the same voice channel to use this command").queue();
            return;
        }

        audioManager.closeAudioConnection();
        channel.sendMessage("Disconnected from the channel.").queue();

    }

    @Override
    public String getHelp() {
        return "Makes the bot leave the [voice] channel";
    }

    @Override
    public String getInvoke() {
        return "leave";
    }
}
