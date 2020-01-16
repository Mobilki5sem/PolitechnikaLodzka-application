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
    private static final int DATABASE_VERSION = 6;
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
        Question q1 = new Question("Kiedy utworzono Politechnike Lodzka", "A)24 maja 1945 ", "B)23 maja 1939", "C)15 maja 1945", "D)19 maja 1945", 1);
        addQuestion(q1);
        Question q2 = new Question("Kto jest rektorem Politechniki Lodzkiej?", "A)prof. dr hab. inż. Ireneusz Zbiciński", "B)prof. dr hab. inż. Sławomir Wiak", "C)prof. dr hab. inż. Dariusz Gawin", "D)prof. dr hab. Grzegorz Bąk", 2);
        addQuestion(q2);
        Question q3 = new Question("Czy Politechnika Lodzka uzyskala status uczelni badawczej?", "A)Tak. Otrzymała w 2019r.", "B)Nie", "C)Tak. Otrzymała w 2016r.", "D)Tak. Otrzymała w 2015r.", 2);
        addQuestion(q3);
        Question q4 = new Question("Ile jest wydzialow na Politechnice Lodzkiej?", "A)6", "B)8", "C)10", "D)9", 4);
        addQuestion(q4);
        Question q5 = new Question("Gdzie miesci sie rektorat Politechniki Lodzkiej?", "A)Ks. Ignacego Skorupki 10/12, 90-001 Łódź", "B)Wólczańska 215, 90-001 Łódź", "C)Ks. Ignacego Skorupki 21, 90-001 Łódź", "D)aleja Politechniki 10, 93-590 Łódź", 1);
        addQuestion(q5);
        Question q6 = new Question("Jak nazywa sie samorzad uczniowski FTIMS?", "A)Samorząd FTIMS", "B)FTIMSiaki", "C)WRS FTIMS", "D)ZSBD FTIMS", 3);
        addQuestion(q6);
        Question q7 = new Question("Kiedy nie przyjmuje interesantow dziekanat FTIMS?", "A)Piątek 10:00 - 11:00", "B)Piątek 13:00 - 14:00", "C)Czwartek 8:00 - 10:00", "D)Wszystkie odpowiedzi są prawidłowe.", 4);
        addQuestion(q7);
        Question q8 = new Question("Ile lat ma Politechnika Lodzka", "A)75 lat", "B)74 lata", "C)65 lat", "D)84 lata", 1);
        addQuestion(q8);
        Question q9 = new Question("Jaki wydział powstał jako pierwszy na Politechnice Lodzkiej?", "A)Wydział Chemii Spożywczej", "B)Wydział Włókienniczy", "C)Wydział Fizyki Technicznej i Matematyki Stosowanej", "D)Wydział Organizacji i Zarządzania", 2);
        addQuestion(q9);
        Question q10 = new Question("Od kiedy na Politechnice Lodzkiej działa Uniwersytet Trzeciego wieku?", "A)Od zawsze", "B)Od 1978 roku", "C)Od 2006 roku", "D)Od 2001 roku", 3);
        addQuestion(q10);
        Question q11 = new Question("Ilu studentow studiuje lacznie na Politechnice Lodzkiej?", "A)23 542", "B)3 897", "C)15 934", "D)14 936", 4);
        addQuestion(q11);
        Question q12 = new Question("Ilu jest kierunkow na Politechnice Lodzkiej?", "A)45", "B)53", "C)48", "D)69", 2);
        addQuestion(q12);
        Question q13 = new Question("Jaka powierzchnie zajmuje Kampus Politechniki Lodzkiej?", "A)32 hektary", "B)48 hektarow", "C)34 hektary", "D)36 hektarow", 1);
        addQuestion(q13);
        Question q14 = new Question("Ilu jest studentow zagranicznych na Politechnice Lodzkiej?", "A)Mniej niż 350", "B)Więcej niż 400", "C)380", "D)370", 2);
        addQuestion(q14);
        Question q15 = new Question("W jakich programach wymiany studenckiej moga brac udzial studenci PL?", "A)AIESEC", "B)Eurostudent", "C)Erasmus+", "D)Most", 3);
        addQuestion(q15);
        Question q16 = new Question("IIlu jest profesorow na Politechnice Lodzkiej?", "A)187", "B)158", "C)245", "D)221", 4);
        addQuestion(q16);
        Question q17 = new Question("Ktora rocznice zalozenia P. Lodzkiej bedziemy obchodzic w 2020 roku?", "A)75", "B)65", "C)70", "D)80", 1);
        addQuestion(q17);
        Question q18 = new Question("Jedna z jednostek miedzywydzialowych Politechniki Lodzkiej to:", "A)Kolegium Towaroznawstwa", "B)Centrum Papiernictwa i Poligrafii", "C)Centrum Chemii", "D)Dom studencki nr 1", 1);
        addQuestion(q18);
        Question q19 = new Question("Na ilu kierunkach mozna studiowac w jezykach obcych?", "A)16", "B)11", "C)14", "D)20", 1);
        addQuestion(q19);
        Question q20 = new Question("W jakich jezykach obcych mozna studiowac na P. Lodzkiej?", "A)angielskim", "B)niemieckim i angielskim", "C)angielskim i francuskim", "D)angielskim, niemieckim i francuskim", 3);
        addQuestion(q20);
        Question q21 = new Question("Jakie byly pierwsze wydzialy w nowoutworzonej P. Lodzkiej?", "A)Chemiczny", "B)Mechaniczny", "C)Elektryczny", "D)Wszystkie wymienione", 4);
        addQuestion(q21);
        Question q22 = new Question("Kto moze brac udzial w prowadzonym przez P. Lodzka Lodzkim Uniwersytecie Dzieciecym?", "A)Dzieci w wieku 8-10", "B)Dzieci w wieku 5-16", "C)Dzieci w wieku 8-12", "D)Dzieci w wieku 10-12", 3);
        addQuestion(q22);
        Question q23 = new Question("W ktorym roku powstalo funkcjonujace przy PŁ Liceum Ogolnoksztalcace?", "A)2014", "B)2005", "C)2002", "D)2007", 4);
        addQuestion(q23);
        Question q24 = new Question("Jak nazywal sie pierwszy rektor P. Lodzkiej?", "A)Bohdan Stefanowski", "B)Sławomir Wiak", "C)Adam Niewiadomski", "D)Osman Achmatowicz", 1);
        addQuestion(q24);
        Question q25 = new Question("Na terenie jakiego historycznego obiektu ma siedzibe Biblioteka P. Lodzkiej?", "A)bylej fabryki Fryderyka Wilhelma Schweikerta", "B)zadnego", "C)starego szpitala psychiatrycznego", "D)starej fabryki wlokienniczej", 1);
        addQuestion(q25);
        Question q26 = new Question("Jeden z doktorow honoris causa P. Lodzkiej to:", "A)Edward Kącki", "B)Piotr Szczepaniak", "C)Ewa Marciniak", "D)Volodymyr Yemyets", 1);
        addQuestion(q26);
        Question q27 = new Question("W ktorym roku P. Lodzka nadala pierwszy tytul doktora honoris causa?", "A)1949", "B)1970", "C)1954", "D)2000", 1);
        addQuestion(q27);
        Question q28 = new Question("Od ktorego roku dziala przy P. Lodzkiej Uniwersytet III wieku?", "A)2002", "B)2010", "C)2006", "D)2008", 3);
        addQuestion(q28);
        Question q29 = new Question("W ktorym roku uchwalono obowiazujacy aktualnie statut P. Lodzkiej?", "A)2016", "B)1945", "C)2010", "D)2019", 4);
        addQuestion(q29);
        Question q30 = new Question("Kto ma prawo uzywania pieczeci P. Lodzkiej?", "A)Rektorowi", "B)Dziekanowi FTIMS", "C)Dziekanowi WEEIA", "D)Kazdy", 1);
        addQuestion(q30);
        Question q31 = new Question("Kto wyraza zgode na uzycie sztandaru P. Lodzkiej?", "A)Dziekan FTIMS", "B)Prorektor", "C)Rektor", "D)Prezydent Lodzi", 3);
        addQuestion(q31);
        Question q32 = new Question("Jaki jest oficjalny skrot nazwy uczelni?", "A)PL", "B)PLdz", "C)LUoT", "D)PLodzka", 1);
        addQuestion(q32);
        Question q33 = new Question("Jaka jest oficjalna nazwa uczelni w jezyku angielskim?", "A)Lodz University of Technology", "B)Technical University of Lodz", "C)Lodz Politechnic", "D)Nie wiem", 1);
        addQuestion(q33);
        Question q34 = new Question("Jaka jest oficjalna nazwa uczelni w jezyku francuskim?", "A)Universite polytechnique de Lodz", "B)Polybuda de Lodz", "C)Universite de Lodz", "D)Universite de technologie de Lodz", 1);
        addQuestion(q34);
        Question q35 = new Question("Ile Studenckich Kol Naukowych dziala przy P. Lodzkiej?", "A)4", "B)9", "C)13", "D)74", 4);
        addQuestion(q35);
        Question q36 = new Question("Co znajduje sie w godle P. Lodzkiej?", "A)Czapka dziekana", "B)Klucz francuski", "C)Olowek", "D)Lodz", 4);
        addQuestion(q36);
        Question q37 = new Question("Ktory z ponizszych wykladowcow BYL kiedys dziekanem?", "A)Ewa Marciniak", "B)Adam Niewiadomski", "C)Piotr Szczepaniak", "D)Gabriela Omiecinska", 3);
        addQuestion(q37);
