package brotatobot.functionality;

import brotatobot.functionality.hosting.Secrets;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.time.Instant;
import java.util.Random;

/** Text in here **/

public class Main {

    private final Random random = new Random();


    private Main() {

        CommandManager commandManager = new CommandManager(random);
        Listener listener = new Listener(commandManager);
        Logger logger = LoggerFactory.getLogger(Main.class);

        WebUtils.setUserAgent("Mozilla/5.0 Brotato/Zebbzz#6235");
        EmbedUtils.setEmbedBuilder(
                () -> new EmbedBuilder()
                        .setColor(getRandomColor())
                        .setFooter("Developed by Zebbzz and FeedMeAPIs", "https://discord.gg/CXksb8u")
                        .setTimestamp(Instant.now())
        );

        try {
            logger.info("Booting");
            new JDABuilder(AccountType.BOT) //Sets account type as bot
                    .setToken(Secrets.TOKEN) // Token stored in Secrets class, logs in to the bot
                    .setGame(Game.streaming("Join Our Discord!", "https://discord.gg/CXksb8u")) // <- Discord invite link stored as a "Playing..."
                    .addEventListener(listener) //Read listener class and execute code inside
                    .build().awaitReady(); //
            logger.info("Running");
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Color getRandomColor() {
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();

        return new Color(r, g, b);
    }


    public static void main(String[] args) {
        new Main();
    }

}
