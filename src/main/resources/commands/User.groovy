package commands

import co.vulpin.birthday.db.Database
import co.vulpin.commando.CommandEvent
import co.vulpin.commando.annotations.Aliases
import co.vulpin.commando.annotations.Cmd
import co.vulpin.commando.annotations.Optional
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.SetOptions
import decorators.BasicPerms

import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import co.vulpin.birthday.db.entities.User as DbUser

@Aliases(["me"])
@Optional
class User {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, YYYY")

    @Cmd
    @BasicPerms
    void set(CommandEvent event, String dayString, String monthString, String yearString, String gmtOffsetString) {
        def ref = Database.instance.getUserRef(event.author.id)

        if(!dayString.isInteger()) {
            event.replyError("$dayString is not a valid day!").queue()
            return
        }

        if(!monthString.isInteger()) {
            event.replyError("$monthString is not a valid month!").queue()
            return
        }

        if(!yearString.isInteger()) {
            event.replyError("$yearString is not a valid year!").queue()
            return
        }

        if(!dayString.isInteger()) {
            event.replyError("$gmtOffsetString is not a valid GMT offset!").queue()
            return
        }

        int day = dayString as int
        int month = monthString as int
        int year = yearString as int
        int gmtOffset = gmtOffsetString as int

        def dateTime
        try {
            def offset = ZoneOffset.ofHours(gmtOffset)
            dateTime = OffsetDateTime.of(year, month, day, 0, 0 ,0 ,0, offset)
        } catch (e) {
            event.replyError("An error occurred while parsing your birthday!", e.message).queue()
            return
        }

        ref.set([
                birthDay: day,
                birthMonth: month,
                birthYear: year,
                gmtOffset: gmtOffset
        ], SetOptions.merge())

        event.reply("Successfully set up your birthday to **${dateTime.format(dateFormatter)}**!").queue()
    }

    @Cmd
    @Optional
    @BasicPerms
    void get(CommandEvent event) {
        def dbUser = getUserRef(event).get().get().toObject(DbUser)
        def date = dbUser?.birthdayDate
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

    private DocumentReference getUserRef(CommandEvent event) {
        return Database.instance.getUserRef(event.author.id)
    }

}
