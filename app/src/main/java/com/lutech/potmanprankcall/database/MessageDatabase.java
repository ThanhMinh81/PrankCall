package com.lutech.potmanprankcall.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.lutech.potmanprankcall.dao.MessageDao;
import com.lutech.potmanprankcall.model.ChatMessage;
import com.lutech.potmanprankcall.model.User;

@Database(entities = {ChatMessage.class, User.class}, version = 3, exportSchema = false)
public abstract class MessageDatabase extends RoomDatabase {
    public abstract MessageDao messageDao();

    private static volatile MessageDatabase instance;

    public static synchronized MessageDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            MessageDatabase.class, "my_database")
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    public static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);


            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Monster Man', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/camomonster.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/videocall_monster_man.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('G-man Monster', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/giantcamera.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/videocall_g_man_monster.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('DJ Monster', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/grandpamonster.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/videocall_djmonster.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Holy Monster', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/holymonster.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/videocall_holy_monster.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Camo Monster', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/camomonster.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/videocall_camo_monster.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Speaker Man', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/speakerman.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/videocall_speaker_man.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Titan Camera', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/titancamera.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/videocall_titan_camera.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Titan Speaker', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/camomonster.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/videocall_titan_speaker.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Rocket Monster', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/giantcamera.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/videocall_rocket_monster.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Hydra Monster', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/holymonster.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/videocall_hydra_monster1.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Urinal Monster', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/speakerman.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/video_call_urinal_monster.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Spider Camera', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/camomonster.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/video_call_spider_camera.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Grandpa Monster', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/grandpamonster.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/videocall_grandpa_monster.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Giant Monster', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/titanspeaker.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/video_call_giant_monster.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Giant Camera', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/holymonster.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/video_call_giant_camera.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Mafia Monster', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/speakerman.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/video_call_mafia_monster.mp4')");


            // tiep tuc isert neu null thi bo


        }
    };
}
