package brotatobot.commands.textcommands;

import brotatobot.functionality.Constants;
import brotatobot.objects.ICommand;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class UICommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (args.isEmpty()) {
            event.getChannel().sendMessage("Missing arguments, check `" + Constants.PREFIX + "help " + getInvoke() + "`").queue();
            return;
        }

        String joined = String.join("", args);
        List<User> foundUsers = FinderUtil.findUsers(joined, event.getJDA());

        if (foundUsers.isEmpty()) {
            List<Member> foundMembers = FinderUtil.findMembers(joined, event.getGuild());

            if (foundMembers.isEmpty()) {
                event.getChannel().sendMessage("No users found for `" + joined + "`").queue();
                return;
            }

            foundUsers = foundMembers.stream().map(Member::getUser).collect(Collectors.toList());

        }

        User user = foundUsers.get(0);
        Member member = event.getGuild().getMember(user);

        MessageEmbed embed = EmbedUtils.defaultEmbed()
                .setColor(member.getColor())
                .setThumbnail(user.getEffectiveAvatarUrl())
                .addField("Username#Discriminator:", String.format("%#s", user), false)
                .addField("Display Name:", member.getEffectiveName(), false)
                .addField("User ID + Mention:", String.format("%s (%s)", user.getId(), member.getAsMention()), false)
                .addField("Account Created:", user.getCreationTime().format(DateTimeFormatter.RFC_1123_DATE_TIME), false)
                .addField("Joined Guild:", member.getJoinDate().format(DateTimeFormatter.RFC_1123_DATE_TIME), false)
                .addField("Online Status", member.getOnlineStatus().name().toLowerCase().replaceAll("_", " "), false)
                .addField("Bot Account", user.isBot() ? "Yes" : "No", false)
                .build();

        event.getChannel().sendMessage(embed).queue();

    }


    @Override
    public String getHelp() {
        return "Displays information about a user\n" +
                "Usage: '" + Constants.PREFIX + getInvoke() + " [username/@user/user id]`";
    }

    @Override
    public String getInvoke() {
        return "userinfo";
    }
}
