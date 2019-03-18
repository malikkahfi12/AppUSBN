package com.example.asus.qunasta.DBHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.asus.qunasta.Model.Category;
import com.example.asus.qunasta.Model.Question;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "DatabaseQuiz.db";
    private static final int DATABASE_VERSION = 1;

    private static DBHelper instance;

    public static synchronized DBHelper getInstance(Context context)
    {
        if(instance == null)
            instance = new DBHelper(context);
        return instance;
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
    GET ALL CATEGORIES FROM DB
     */
    public List<Category> getAllCategories()
    {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Category", null);
        List<Category>categories =  new ArrayList<>();
        if(cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                Category category =  new Category(
                        cursor.getInt(cursor.getColumnIndex("ID")),
                        cursor.getString(cursor.getColumnIndex("Name")),
                        cursor.getString(cursor.getColumnIndex("Image")));
                categories.add(category);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return categories;
    }

    public List <Category> getCategoryByName(String name)
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect={"ID,Name,Image"};
        String tableName = "Category";
        qb.setTables(tableName);
        Cursor cursor = qb.query(db,sqlSelect,"Name LIKE ?", new String[]{"%"+name+"%"},null,null,null);
        List<Category> result = new ArrayList<>();
        if(cursor.moveToFirst())
        {
            do {
                Category category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                category.setName(cursor.getString(cursor.getColumnIndex("Name")));
                category.setImage(cursor.getString(cursor.getColumnIndex("Image")));
                result.add(category);

            }while (cursor.moveToNext());

        }
        return result;
    }

    /*
    GET ALL QUESTION FROM DB BY CATEGORY
     */
    public List<Question> getQuestionByCategory(int category)
    {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM Question WHERE CategoryID = %d ORDER BY RANDOM() LIMIT 30",category), null);
        List<Question> questions =  new ArrayList<>();
        if(cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                Question question = new Question(cursor.getInt(cursor.getColumnIndex("ID")),
                        cursor.getString(cursor.getColumnIndex("QuestionText")),
                        cursor.getString(cursor.getColumnIndex("QuestionImage")),
                        cursor.getString(cursor.getColumnIndex("AnswerA")),
                        cursor.getString(cursor.getColumnIndex("AnswerB")),
                        cursor.getString(cursor.getColumnIndex("AnswerC")),
                        cursor.getString(cursor.getColumnIndex("AnswerD")),
                        cursor.getString(cursor.getColumnIndex("CorrectAnswer")),
                        cursor.getInt(cursor.getColumnIndex("IsImageQuestion"))==0?Boolean.FALSE:Boolean.TRUE,
                        cursor.getInt(cursor.getColumnIndex("CategoryID")));
                questions.add(question);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return questions;
    }

    public List<String> getNames(){
        SQLiteDatabase db = instance.getWritableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect={"Name"};
        String tableName="Category";

        qb.setTables(tableName);
        Cursor cursor = qb.query(db, sqlSelect, null,null, null, null, null);
        List<String> result = new ArrayList<>();
        if(cursor.moveToFirst())
        {
            do{
                result.add(cursor.getString(cursor.getColumnIndex("Name")));
            }while (cursor.moveToNext());
        }
        return result;
    }
}
