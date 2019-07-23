package brotatobot.commands.audiocommands;

import brotatobot.objects.ICommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.GuildVoiceState;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

import java.util.List;

public class JoinCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();

        if (audioManager.isConnected()) {
            channel.sendMessage("I'm already connected to a voice channel").queue();
            return;
        }

        GuildVoiceState memberVoiceState = event.getMember().getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("Please join a voice channel first").queue();
            return;
        }

        VoiceChannel vc = memberVoiceState.getChannel();
        Member selfMember = event.getGuild().getSelfMember();

        if (!selfMember.hasPermission(vc, Permission.VOICE_CONNECT)) {
            channel.sendMessageFormat("I am missing permission to join %s", vc).queue();
            return;
        }

        audioManager.openAudioConnection(vc);
        channel.sendMessage("Joining your voice channel").queue();


    }

    @Override
    public String getHelp() {
        return "Makes the bot join your [voice] channel";
    }

    @Override
    public String getInvoke() {
        return "join";
    }
}
