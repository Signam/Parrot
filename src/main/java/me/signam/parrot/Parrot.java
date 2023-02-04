package me.signam.parrot;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Color;


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

                        EmbedCreateSpec embed = EmbedCreateSpec.builder()
                                .color(Color.BLUE)
                                .title("Parrot")
                                .description("Parrot says: " + event.getMessage().getContent())
                                //.image("https://avatars.githubusercontent.com/u/90117558?s=200&v=4")
                                .footer("Powered by The Fifth Column Inc Systems", "https://avatars.githubusercontent.com/u/90117558?s=200&v=")
                                .build();


                        EmbedCreateSpec debugembed = EmbedCreateSpec.builder()
                                .color(Color.BLUE)
                                .title("Parrot")
                                .description("Message has been sent to channel <#" + configUtils.getOutputChannel() + ">")
                                //.image("https://avatars.githubusercontent.com/u/90117558?s=200&v=4")
                                .footer("Powered by The Fifth Column Inc Systems", "https://avatars.githubusercontent.com/u/90117558?s=200&v=")
                                .build();


                        if (!event.getMessage().getContent().equals("<@" + configUtils.getBotID() + ">")) {
                            if (configUtils.isDebugMode()) {

                                guild.getChannelById(Snowflake.of(configUtils.getInputChannel()))
                                        .ofType(GuildMessageChannel.class)
                                        .flatMap(channel -> channel.createMessage(MessageCreateSpec.builder()
                                                .addEmbed(debugembed)
                                                .build()
                                        )).subscribe();
                            }

                            guild.getChannelById(Snowflake.of(configUtils.getOutputChannel()))
                                    .ofType(GuildMessageChannel.class)
                                    .flatMap(channel -> channel.createMessage(embed))
                                    .subscribe();
                        }


                        event.getMessage().getData().attachments().forEach(attachmentData -> {

                            EmbedCreateSpec imageembed = EmbedCreateSpec.builder()
                                    .color(Color.BLUE)
                                    .title("Parrot")
                                    .description("Parrot sent a new image!")
                                    .image(attachmentData.url())
                                    .footer("Powered by The Fifth Column Inc Systems", "https://avatars.githubusercontent.com/u/90117558?s=200&v=")
                                    .build();

                            EmbedCreateSpec debugimageembed = EmbedCreateSpec.builder()
                                    .color(Color.BLUE)
                                    .title("Parrot")
                                    .description("Image has been sent to channel <#" + configUtils.getOutputChannel() + ">")
                                    .image(attachmentData.url())
                                    .footer("Powered by The Fifth Column Inc Systems", "https://avatars.githubusercontent.com/u/90117558?s=200&v=")
                                    .build();

                            if (configUtils.isDebugMode()) {
                                guild.getChannelById(Snowflake.of(configUtils.getInputChannel()))
                                        .ofType(GuildMessageChannel.class)
                                        .flatMap(channel -> channel.createMessage(MessageCreateSpec.builder()
                                                .addEmbed(debugimageembed)
                                                .build()
                                        )).subscribe();
                            }


                            guild.getChannelById(Snowflake.of(configUtils.getOutputChannel()))
                                    .ofType(GuildMessageChannel.class)
                                    .flatMap(channel -> channel.createMessage(MessageCreateSpec.builder()
                                            .addEmbed(imageembed)
                                            .build()
                                    )).subscribe();
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
