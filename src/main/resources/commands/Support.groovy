package commands

import co.vulpin.commando.CommandEvent
import co.vulpin.commando.annotations.Cmd
import co.vulpin.commando.annotations.Optional
import co.vulpin.commando.annotations.check.BotPerms

import static net.dv8tion.jda.core.Permission.MESSAGE_WRITE

class Support {

    private static final SUPPORT_INVITE = "https://discord.gg/CCeDmg"

    @Cmd
    @Optional
    @BotPerms([MESSAGE_WRITE])
    void get(CommandEvent event) {
        event.channel.sendMessage(SUPPORT_INVITE).queue()
    }

}
