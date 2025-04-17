package com.example.fitnesstracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FitnessDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "fitness.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_WEIGHT = "weight_entries";
    public static final String TABLE_WORKOUTS = "workouts";
    public static final String TABLE_DIET_PLANS = "diet_plans";
    
    // Common column names
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    
    // Weight table columns
    public static final String COLUMN_WEIGHT_VALUE = "weight_value";
    
    // Workout table columns
    public static final String COLUMN_WORKOUT_TYPE = "workout_type";
    public static final String COLUMN_WORKOUT_DURATION = "duration";
    public static final String COLUMN_CALORIES_BURNED = "calories_burned";
    
    // Diet plan columns
    public static final String COLUMN_DIET_TYPE = "diet_type";
    public static final String COLUMN_CALORIES_CONSUMED = "calories_consumed";

    // Create table statements
    private static final String CREATE_WEIGHT_TABLE = "CREATE TABLE " + TABLE_WEIGHT + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_DATE + " INTEGER NOT NULL, " +
            COLUMN_WEIGHT_VALUE + " REAL NOT NULL);";
            
    private static final String CREATE_WORKOUTS_TABLE = "CREATE TABLE " + TABLE_WORKOUTS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_DATE + " INTEGER NOT NULL, " +
            COLUMN_WORKOUT_TYPE + " TEXT NOT NULL, " +
            COLUMN_WORKOUT_DURATION + " INTEGER, " +
            COLUMN_CALORIES_BURNED + " REAL);";
            
    private static final String CREATE_DIET_PLANS_TABLE = "CREATE TABLE " + TABLE_DIET_PLANS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_DATE + " INTEGER NOT NULL, " +
            COLUMN_DIET_TYPE + " TEXT NOT NULL, " +
            COLUMN_CALORIES_CONSUMED + " REAL);";

    public FitnessDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WEIGHT_TABLE);
        db.execSQL(CREATE_WORKOUTS_TABLE);
        db.execSQL(CREATE_DIET_PLANS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For simplicity, drop and recreate tables on upgrade
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIET_PLANS);
        onCreate(db);
    }
    
    // Weight tracking methods
    public long addWeightEntry(double weight) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, System.currentTimeMillis());
        values.put(COLUMN_WEIGHT_VALUE, weight);
        return db.insert(TABLE_WEIGHT, null, values);
    }
    
    public double getLatestWeight() {
        SQLiteDatabase db = getReadableDatabase();
        double weight = 0.0;
        
        Cursor cursor = db.query(
                TABLE_WEIGHT,
                new String[]{COLUMN_WEIGHT_VALUE},
                null,
                null,
                null,
                null,
                COLUMN_DATE + " DESC",
                "1"
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            weight = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT_VALUE));
            cursor.close();
        }
        
        return weight;
    }
    
    // Workout tracking methods
    public long addWorkout(String type, int durationMinutes, double caloriesBurned) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, System.currentTimeMillis());
        values.put(COLUMN_WORKOUT_TYPE, type);
        values.put(COLUMN_WORKOUT_DURATION, durationMinutes);
        values.put(COLUMN_CALORIES_BURNED, caloriesBurned);
        return db.insert(TABLE_WORKOUTS, null, values);
    }
    
    public double getTotalWorkoutCalories() {
        SQLiteDatabase db = getReadableDatabase();
        double total = 0.0;
        
        Cursor cursor = db.rawQuery(
                "SELECT SUM(" + COLUMN_CALORIES_BURNED + ") FROM " + TABLE_WORKOUTS,
                null
        );
        
        if (cursor != null && cursor.moveToFirst() && !cursor.isNull(0)) {
            total = cursor.getDouble(0);
            cursor.close();
        }
        
        return total;
    }
    
    public double getWorkoutCaloriesForDate(long date) {
        SQLiteDatabase db = getReadableDatabase();
        double calories = 0.0;
        
        // Calculate start and end of the day
        long startOfDay = date - (date % (24 * 60 * 60 * 1000));
        long endOfDay = startOfDay + (24 * 60 * 60 * 1000) - 1;
        
        Cursor cursor = db.rawQuery(
                "SELECT SUM(" + COLUMN_CALORIES_BURNED + ") FROM " + TABLE_WORKOUTS +
                " WHERE " + COLUMN_DATE + " BETWEEN ? AND ?",
                new String[]{String.valueOf(startOfDay), String.valueOf(endOfDay)}
        );
        
        if (cursor != null && cursor.moveToFirst() && !cursor.isNull(0)) {
            calories = cursor.getDouble(0);
            cursor.close();
        }
        
        return calories;
    }
    
    // Diet plan tracking methods
    public long addDietPlan(String type, double caloriesConsumed) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, System.currentTimeMillis());
        values.put(COLUMN_DIET_TYPE, type);
        values.put(COLUMN_CALORIES_CONSUMED, caloriesConsumed);
        return db.insert(TABLE_DIET_PLANS, null, values);
    }
    
    // Get workout entries for a specific date
    public List<WorkoutEntry> getWorkoutsForDate(long date) {
        List<WorkoutEntry> workouts = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        
        // Calculate start and end of the day
        long startOfDay = date - (date % (24 * 60 * 60 * 1000));
        long endOfDay = startOfDay + (24 * 60 * 60 * 1000) - 1;
        
        Cursor cursor = db.query(
                TABLE_WORKOUTS,
                null,
                COLUMN_DATE + " BETWEEN ? AND ?",
                new String[]{String.valueOf(startOfDay), String.valueOf(endOfDay)},
                null,
                null,
                COLUMN_DATE + " ASC"
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                WorkoutEntry entry = new WorkoutEntry();
                entry.id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
                entry.date = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                entry.type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WORKOUT_TYPE));
                entry.duration = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WORKOUT_DURATION));
                entry.calories = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED));
                workouts.add(entry);
            } while (cursor.moveToNext());
            cursor.close();
        }
        
        return workouts;
    }
    
    // Get diet plans for a specific date
    public List<DietPlanEntry> getDietPlansForDate(long date) {
        List<DietPlanEntry> dietPlans = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        
        // Calculate start and end of the day
        long startOfDay = date - (date % (24 * 60 * 60 * 1000));
        long endOfDay = startOfDay + (24 * 60 * 60 * 1000) - 1;
        
        Cursor cursor = db.query(
                TABLE_DIET_PLANS,
                null,
                COLUMN_DATE + " BETWEEN ? AND ?",
                new String[]{String.valueOf(startOfDay), String.valueOf(endOfDay)},
                null,
                null,
                COLUMN_DATE + " ASC"
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                DietPlanEntry entry = new DietPlanEntry();
                entry.id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
                entry.date = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                entry.type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIET_TYPE));
                entry.calories = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_CONSUMED));
                dietPlans.add(entry);
            } while (cursor.moveToNext());
            cursor.close();
        }
        
        return dietPlans;
    }
    
    // Get average daily workout calories for the last 7 days
    public double getAverageDailyWorkoutCalories() {
        SQLiteDatabase db = getReadableDatabase();
        double average = 0.0;
        
        long now = System.currentTimeMillis();
        long sevenDaysAgo = now - (7 * 24 * 60 * 60 * 1000);
        
        Cursor cursor = db.rawQuery(
                "SELECT AVG(daily_total) FROM (" +
                "   SELECT SUM(" + COLUMN_CALORIES_BURNED + ") as daily_total, " +
                "   strftime('%Y-%m-%d', " + COLUMN_DATE + "/1000, 'unixepoch') as day " +
                "   FROM " + TABLE_WORKOUTS +
                "   WHERE " + COLUMN_DATE + " BETWEEN ? AND ? " +
                "   GROUP BY day" +
                ")",
                new String[]{String.valueOf(sevenDaysAgo), String.valueOf(now)}
        );
        
        if (cursor != null && cursor.moveToFirst() && !cursor.isNull(0)) {
            average = cursor.getDouble(0);
            cursor.close();
        }
        
        return average;
    }
    
    // Get recent workouts for the report section
    public List<WorkoutEntry> getRecentWorkouts(int limit) {
        List<WorkoutEntry> workouts = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        
        Cursor cursor = db.query(
                TABLE_WORKOUTS,
                null,
                null,
                null,
                null,
                null,
                COLUMN_DATE + " DESC",
                String.valueOf(limit)
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                WorkoutEntry entry = new WorkoutEntry();
                entry.id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
                entry.date = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                entry.type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WORKOUT_TYPE));
                entry.duration = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WORKOUT_DURATION));
                entry.calories = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED));
                workouts.add(entry);
            } while (cursor.moveToNext());
            cursor.close();
        }
        
        return workouts;
    }
    
    // Get recent diet plans for the report section
    public List<DietPlanEntry> getRecentDietPlans(int limit) {
        List<DietPlanEntry> dietPlans = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        
        Cursor cursor = db.query(
                TABLE_DIET_PLANS,
                null,
                null,
                null,
                null,
                null,
                COLUMN_DATE + " DESC",
                String.valueOf(limit)
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                DietPlanEntry entry = new DietPlanEntry();
                entry.id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
                entry.date = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                entry.type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIET_TYPE));
                entry.calories = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_CONSUMED));
                dietPlans.add(entry);
            } while (cursor.moveToNext());
            cursor.close();
        }
        
        return dietPlans;
    }
    
    // Data classes for entries
    public static class WorkoutEntry {
        public long id;
        public long date;
        public String type;
        public int duration;
        public double calories;
    }
    
    public static class DietPlanEntry {
        public long id;
        public long date;
        public String type;
        public double calories;
    }
}
