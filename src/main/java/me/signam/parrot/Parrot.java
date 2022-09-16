package me.signam.parrot;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.checkerframework.checker.units.qual.C;

import java.time.Instant;

public class Parrot {


    public static void main(final String[] args) {

        ConfigUtils configUtils = new ConfigUtils();

        final String token = configUtils.getToken();
        final DiscordClient client = DiscordClient.create(token);
        final GatewayDiscordClient gateway = client.login().block();

        gateway.on(MessageCreateEvent.class).subscribe(event -> {
            try {
                final Message message = event.getMessage();
                if (configUtils.getInputChannel().equals(message.getChannelId().asString())) {


                    final Guild guild = message.getGuild().block();
                    if (!event.getMessage().getContent().equals(""))
                        guild.getChannelById(Snowflake.of(configUtils.getOutputChannel())).block().getRestChannel()
                                .createMessage(event.getMessage().getContent()).block();

                    event.getMessage().getData().attachments().forEach(attachmentData -> {
                        guild.getChannelById(Snowflake.of(configUtils.getOutputChannel())).block().getRestChannel()
                                .createMessage(attachmentData.url()).block();
                    });


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        gateway.onDisconnect().block();
    }



}
