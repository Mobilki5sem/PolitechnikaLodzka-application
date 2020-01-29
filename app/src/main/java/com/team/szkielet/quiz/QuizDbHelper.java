package com.team.szkielet.quiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;

// to otwiera baze jesli ona istnieje,tworzy ja jesli nie istnije i updatuje jesli potrzeba.
public class QuizDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PolitechnikaQuiz.db";
    private static final int DATABASE_VERSION = 8;
    private SQLiteDatabase db;

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //wywoluje jesli baza tworzona pierwszy raz
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
        Question q1 = new Question("Kiedy utworzono Politechnikę Łódzka", "24 maja 1945 ", "23 maja 1939", "15 maja 1945", "19 maja 1945", 1);
        addQuestion(q1);
        Question q2 = new Question("Kto jest rektorem Politechniki Łódzkiej?", "prof. dr hab. inż. Ireneusz Zbiciński", "prof. dr hab. inż. Sławomir Wiak", "prof. dr hab. inż. Dariusz Gawin", "prof. dr hab. Grzegorz Bąk", 2);
        addQuestion(q2);
        Question q3 = new Question("Czy Politechnika Łodzka uzyskała status uczelni badawczej?", "Tak. Otrzymała w 2019r.", "Nie", "Tak. Otrzymała w 2016r.", "Tak. Otrzymała w 2015r.", 2);
        addQuestion(q3);
        Question q4 = new Question("Ile jest wydzialów na Politechnice Łódzkiej?", "6", "8", "10", "9", 4);
        addQuestion(q4);
        Question q5 = new Question("Gdzie mieści się rektorat Politechniki Łódzkiej?", "Ks. Ignacego Skorupki 10/12, 90-001 Łódź", "Wólczańska 215, 90-001 Łódź", "Ks. Ignacego Skorupki 21, 90-001 Łódź", "aleja Politechniki 10, 93-590 Łódź", 1);
        addQuestion(q5);
        Question q6 = new Question("Jak nazywa się samorząd uczniowski FTIMS?", "Samorząd FTIMS", "FTIMSiaki", "WRS FTIMS", "ZSBD FTIMS", 3);
        addQuestion(q6);
        Question q7 = new Question("Kiedy nie przyjmuje interesantów dziekanat FTIMS?", "Piątek 10:00 - 11:00", "Piątek 13:00 - 14:00", "Czwartek 8:00 - 10:00", "Wszystkie odpowiedzi są prawidłowe.", 4);
        addQuestion(q7);
        Question q8 = new Question("Ile lat ma Politechnika Łódzka", "75 lat", "74 lata", "65 lat", "84 lata", 1);
        addQuestion(q8);
        Question q9 = new Question("Jaki wydział powstał jako pierwszy na Politechnice Łódzkiej?", "Wydział Chemii Spożywczej", "Wydział Włókienniczy", "Wydział Fizyki Technicznej i Matematyki Stosowanej", "Wydział Organizacji i Zarządzania", 2);
        addQuestion(q9);
        Question q10 = new Question("Od kiedy na Politechnice Łodzkiej działa Uniwersytet Trzeciego wieku?", "Od zawsze", "Od 1978 roku", "Od 2006 roku", "Od 2001 roku", 3);
        addQuestion(q10);
        Question q11 = new Question("Ilu studentów studiuje łącznie na Politechnice Łódzkiej?", "23 542", "3 897", "15 934", "14 936", 4);
        addQuestion(q11);
        Question q12 = new Question("Ile jest kierunków na Politechnice Łódzkiej?", "45", "53", "48", "69", 2);
        addQuestion(q12);
        Question q13 = new Question("Jaką powierzchnię zajmuje Kampus Politechniki Łódzkiej?", "32 hektary", "48 hektarów", "34 hektary", "36 hektarów", 1);
        addQuestion(q13);
        Question q14 = new Question("Ilu jest studentów zagranicznych na Politechnice Łódzkiej?", "Mniej niż 350", "Więcej niż 400", "380", "370", 2);
        addQuestion(q14);
        Question q15 = new Question("W jakich programach wymiany studenckiej mogą brać udział studenci PŁ?", "AIESEC", "Eurostudent", "Erasmus+", "Most", 3);
        addQuestion(q15);
        Question q16 = new Question("Ilu jest profesorów na Politechnice Łódzkiej?", "187", "158", "245", "221", 4);
        addQuestion(q16);
        Question q17 = new Question("Którą rocznicę założenia P. Łodzkiej będziemy obchodzić w 2020 roku?", "75", "65", "70", "80", 1);
        addQuestion(q17);
        Question q18 = new Question("Jedna z jednostek miedzywydziałowych Politechniki Łódzkiej to:", "Kolegium Towaroznawstwa", "Centrum Papiernictwa i Poligrafii", "Centrum Chemii", "Dom studencki nr 1", 1);
        addQuestion(q18);
        Question q19 = new Question("Na ilu kierunkach można studiować w językach obcych?", "16", "11", "14", "20", 1);
        addQuestion(q19);
        Question q20 = new Question("W jakich językach obcych można studiować na P. Łódzkiej?", "angielskim", "niemieckim i angielskim", "angielskim i francuskim", "angielskim, niemieckim i francuskim", 3);
        addQuestion(q20);
        Question q21 = new Question("Jakie były pierwsze wydziały w nowoutworzonej P. Łódzkiej?", "Chemiczny", "Mechaniczny", "Elektryczny", "Wszystkie wymienione", 4);
        addQuestion(q21);
        Question q22 = new Question("Kto może brać udział w prowadzonym przez P. Łódzką Łódzkim Uniwersytecie Dziecięcym?", "Dzieci w wieku 8-10", "Dzieci w wieku 5-16", "Dzieci w wieku 8-12", "Dzieci w wieku 10-12", 3);
        addQuestion(q22);
        Question q23 = new Question("W którym roku powstało funkcjonujące przy PŁ Liceum Ogólnoksztalcące?", "2014", "2005", "2002", "2007", 4);
        addQuestion(q23);
        Question q24 = new Question("Jak nazywał się pierwszy rektor P. Łódzkiej?", "Bohdan Stefanowski", "Sławomir Wiak", "Adam Niewiadomski", "Osman Achmatowicz", 1);
        addQuestion(q24);
        Question q25 = new Question("Na terenie jakiego historycznego obiektu ma siedzibę Biblioteka P. Łódzkiej?", "byłej fabryki Fryderyka Wilhelma Schweikerta", "żadnego", "starego szpitala psychiatrycznego", "starej fabryki włókienniczej", 1);
        addQuestion(q25);
        Question q26 = new Question("Jeden z doktorów honoris causa P. Łódzkiej to:", "Edward Kącki", "Piotr Szczepaniak", "Ewa Marciniak", "Volodymyr Yemyets", 1);
        addQuestion(q26);
        Question q27 = new Question("W którym roku P. Łódzka nadała pierwszy tytuł doktora honoris causa?", "1949", "1970", "1954", "2000", 1);
        addQuestion(q27);
        Question q28 = new Question("Od którego roku działa przy P. Łódzkiej Uniwersytet III wieku?", "2002", "2010", "2006", "2008", 3);
        addQuestion(q28);
        Question q29 = new Question("W którym roku uchwalono obowiązujący aktualnie statut P. Łódzkiej?", "2016", "1945", "2010", "2019", 4);
        addQuestion(q29);
        Question q30 = new Question("Kto ma prawo używania pieczeci P. Łódzkiej?", "Rektorowi", "Dziekanowi FTIMS", "Dziekanowi WEEIA", "Każdy", 1);
        addQuestion(q30);
        Question q31 = new Question("Kto wyraża zgodę na użycie sztandaru P. Łódzkiej?", "Dziekan FTIMS", "Prorektor", "Rektor", "Prezydent Lodzi", 3);
        addQuestion(q31);
        Question q32 = new Question("Jaki jest oficjalny skrót nazwy uczelni?", "PŁ", "PŁdz", "LUoT", "PŁodzka", 1);
        addQuestion(q32);
        Question q33 = new Question("Jaka jest oficjalna nazwa uczelni w języku angielskim?", "Lodz University of Technology", "Technical University of Lodz", "Lodz Politechnic", "Nie wiem", 1);
        addQuestion(q33);
        Question q34 = new Question("Jaka jest oficjalna nazwa uczelni w języku francuskim?", "Universite polytechnique de Lodz", "Polybuda de Lodz", "Universite de Lodz", "Universite de technologie de Lodz", 1);
        addQuestion(q34);
        Question q35 = new Question("Ile Studenckich Kół Naukowych działa przy P. Łódzkiej?", "4", "9", "13", "74", 4);
        addQuestion(q35);
        Question q36 = new Question("Co znajduje się w godle P. Łódzkiej?", "Czapka dziekana", "Klucz francuski", "Ołówek", "Łodz", 4);
        addQuestion(q36);
        Question q37 = new Question("Który z poniższych wykładowców BYŁ kiedyś dziekanem?", "Ewa Marciniak", "Adam Niewiadomski", "Piotr Szczepaniak", "Gabriela Omiecinska", 3);
        addQuestion(q37);
       Question q40 = new Question("Jak nazywa się radio studenckie P. Łódzkiej?", "Głos Politechniki", "ŻAK", "RPL", "Radiopolis", 2);
       addQuestion(q40);
        Question q41 = new Question("Ile Domów Studenta ma P. Łódzka?", "9", "8", "7", "10", 1);
        addQuestion(q41);
        Question q42 = new Question("Kto prowadzi wykłady z Programowania Obiektowego?", "Arkadiusz Tomczyk", "Adam Niewiadomski", "Kamil Stokfiszewski", "Roman Krasiukianis", 1);
        addQuestion(q42);
        Question q43 = new Question("Ile pięter ma LODEX?", "5", "4", "3", "2", 1);
        addQuestion(q43);
       Question q45 = new Question("Jakie są średnie zarobki po ukończeniu P. Łódzkiej?", "4000 brutto", "6000 brutto", "8000 brutto", "3000 brutto", 2);
        addQuestion(q45);
        Question q47 = new Question("Kto prowadzi wykłady z XMLa?", "Joanna Ochelska-Mierzejewska", "Arkadiusz Tomczyk", "Alicja Nowacka", "Gabriela Omiecińska", 1);
        addQuestion(q47);
        Question q48 = new Question("Gdzie odbywają się laboratoria z sieci komputerowych?", "B9", "B14", "B19", "B15", 3);
        addQuestion(q48);
        Question q49 = new Question("Kto prowadzi wykłady z Matematyki Dyskretnej?", "Jacek Rogowski", "Jan Rogowski", "Ewa Marciniak", "Igor Kossowski", 1);
        addQuestion(q49);
        Question q50 = new Question("Kto prowadzi wykłady z Matematyki I?", "Igor Kossowski", "Ewa Marciniak", "Kamil Stokfiszewski", "Piotr Gamorski", 2);
        addQuestion(q50);


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
