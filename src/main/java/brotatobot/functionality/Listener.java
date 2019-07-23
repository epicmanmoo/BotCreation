package brotatobot.functionality;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Listener extends ListenerAdapter {

    private final CommandManager manager;
    private final Logger logger = LoggerFactory.getLogger(Listener.class);

    public Listener(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void onReady(ReadyEvent event) {
        logger.info(String.format("Logged in as %#s", event.getJDA().getSelfUser()));
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        User author = event.getAuthor();
        Message message = event.getMessage();
        String content = message.getContentDisplay();

        if (event.isFromType(ChannelType.TEXT)) {

            Guild guild = event.getGuild();
            TextChannel textChannel = event.getTextChannel();

            logger.info(String.format("(%s)[%s]<%#s>: %s", guild.getName(), textChannel.getName(), author, content));
        } else if (event.isFromType(ChannelType.PRIVATE)) {
            logger.info(String.format("[PRIVATE]<%#s>: %s", author, content));
        }
    }
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e){
        List<TextChannel> ch = e.getGuild().getTextChannelsByName("general", false);
        TextChannel chToSend = ch.get(0);
        chToSend.sendMessage("Welcome to our server " + e.getMember().getAsMention()).queue();
    }

    @Override
    public void onTextChannelCreate(TextChannelCreateEvent e){
        List<Role> mutedrole = e.getGuild().getRolesByName("muted", false);
        e.getChannel().createPermissionOverride(mutedrole.get(0)).setDeny(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION).queue();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String rw = event.getMessage().getContentRaw();

        if (rw.equalsIgnoreCase(Constants.PREFIX + "shutdown") &&
                event.getAuthor().getIdLong() == Constants.OWNER) {
            shutdown(event.getJDA());
            return;
        }

        String prefix = Constants.PREFIXES.computeIfAbsent(event.getGuild().getIdLong(), (L) -> Constants.PREFIX);

        if (!event.getAuthor().isBot() && !event.getMessage().isWebhookMessage() &&
                rw.startsWith(prefix)) {
            manager.handleCommand(event);
        }
    }

    private void shutdown(JDA jda) {
        jda.shutdown();
        System.exit(0);

    }
}
