package commands

import co.vulpin.birthday.db.Database
import co.vulpin.birthday.db.entities.User as DbUser
import co.vulpin.commando.CommandEvent
import co.vulpin.commando.annotations.Aliases
import co.vulpin.commando.annotations.Cmd
import co.vulpin.commando.annotations.Optional
import co.vulpin.commando.annotations.check.OwnerOnly
import com.google.cloud.firestore.DocumentReference
import com.jagrosh.jdautilities.commons.utils.FinderUtil
import decorators.BasicPerms

import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Aliases(["me"])
@Optional
class User {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, YYYY O")

    @Cmd
    @BasicPerms
    void set(CommandEvent event, String day, String month, String year, String gmtOffset) {
        def ref = getUserRef(event.author.id)

        def date
        try {
            date = parseDate(day, month, year, gmtOffset)
        } catch (e) {
            event.replyError(
                "An error occurred while parsing that date!",
                "**$e.class.simpleName:** $e.message"
            ).queue()
            return
        }

        if(ref.get().get().exists()) {
            event.replyError("You have already set a birthday! " +
                    "**You cannot change your birthday once it has been set to prevent abuse.** " +
                    "If there has been a mistake, please contact **Nik#1234**.").queue()
            return
        }

        ref.set([
            birthdayEpochSeconds: date.toEpochSecond(),
            gmtOffset: date.offset.totalSeconds
        ])

        event.reply("Successfully set your birthday to **${date.format(dateFormatter)}**!").queue()
    }

    @Cmd
    @BasicPerms
    void set(CommandEvent event, String ignored) {
        event.reply("Looks like your forgot a couple parameters! An example of the correct usage is: " +
                "`bday set 30, 9, 1999, -4`").queue()
    }

    @Cmd
    @Optional
    @BasicPerms
    void get(CommandEvent event) {
        def dbUser = getDbUser(event.author.id)
        def date = dbUser?.birthdayStart
        if(date)
            event.reply(date.format(dateFormatter)).queue()
        else
            event.reply("You haven't set a birthday yet!").queue()
    }

    @Cmd
    @Optional
    @BasicPerms
    void get(CommandEvent event, String input) {
        def user = FinderUtil.findMembers(input, event.guild)[0]?.user
        if(!user) {
            event.replyError("I couldn't find a person for that input :pensive:").queue()
            return
        }

        def dbUser = getDbUser(user.id)
        def date = dbUser?.birthdayStart
        if(date)
            event.reply(date.format(dateFormatter)).queue()
        else
            event.reply("That person hasn't set a birthday yet!").queue()
    }

    @Cmd
    @OwnerOnly
    void reset(CommandEvent event, String input) {
        def user = FinderUtil.findUsers(input, event.JDA)[0]
        if(!user) {
            event.replyError("I couldn't find a person for that input :pensive:").queue()
            return
        }

        getUserRef(user.id).delete()
        event.reply("${user.asMention}'s birthday has been reset.").queue()
    }

    private OffsetDateTime parseDate(String day, String month, String year, String gmtOffset) {
        return parseDate(day as int, month as int, year as int, gmtOffset as double)
    }

    private OffsetDateTime parseDate(int day, int month, int year, double gmtOffset) {
        def date = LocalDate.of(year, month, day)
        def time = LocalTime.of(0, 0)

        def seconds = gmtOffset * 60 * 60 as int

        def zone = ZoneOffset.ofTotalSeconds(seconds)

        def birthday = OffsetDateTime.of(date, time, zone)

        return birthday
    }

    private DbUser getDbUser(String userId) {
        def future = getUserRef(userId).get()
        return future.get().toObject(DbUser)
    }

    private DocumentReference getUserRef(String userId) {
        return Database.instance.getUserRef(userId)
    }

}
