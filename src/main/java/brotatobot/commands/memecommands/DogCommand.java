package brotatobot.commands.memecommands;

import brotatobot.objects.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;

public class DogCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member selfMember = event.getGuild().getSelfMember();
        MessageChannel channel = event.getChannel();
        if (!selfMember.hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
            channel.sendMessage("Sorry, I do not have permission to use this command!\n" +
                    "Please enable me to send embed links to use this command").queue();
            return;
        }
        try {
            JSONObject json = readJsonFromUrl("https://dog.ceo/api/breeds/image/random");
            //System.out.println(json.get("message"));
            MessageEmbed embed = EmbedUtils.embedImage(json.get("message").toString()).build();
            channel.sendMessage(embed).queue();
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        URLConnection is = new URL(url).openConnection();
        InputStream iss = is.getInputStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(iss, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            iss.close();
        }
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
