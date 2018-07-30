package commands

import co.vulpin.commando.CommandEvent
import co.vulpin.commando.annotations.Cmd
import co.vulpin.commando.annotations.Optional
import decorators.BasicPerms

import static net.dv8tion.jda.core.Permission.MANAGE_ROLES
import static net.dv8tion.jda.core.Permission.MESSAGE_ADD_REACTION
import static net.dv8tion.jda.core.Permission.MESSAGE_EMBED_LINKS
import static net.dv8tion.jda.core.Permission.MESSAGE_EXT_EMOJI
import static net.dv8tion.jda.core.Permission.MESSAGE_READ
import static net.dv8tion.jda.core.Permission.MESSAGE_WRITE

class Invite {

    @Cmd
    @Optional
    @BasicPerms
    void get(CommandEvent event) {
        event.reply("[**Invite**](${getInviteUrl(event)})").queue()
    }

    @Cmd
    void basic(CommandEvent event) {
        event.reply(getInviteUrl(event)).queue()
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
