package com.veon.prebid.demo.data.datasource.db

import com.veon.prebid.demo.data.datasource.db.schemas.NoteSchema
import com.veon.prebid.demo.data.datasource.db.schemas.TagSchema
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class Database {

    companion object {
        private var INSTANCE: Realm? = null

        fun getInstance(): Realm {
            return if (INSTANCE != null) {
                INSTANCE!!
            } else {
                synchronized(this) {
                    val config = RealmConfiguration.Builder(schema = setOf(NoteSchema::class, TagSchema::class))
                        .name("minote.dp")
                        .schemaVersion(2)
                    INSTANCE = Realm.open(config.build())
                }
                INSTANCE!!
            }
        }

    }


}