package co.vulpin.birthday.db

import co.vulpin.birthday.db.entities.Guild
import co.vulpin.birthday.db.entities.User
import co.vulpin.firestore.sync.central.CentrallySyncedCollection
import co.vulpin.firestore.sync.individual.IndividuallySyncedCollection
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions

@Singleton(strict = false)
class Database {

    @Delegate
    private Firestore firestore

    private CentrallySyncedCollection<User> users
    private IndividuallySyncedCollection<Guild> guilds

    Database() {

        def creds = GoogleCredentials.applicationDefault

        def opts = FirestoreOptions.newBuilder()
                .setCredentials(creds)
                .setTimestampsInSnapshotsEnabled(true)
                .build()

        firestore = opts.service

        users = new CentrallySyncedCollection<>(collection("users"), User)
        guilds = new IndividuallySyncedCollection<>(collection("guilds"), Guild)
    }

    IndividuallySyncedCollection<Guild> getGuilds() {
        return guilds
    }

    CentrallySyncedCollection<User> getUsers() {
        return users
    }

    DocumentReference getGuildRef(String guildId) {
        return collection("guilds").document(guildId)
    }

    Guild getGuild(String guildId) {
        def fut = getGuildRef(guildId).get()
        return fut.get().toObject(Guild)
    }

    DocumentReference getUserRef(String userId) {
        return collection("users").document(userId)
    }

    User getUser(String userId) {
        def fut = getUserRef(userId).get()
        return fut.get().toObject(User)
    }

}
