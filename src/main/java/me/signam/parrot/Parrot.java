package me.signam.parrot;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;

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
                    if (!event.getMessage().getContent().equals("") && !event.getMessage().getContent().equals("Message has been sent to channel <#" + configUtils.getOutputChannel() + ">")) {
                        System.out.println("Debug Mode:" + configUtils.isDebugMode());
                        if (configUtils.isDebugMode()) {


                            guild.getChannelById(Snowflake.of(configUtils.getInputChannel())).block().getRestChannel()
                                    .createMessage("Message has been sent to channel <#" + configUtils.getOutputChannel() + ">").block();
                        } else {

                        }
                        guild.getChannelById(Snowflake.of(configUtils.getOutputChannel())).block().getRestChannel()
                                .createMessage(event.getMessage().getContent()).block();

                        event.getMessage().getData().attachments().forEach(attachmentData -> {
                            guild.getChannelById(Snowflake.of(configUtils.getOutputChannel())).block().getRestChannel()
                                    .createMessage(attachmentData.url()).block();

                        });

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        gateway.onDisconnect().block();
    }



}
