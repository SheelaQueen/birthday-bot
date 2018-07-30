package commands

import co.vulpin.commando.CommandEvent
import co.vulpin.commando.annotations.Cmd
import co.vulpin.commando.annotations.Optional
import decorators.BasicPerms
import net.dv8tion.jda.core.EmbedBuilder

class Help {

    @Cmd
    @Optional
    @BasicPerms
    get(CommandEvent event) {
        def embed = new EmbedBuilder().with {
            color = event.guild?.selfMember?.color

            def self = event.JDA.selfUser
            setAuthor(self.name, null, self.effectiveAvatarUrl)

            addField(
                "bday set [day], [month], [year], [gmt offset]",
                "Enters your birthday into the system.\n\n" +
                        "If you don't know what a GMT offset is, " +
                        "[click here](https://www.timeanddate.com/time/map/) and hover over your location " +
                        "on the map. Your GMT offset is the value at the bottom that is highlighted. If " +
                        "you the highlighted value at the bottom simply says `UTC`, then your GMT offset " +
                        "is 0.",
                false
            )

            addField(
                "bday role set [role name]",
                "Selects a role to use as the birthday role.",
                false
            )

            addField(
                "bday role create",
                "Automatically creates a birthday role and stores it.",
                false
            )

            build()
        }

        event.channel.sendMessage(embed).queue()
    }

}
