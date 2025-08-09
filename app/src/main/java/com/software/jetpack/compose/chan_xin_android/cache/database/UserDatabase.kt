package com.software.jetpack.compose.chan_xin_android.cache.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.software.jetpack.compose.chan_xin_android.cache.dao.IDynamicDao
import com.software.jetpack.compose.chan_xin_android.cache.dao.ISocialDao
import com.software.jetpack.compose.chan_xin_android.cache.dao.IUserDao
import com.software.jetpack.compose.chan_xin_android.converter.FriendStatusConverter
import com.software.jetpack.compose.chan_xin_android.converter.FriendStatusInfoConverter
import com.software.jetpack.compose.chan_xin_android.converter.PostContentConverter
import com.software.jetpack.compose.chan_xin_android.converter.PostMetaConverter
import com.software.jetpack.compose.chan_xin_android.entity.CommentReply
import com.software.jetpack.compose.chan_xin_android.entity.FriendApply
import com.software.jetpack.compose.chan_xin_android.entity.FriendFeed
import com.software.jetpack.compose.chan_xin_android.entity.FriendRelation
import com.software.jetpack.compose.chan_xin_android.entity.Post
import com.software.jetpack.compose.chan_xin_android.entity.PostLike
import com.software.jetpack.compose.chan_xin_android.entity.User
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
internal const val DATABASE_NAME = "chan_xin.db"
@Database(entities = [User::class,FriendApply::class,FriendRelation::class,Post::class,FriendFeed::class,PostLike::class,CommentReply::class], version = 9, exportSchema = true)
@TypeConverters(
    FriendStatusConverter::class,
    FriendStatusInfoConverter::class,
    PostMetaConverter::class,
    PostContentConverter::class
)
abstract class UserDatabase:RoomDatabase() {
    abstract fun userDao():IUserDao
    abstract fun socialDao():ISocialDao
    abstract fun dynamicDao():IDynamicDao

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: UserDatabase? = null
        fun getInstance(): UserDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase().also { instance = it }
            }
        }

        private fun buildDatabase(): UserDatabase {
            val migration1To2 = object : Migration(1, 2) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL(
                        """
            CREATE TABLE IF NOT EXISTS `friend_apply` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `user_id` TEXT NOT NULL, -- 对应实体类 userId 的 @ColumnInfo(name = "user_id")
                `nickname` TEXT NOT NULL,
                `avatar_url` TEXT NOT NULL, -- 对应实体类 avatar 的 @ColumnInfo(name = "avatar_url")
                `gender` INTEGER NOT NULL,
                `greet_msg` TEXT NOT NULL, -- 对应实体类 greetMsg 的 @ColumnInfo(name = "greet_msg")
                `status` INTEGER NOT NULL
            )
        """.trimIndent()
                    )
                }
            }

            val migration2To3 = object : Migration(2, 3) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL("DELETE FROM friend_apply")
                    db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_FriendApply_userId ON friend_apply(user_id)")
                }
            }
            val migration3To4 = object : Migration(3, 4) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL("DELETE FROM friend_apply")
                    db.execSQL("DROP INDEX IF EXISTS index_FriendApply_userId")
                    db.execSQL("ALTER TABLE friend_apply ADD COLUMN applicant_id TEXT NOT NULL")
                    db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_friend_apply_userId_and_applicantId ON friend_apply(user_id,applicant_id)")

                }
            }
            val migration4To5 = object : Migration(4,5) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL("""
                        CREATE TABLE IF NOT EXISTS `friend_relation` (
                            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                            `user_id` TEXT NOT NULL,
                            `friend_id` TEXT NOT NULL,
                            `status` TEXT NOT NULL
                        )
                    """.trimIndent())

                }
            }
            val migration5To6 = object : Migration(5,6) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL("""
                        CREATE TABLE IF NOT EXISTS `post` (
                            `post_id` TEXT PRIMARY KEY NOT NULL,
                            `user_id` TEXT NOT NULL,
                            `content` TEXT NOT NULL,
                            `meta` TEXT NOT NULL,
                            `is_pinned`  INTEGER NOT NULL CHECK (is_pinned IN (0, 1)),
                            `create_time` INTEGER NOT NULL
                        )
                    """.trimIndent())
                }


            }
            val migration6To7 = object : Migration(6,7) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL("""
                        CREATE TABLE IF NOT EXISTS `friend_feed` (
                            `post_id` TEXT PRIMARY KEY NOT NULL,
                            `user_id` TEXT NOT NULL,
                            `content` TEXT NOT NULL,
                            `meta` TEXT NOT NULL,
                            `is_pinned`  INTEGER NOT NULL CHECK (is_pinned IN (0, 1)),
                            `create_time` INTEGER NOT NULL
                        )
                    """.trimIndent())
                }


            }
            val migration7To8 = object : Migration(7,8) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL("""
                        CREATE TABLE IF NOT EXISTS `post_like` (
                            `id` INTEGER PRIMARY KEY NOT NULL,
                            `post_id` TEXT NOT NULL,
                            `user_id` TEXT NOT NULL,
                            `is_deleted` INTEGER NOT NULL CHECK (is_deleted IN (0,1))
                        )
                    """.trimIndent())
                    db.execSQL("""
                        CREATE UNIQUE INDEX IF NOT EXISTS index_post_like_postId_and_userId ON post_like(post_id,user_id)
                    """.trimIndent())
                }

            }
            val migration8To9 = object : Migration(8,9) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL("""
                        CREATE TABLE IF NOT EXISTS `comment_reply` (
                            `comment_replie_id` TEXT PRIMARY KEY NOT NULL,
                            `post_id` TEXT NOT NULL,
                            `user_id` TEXT NOT NULL,
                            `target_user_id` TEXT NOT NULL,
                            `content` TEXT NOT NULL,
                            `is_deleted` INTEGER NOT NULL CHECK (is_deleted IN (0,1))
                        )
                    """.trimIndent())
                }

            }
            return Room.databaseBuilder(
                context = AppGlobal.getAppContext(), klass = UserDatabase::
                class.java, name = DATABASE_NAME
            )
                .addMigrations(
                    migration1To2,
                    migration2To3,
                    migration3To4,
                    migration4To5,
                    migration5To6,
                    migration6To7,
                    migration7To8,
                    migration8To9
                )
                .build()
        }
    }

}