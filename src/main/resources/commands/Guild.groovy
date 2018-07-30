package commands

import co.vulpin.birthday.db.Database
import co.vulpin.commando.CommandEvent
import co.vulpin.commando.annotations.Aliases
import co.vulpin.commando.annotations.Cmd
import co.vulpin.commando.annotations.Optional
import co.vulpin.commando.annotations.check.GuildAdminOnly
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.SetOptions
import com.jagrosh.jdautilities.commons.utils.FinderUtil
import co.vulpin.birthday.db.entities.Guild as DbGuild
import decorators.BasicPerms

@Aliases(["server"])
@Optional
class Guild {

    class Role {

        private static final BIRTHDAY_CAKE = "\uD83C\uDF82"

        @Cmd
        @GuildAdminOnly
        @Optional
        @BasicPerms
        void set(CommandEvent event, String roleQuery) {
            def role = FinderUtil.findRoles(roleQuery, event.guild)[0]

            if(role) {
                getGuildRef(event).set([
                    birthdayRoleId: role.id
                ], SetOptions.merge())
                event.reply("The birthday role has been set to ${role.asMention}").queue()
            } else {
                event.reply("I couldn't find a role for that name :pensive:").queue()
            }
        }

        @Cmd
        @Aliases(["disable", "stop"])
        @GuildAdminOnly
        @BasicPerms
        void remove(CommandEvent event) {
            getGuildRef(event).set([
                birthdayRoleId: null
            ], SetOptions.merge())
            event.reply("Removed this server's birthday role. " +
                    "It will no longer be assigned on people's birthdays.").queue()
        }

        @Cmd
        @GuildAdminOnly
        @BasicPerms
        void create(CommandEvent event) {
            def roleAction = event.guild.controller.createRole()
                    .setName(BIRTHDAY_CAKE)

            roleAction.queue({
                getGuildRef(event).set([
                    birthdayRoleId: it.id
                ], SetOptions.merge())
                event.reply("Successfully created a birthday role.").queue()
            })
        }

        @Cmd
        @Optional
        @BasicPerms
        void get(CommandEvent event) {
            def dbGuild = getGuildRef(event).get().get().toObject(DbGuild)
            if(dbGuild.birthdayRoleId) {
                def role = event.guild.getRoleById(dbGuild.birthdayRoleId)
                if(role)
                    event.reply("Birthday Role: ${role.asMention}").queue()
                else
                    event.reply("It appears the current birthday role has been deleted. " +
                            "Please setup a new one.").queue()
            } else {
                event.reply("A birthday role has not been setup on this server.").queue()
            }
        }

        private DocumentReference getGuildRef(CommandEvent event) {
            return Database.instance.getGuildRef(event.guild.id)
        }

    }

}
