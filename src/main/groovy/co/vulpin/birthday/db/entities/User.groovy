package co.vulpin.birthday.db.entities

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

class User {

    Long birthdayEpochSeconds
    Integer gmtOffset

    OffsetDateTime getBirthdayStart() {
        def instant = Instant.ofEpochSecond(birthdayEpochSeconds)
        return OffsetDateTime.ofInstant(instant, zoneOffset)
    }

    OffsetDateTime getBirthdayEnd() {
        return birthdayStart.plusDays(1)
    }

    ZoneOffset getZoneOffset() {
        return ZoneOffset.ofTotalSeconds(gmtOffset)
    }

}
