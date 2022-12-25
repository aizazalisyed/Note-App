package com.architecturecomponentstutorial;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {Note.class}, version = 1)
abstract public class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration().addCallback(roomCallback).build();
        }
        return instance;
    }
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();

        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void,Void>{
        private NoteDao nodeDao;
        private PopulateDbAsyncTask(NoteDatabase db){
            nodeDao = db.noteDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {

            nodeDao.insert(new Note("Title 1", "Description1", 1));
            nodeDao.insert(new Note("Title 2", "Description2", 2));
            nodeDao.insert(new Note("Title 3", "Description3", 3));
            return null;
        }
    }
}
