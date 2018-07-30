package commands

import co.vulpin.birthday.db.Database
import co.vulpin.birthday.db.entities.User as DbUser
import co.vulpin.commando.CommandEvent
import co.vulpin.commando.annotations.Aliases
import co.vulpin.commando.annotations.Cmd
import co.vulpin.commando.annotations.Optional
import com.google.cloud.firestore.DocumentReference
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
        def ref = Database.instance.getUserRef(event.author.id)

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
                    "You cannot change your birthday once it has been set to prevent abuse. " +
                    "If there has been a mistake, please contact **Nik#1234**.").queue()
            return
        }

        ref.set([
                birthDay: date.dayOfMonth,
                birthMonth: date.monthValue,
                birthYear: date.year,
                gmtOffset: date.offset.totalSeconds / 60 / 60 as int
        ])

        event.reply("Successfully set up your birthday to **${date.format(dateFormatter)}**!").queue()
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
        def dbUser = getUserRef(event).get().get().toObject(DbUser)
        def date = dbUser?.birthdayStart
        if(date) {
            event.reply(date.format(dateFormatter)).queue()
        } else {
            event.reply("You haven't set a birthday yet!").queue()
        }
    }

    @Cmd
    @Aliases(["delete", "remove", "stop"])
    @BasicPerms
    void remove(CommandEvent event) {
        getUserRef(event).delete()
        event.reply("Your birthday has been removed :wave: Sorry to see you go :pensive:").queue()
    }

    private OffsetDateTime parseDate(String day, String month, String year, String gmtOffset) {
        return parseDate(day as int, month as int, year as int, gmtOffset as int)
    }

    private OffsetDateTime parseDate(int day, int month, int year, int gmtOffset) {
        def date = LocalDate.of(year, month, day)
        def time = LocalTime.of(0, 0)
        def zone = ZoneOffset.ofHours(gmtOffset)

        def birthday = OffsetDateTime.of(date, time, zone)

        return birthday
    }

    private DocumentReference getUserRef(CommandEvent event) {
        return Database.instance.getUserRef(event.author.id)
    }

}
