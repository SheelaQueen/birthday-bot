package co.vulpin.birthday.db.entities

import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset

class User {

    Integer birthDay, birthMonth, birthYear, gmtOffset

    LocalDate getBirthdayDate() {
        if(birthDay && birthMonth && birthYear)
            return LocalDate.of(birthYear, birthMonth, birthDay)
        else
            return null
    }

    OffsetDateTime getBirthdayStart() {
        def time = LocalTime.of(0, 0)
        return OffsetDateTime.of(birthdayDate, time, zoneOffset)
    }

    OffsetDateTime getBirthdayEnd() {
        return birthdayStart.plusDays(1)
    }

    ZoneOffset getZoneOffset() {
        return ZoneOffset.ofHours(gmtOffset)
    }

}
