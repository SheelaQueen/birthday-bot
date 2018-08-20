package commands

import co.vulpin.commando.CommandEvent
import co.vulpin.commando.annotations.Cmd
import co.vulpin.commando.annotations.Optional
import decorators.BasicPerms

import static net.dv8tion.jda.core.Permission.*

class Invite {

    @Cmd
    @Optional
    @BasicPerms
    void get(CommandEvent event) {
        event.reply("**[Invite](${getInviteUrl(event)})**").queue()
    }

    private String getInviteUrl(CommandEvent event) {
        def bot = event.JDA.asBot()
        return bot.getInviteUrl(
            MESSAGE_READ,
            MESSAGE_WRITE,
            MESSAGE_EMBED_LINKS,
            MANAGE_ROLES,
            MESSAGE_EXT_EMOJI,
            MESSAGE_ADD_REACTION
        )
    }

}
