package com.team.szkielet.quiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;

// this class takes care of opening the database if it exists, creating it if it does not, and upgrading it as necessary.
public class QuizDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PolitechnikaQuiz.db";
    private static final int DATABASE_VERSION = 3;
    private SQLiteDatabase db;
    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuizContract.QuestionsTable.TABLE_NAME + " ( " +
                QuizContract.QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuizContract.QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuizContract.QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuizContract.QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuizContract.QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuizContract.QuestionsTable.COLUMN_OPTION4 + " TEXT, " +
                QuizContract.QuestionsTable.COLUMN_ANSWER_NR + " INTEGER" +
                ")";
//stworzy tablice w sqlu
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuizContract.QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    private void fillQuestionsTable() {
        Question q1 = new Question("Zaznacz AXD", "A)Ty ", "B)No nie", "C)Wiem", "D)Jak", 1);
        addQuestion(q1);
        Question q2 = new Question("Zaznacz B", "A)MnieXD", "B)Nie", "C)Przegadasz","D)Bo", 2);
        addQuestion(q2);
        Question q3 = new Question("Zaznacz C", "A)Mydło", "B)Wszystko", "C)Umyje","D)Nawet", 3);
        addQuestion(q3);
        Question q4 = new Question("Zaznacz A", "A)Dziewczyny", "B)Lubia", "C)Braz","D)XD", 1);
        addQuestion(q4);
        Question q5 = new Question("Po informatyce mozesz miec:", "A)Szybkie Dziewczyny", "B)Piekne samochody", "C)A i B sa poprawne","D)Koks", 3);
        addQuestion(q5);
        Question q6 = new Question("Najwiekszym chujem uczelni jest:", "A)Adam N", "B)Nie ma", "C)Robert Lewandowski","D)Twoj Stary", 3);
        addQuestion(q6);
        Question q7 = new Question("Ile wydzialow ma PL:", "A)4", "B)5", "C)1","D)7", 3);
        addQuestion(q7);
        Question q8 = new Question("Ile lat ma politechnika lodzka:", "A)Kot ma dom", "B)Piekne samochody", "C)A i B sa poprawne","D)Koks", 3);
        addQuestion(q8);
        Question q9 = new Question("Ilu jest wykladowcow:", "A)Szybkie Dziewczyny", "B)Piekne samochody", "C)A i B sa poprawne","D)Koks", 3);
        addQuestion(q9);
        Question q10 = new Question("Co najczesciej mowi ochelska:", "A)Tutaj tak", "B)yyyyyy", "C)No to tak","D)Tutaj", 3);
        addQuestion(q10);
        Question q11 = new Question("Cos:", "A)Szybkie Dziewczyny", "B)Piekne samochody", "C)A i B sa poprawne","D)Koks", 3);
        addQuestion(q11);
        Question q12 = new Question("Dlaczego Radek to cwel:", "A)Ty no nie wiem", "B)bo tak", "C)sram ci na leb","D)HALO", 3);
        addQuestion(q12);
        Question q13 = new Question("Kogo jebac i dlaczego adasia:", "A)bo tak", "B)ty no nie wiem", "C)ale jaja","D)Bo zjeb", 3);
        addQuestion(q13);
        Question q14 = new Question("Kto ma romans z uczennica:", "A)Adas", "B)Ten od enduro", "C)Sowizdrzal pierdolony","D)Koks", 3);
        addQuestion(q14);

    }

    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuizContract.QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuizContract.QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuizContract.QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuizContract.QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuizContract.QuestionsTable.COLUMN_OPTION4, question.getOption4());
        cv.put(QuizContract.QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        db.insert(QuizContract.QuestionsTable.TABLE_NAME, null, cv);
    }

    public List<Question> getAllQuestions() {
        List<Question> questionList = new ArrayList<>();
        //uruchamia tez opcje on create
        db = getReadableDatabase();
        //kursor po kazdym wierszu tabeli
        Cursor c = db.rawQuery("SELECT * FROM " + QuizContract.QuestionsTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_OPTION4)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_ANSWER_NR)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }
}
