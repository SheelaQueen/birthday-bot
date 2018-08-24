package co.vulpin.birthday.db.entities

import co.vulpin.firestore.sync.individual.IndividuallySyncedEntity
import groovy.transform.InheritConstructors

@InheritConstructors
class Guild extends IndividuallySyncedEntity {

    String birthdayRoleId

}
