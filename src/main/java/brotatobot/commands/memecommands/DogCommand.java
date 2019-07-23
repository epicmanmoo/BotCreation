package brotatobot.commands.memecommands;

import brotatobot.objects.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class DogCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member selfMember = event.getGuild().getSelfMember();
        MessageChannel channel = event.getChannel();
        WebUtils.ins.getJSONObject("https://dog.ceo/api/breeds/image/random").async((json) -> {
            String url = json.get("message").asText();
            System.out.println(url);
            MessageEmbed embed = EmbedUtils.embedImage(url).build();

            if (!selfMember.hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
                channel.sendMessage("Sorry, I do not have permission to use this command!\n" +
                        "Please enable me to send embed links to use this command").queue();
                return;
            }
            event.getChannel().sendMessage(embed).queue();
        });
    }
    @Override
    public String getHelp() {
        return "Shows a dog pic";
    }

    @Override
    public String getInvoke() {
        return "dog";
    }
}