//        Question q38 = new Question("Co znajduje sie na sztandarze P. Lodzkiej?", "A)Adas", "B)Ten od enduro", "C)Sowizdrzal pierdolony", "D)Koks", 3);
//        addQuestion(q38);
//        Question q39 = new Question("Jak wyglada wzor czapki studenckiej?", "A)Adas", "B)Ten od enduro", "C)Sowizdrzal pierdolony", "D)Koks", 3);
//        addQuestion(q39);
       Question q40 = new Question("Jak nazywa sie radio studenckie P. Lodzkiej?", "A)Glos Politechniki", "B)ZAK", "C)RPL", "D)Radiopolis", 2);
       addQuestion(q40);
        Question q41 = new Question("Ile Domow Studentama P. Lodzka?", "A)9", "B)8", "C)7", "D)10", 1);
        addQuestion(q41);
        Question q42 = new Question("Kto prowadzi wyklady z Programowania Obiektowego?", "A)Arkadiusz Tomczyk", "B)Adam Niewiadomski", "C)Kamil Stokfiszewski", "D)Roman Krasiukianis", 1);
        addQuestion(q42);
        Question q43 = new Question("Ile pieter ma LODEX?", "A)5", "B)4", "C)3", "D)2", 1);
        addQuestion(q43);
//        Question q44 = new Question("Kiedy powstal LODEX?", "A)Adas", "B)Ten od enduro", "C)Sowizdrzal pierdolony", "D)Koks", 3);
//        addQuestion(q44);
       Question q45 = new Question("Jakie sa srednie zarobki po ukonczeniu P. Lodzkiej?", "A)4000 brutto", "B)6000 brutto", "C)8000 brutto", "D)3000 brutto", 2);
        addQuestion(q45);
//        Question q46 = new Question("Ile w przyblizeniu sal ma LODEX?", "A)Adas", "B)Ten od enduro", "C)Sowizdrzal pierdolony", "D)Koks", 3);
//        addQuestion(q46);
        Question q47 = new Question("Kto prowadzi wyklady z XMLa?", "A)Joanna Ochelska-Mierzejewska", "B)Arkadiusz Tomczyk", "C)Alicja Nowacka", "D)Gabriela Omiecinska", 1);
        addQuestion(q47);
        Question q48 = new Question("Gdzie odbywaja sie laboratoria z sieci komputerowych?", "A)B9", "B)B14", "C)B19", "D)B15", 3);
        addQuestion(q48);
        Question q49 = new Question("Kto prowadzi wyklady z Matematyki Dyskretnej?", "A)Jacek Rogowski", "B)Jan Rogowski", "C)Ewa Marciniak", "D)Igor Kossowski", 1);
        addQuestion(q49);
        Question q50 = new Question("Kto prowadzi wyklady z Matematyki I?", "A)Igor Kossowski", "B)Ewa Marciniak", "C)Kamil Stokfiszewski", "D)Piotr Gamorski", 2);
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
