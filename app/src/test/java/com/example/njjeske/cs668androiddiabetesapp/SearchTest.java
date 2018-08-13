package com.example.njjeske.cs668androiddiabetesapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class SearchTest {
    ArrayList<DB_Object> databaseList = new ArrayList<>();
    Search s;

    @Before
    public void setUp() {
        s = new Search(databaseList);
    }

    @After
    public void cleanUp() {
        databaseList = new ArrayList<>();
    }

    private void add1BGL() {
        Random r = new Random();
        int rBGL = r.nextInt((600 - 40) + 1) + 40;
        DB_Object obj1 = new DB_Object("Blood Glucose", "1/1/2018", "1:0", rBGL + "");
        databaseList.add(obj1);
//        System.out.println("Added: "+obj1.toString());
    }

    private void add1Food() {
        DB_Object obj1 = new DB_Object("Diet", "1/1/2018", "1:0", "strawberry");
        databaseList.add(obj1);
//        System.out.println("Added: "+obj1.toString());
    }

    private void add1Exercise() {
        DB_Object obj1 = new DB_Object("Exercise", "1/1/2018", "1:0", "walking");
        databaseList.add(obj1);
//        System.out.println("Added: "+obj1.toString());
    }

    private void add1Medication() {
        DB_Object obj1 = new DB_Object("Medication", "1/1/2018", "1:0", "tylenol");
        databaseList.add(obj1);
//        System.out.println("Added: "+obj1.toString());
    }

    private int addedRandom(String type) {
        Random r = new Random();
        int temp = r.nextInt((10 - 1) + 1) + 1;
        for (int i = 0; i < temp; i++) {
            switch (type) {
                case "Blood Glucose":
                    add1BGL();
                    break;
                case "Diet":
                    add1Food();
                    break;
                case "Exercise":
                    add1Exercise();
                    break;
                case "Medication":
                    add1Medication();
                    break;
            }
        }
        System.out.println(temp + " items added");
        return temp;

    }

    private void checkAll(boolean bgl, boolean diet, boolean exercise, boolean medication) {
        s.check_bgl = bgl;
        s.check_diet = diet;
        s.check_exercise = exercise;
        s.check_medication = medication;
    }

    @Test
    public void getDb() {
        assertEquals(s.getDb().size(), 0);
        add1BGL();
        s = new Search(databaseList);
        assertEquals(s.getDb().size(), 1);
        add1BGL();
        s = new Search(databaseList);
        assertEquals(s.getDb().size(), 2);
        databaseList.clear();
        assertEquals(s.getDb().size(), 0);
        s = new Search(null);
        assertNull(s.getDb());
    }

    @Test
    public void setDb() {
        assertEquals(s.getDb().size(), 0);
        for (int i = 0; i < 10; i++) {
            add1BGL();
        }
        assertEquals(s.getDb().size(), 10);
        add1BGL();
        assertEquals(s.getDb().size(), 11);
        s.setDb(new ArrayList<DB_Object>());
        assertEquals(s.getDb().size(), 0);
        databaseList.remove(0);
        s.setDb(databaseList);
        assertEquals(s.getDb().size(), 10);
        databaseList.clear();
        s.setDb(databaseList);
        assertEquals(s.getDb().size(), 0);
        s.setDb(null);
        assertNull(s.getDb());
    }

    @Test
    public void filterByType() {
        assertEquals(s.filterByType().size(), 0);
        System.out.println("Filter size: " + s.filterByType().size());
        s.check_bgl = true;
        for (int i = 0; i < 5; i++) {
            add1BGL();
        }
        add1Food();
        System.out.println("DB size: " + s.getDb().size());
        assertEquals(s.getDb().size(), 6);

        // bgl only
        ArrayList<DB_Object> temp = s.filterByType();
        assertNotNull(temp);
        System.out.println("Filter size: " + s.filterByType().size());
        assertEquals(s.filterByType().size(), 5);

        add1Food();
        add1Exercise();
        add1Medication(); //9 items
        System.out.println("DB size: " + s.getDb().size());

        // 2 types only
        checkAll(false, true, false, true);
        assertEquals(s.getDb().size(), 9);
        assertEquals(s.filterByType().size(), 3);
        System.out.println("Filter size: " + s.filterByType().size());

        // medication only
        checkAll(false, false, false, true);
        assertEquals(s.filterByType().size(), 1);
        System.out.println("Filter size: " + s.filterByType().size());

        // exercise only
        checkAll(false, false, true, false);
        assertEquals(s.filterByType().size(), 1);
        System.out.println("Filter size: " + s.filterByType().size());

        // diet only
        checkAll(false, true, false, false);
        assertEquals(s.filterByType().size(), 2);
        System.out.println("Filter size: " + s.filterByType().size());

        databaseList.clear();
        System.out.println("DB size: " + s.getDb().size());
        int r = addedRandom("Blood Glucose");
        System.out.println("DB size: " + s.getDb().size());
        assertEquals(s.getDb().size(), r);
        checkAll(true, false, false, false);
        int r2 = addedRandom("Diet");
        System.out.println("DB size: " + s.getDb().size());
        assertEquals(s.getDb().size(), r + r2);

        // one type only, random size added
        assertEquals(s.filterByType().size(), r);
        System.out.println("Filter size: " + s.filterByType().size());

    }

    @Test
    public void filterByType1() {
    }

    @Test
    public void filterByDate() {
    }

    @Test
    public void filterByDate1() {
    }

    @Test
    public void filterByTime() {
    }

    @Test
    public void filterByTime1() {
    }

    @Test
    public void filterByBglValue() {
    }

    @Test
    public void filterByBglValue1() {
    }

    @Test
    public void filterByKeyWords() {
    }

    @Test
    public void filterByKeyWords1() {
    }

    @Test
    public void isAfterDate() {
    }

    @Test
    public void isBeforeDate() {
    }

    @Test
    public void isAfterTime() {
    }

    @Test
    public void isBetweenBglValue() {
    }

    @Test
    public void containsWords() {
    }
}