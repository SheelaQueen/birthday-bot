package commands

import co.vulpin.commando.CommandEvent
import co.vulpin.commando.annotations.Cmd
import co.vulpin.commando.annotations.Optional
import co.vulpin.commando.annotations.check.BotPerms

import static net.dv8tion.jda.core.Permission.MESSAGE_WRITE

class Support {

    @Cmd
    @Optional
    @BotPerms([MESSAGE_WRITE])
    void get(CommandEvent event) {
        event.channel.sendMessage(System.getenv("DISCORD_SUPPORT_SERVER_INVITE")).queue()
    }

}
