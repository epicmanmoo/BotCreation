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
import java.util.Random;

public class MemeCommand implements ICommand {

    private final Random random;

    public MemeCommand(Random random) {
        this.random = random;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member selfMember = event.getGuild().getSelfMember();
        MessageChannel channel = event.getChannel();
        WebUtils.ins.getJSONObject("https://meme-api.herokuapp.com/gimme").async((json) -> {

//            if (random.nextInt(2) == 1) {
//                String title = json.get("title").asText();
//                String url = json.get("url").asText();
//                String image = json.get("url").asText();
//                MessageEmbed embed = EmbedUtils.embedImage(url).build();
//                event.getChannel().sendMessage(embed).queue();
//                return;
//            }

            String title = json.get("title").asText();
            String url = json.get("url").asText();
            String image = json.get("url").asText();

            MessageEmbed embed = EmbedUtils.embedMessageWithTitle(title, url).setImage(image).build();

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
        return "Shows a random meme";
    }

    @Override
    public String getInvoke() {
        return "meme";
    }
}
