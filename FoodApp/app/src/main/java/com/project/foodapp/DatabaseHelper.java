package com.project.foodapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Имя базы данных и версия
    private static final String DATABASE_NAME = "mydatabase";
    private static final int DATABASE_VERSION = 62; //После каждого обновления базы меняй версию на +1
    private Context context;
    String fullPath;
    File imgFile;
    ContentValues values = new ContentValues();

    // Конструктор
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    private void unzipAssets() {
        try {
            InputStream inputStream = context.getAssets().open("imgs.zip");
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            ZipEntry zipEntry;
            byte[] buffer = new byte[1024];
            int count;
            int fileCounter = 1;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (!zipEntry.isDirectory() && zipEntry.getName().endsWith(".jpg") && fileCounter < 5) {
                    String fullPath = context.getFilesDir().getAbsolutePath() + File.separator + "imgs" + File.separator;
                    File dir = new File(fullPath);
                    dir.mkdirs();
                    File outputFile = new File(dir, fileCounter + ".jpg");
                    FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

                    while ((count = zipInputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, count);
                    }
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    fileCounter++;
                }
            }

            zipInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Создание таблицы
    @Override
    public void onCreate(SQLiteDatabase db) {
        unzipAssets();
        db.execSQL("CREATE TABLE IF NOT EXISTS Recipes (name TEXT PRIMARY KEY, section TEXT, ingredients TEXT, process TEXT, photo BLOB)");

        db.execSQL("CREATE TABLE IF NOT EXISTS Favorite (name TEXT PRIMARY KEY, ingredients TEXT, process TEXT, photo BLOB, notes TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS CookBook (name TEXT PRIMARY KEY, ingredients TEXT, process TEXT, photo BLOB)");

        // Добавление значения в таблицу Recipes
        values.clear();
        //1
        values.put("name", "Grilled peach, chicken & feta salad");
        values.put("section", "Salad");
        values.put("ingredients","400g tender mini chicken fillets\n" +
                "3 tbsp extra virgin olive oil\n" +
                "4 sun-ripened peaches, quartered and pitted\n" +
                "4 tsp aged sherry vinegar\n" +
                "1 tbsp pure honey\n" +
                "1 fiery red chilli, minced\n" +
                "110g mixed herb salad greens\n" +
                "100g crumbled feta cheese");
        values.put("process", "STEP 1: Heat a griddle pan. Oil and season the chicken. Cook for 3-4 mins each side. Set aside.\n" +
                "STEP 2: Toss peaches in oil and pepper. Grill 1-2 mins each side.\n" +
                "STEP 3: Combine remaining oil, vinegar, honey, and chilli. Mix with salad. Top with chicken, peaches, and feta. Drizzle with chicken juices. Serve immediately.");

        fullPath = context.getFilesDir().getAbsolutePath() + File.separator + "imgs" + File.separator + "1.jpg";
        imgFile = new File(fullPath);
        if (imgFile.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(imgFile);
                byte[] imageByteArray = new byte[(int) imgFile.length()];
                fis.read(imageByteArray);
                fis.close();
                values.put("photo", imageByteArray);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        db.insert("Recipes", null, values);


        values.clear();
        //2
        values.put("name", "Crisp Verdant Bean Medley ");
        values.put("section", "Salad");
        values.put("ingredients","600g fresh green beans, ends removed\n" +
                "3 tbsp extra virgin olive oil\n" +
                "2 large garlic cloves, finely sliced\n" +
                "1 tbsp aged balsamic vinegar\n" +
                "3 tbsp freshly chopped mint");
        values.put("process", "STEP 1: Cook the beans in boiling salted water for 5-6 mins, until just tender, then drain and refresh under cold running water. Shake well to remove excess water, then pat dry with kitchen paper. You can do this the day before and keep the cooked beans covered in the fridge.\n" +
                "STEP 2: Heat 1 tbsp of the oil in a small pan, add the garlic and fry quickly until crisp and lightly golden. Tip into a bowl with the oil from the pan and leave to cool. This can be done the day before");

        fullPath = context.getFilesDir().getAbsolutePath() + File.separator + "imgs" + File.separator + "2.jpg";
        imgFile = new File(fullPath);
        if (imgFile.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(imgFile);
                byte[] imageByteArray = new byte[(int) imgFile.length()];
                fis.read(imageByteArray);
                fis.close();
                values.put("photo", imageByteArray);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        db.insert("Recipes", null, values);


        values.clear();
        //3
        values.put("name", "Spiced Chickpea-Stuffed Vegan Spuds");
        values.put("section", "Lunch");
        values.put("ingredients","4 whole sweet potatoes\n" +
                "1 tbsp virgin coconut oil\n" +
                        "1 ½ tsp whole cumin seeds\n" +
                        "1 robust onion, finely chopped\n" +
                        "2 aromatic garlic cloves, minced\n" +
                        "A thumb-sized fresh ginger, grated\n" +
                        "1 vibrant green chilli, minced\n" +
                        "1 tsp traditional garam masala\n" +
                        "1 tsp ground coriander seeds\n" +
                        "½ tsp golden turmeric powder\n" +
                        "2 tbsp tikka masala blend\n" +
                        "2 cans (400g each) diced tomatoes\n" +
                        "2 cans (400g each) chickpeas, rinsed and drained\n" +
                        "Fresh lemon wedges and cilantro leaves for garnish");
                values.put("process", "STEP 1: Preheat oven to 200C/180C fan/gas 6. Fork-prick sweet potatoes, place on a tray, and roast for 45 mins or until soft.\n" +
                        "STEP 2: In a saucepan on medium heat, melt coconut oil. Fry cumin seeds for 1 min, add onions, and sauté for 7-10 mins.\n" +
                        "STEP 3: Add garlic, ginger, and chilli; cook for 2-3 mins. Stir in spices and tikka masala paste. After 2 mins, pour in tomatoes, simmer, then add chickpeas. Cook 20 mins. Season.\n" +
                        "STEP 4: Split roasted potatoes on plates. Top with chickpea curry, a lemon squeeze, and garnish with coriander. Serve.");

        fullPath = context.getFilesDir().getAbsolutePath() + File.separator + "imgs" + File.separator + "3.jpg";
        imgFile = new File(fullPath);
        if (imgFile.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(imgFile);
                byte[] imageByteArray = new byte[(int) imgFile.length()];
                fis.read(imageByteArray);
                fis.close();
                values.put("photo", imageByteArray);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        db.insert("Recipes", null, values);

        values.clear();
        //4
        values.put("name", " Savory Satay Chicken Greens Bowl");
        values.put("section", "Lunch");
        values.put("ingredients","1 tbsp authentic tamari sauce\n" +
                "1 tsp curry blend, medium heat\n" +
                        "¼ tsp roasted ground cumin\n" +
                        "1 aromatic garlic clove, finely grated\n" +
                        "1 tsp pure honey\n" +
                        "2 skinless chicken breast fillets (turkey as an alternative)\n" +
                        "1 tbsp crunchy peanut spread (preferably sugar-free and palm oil-free)\n" +
                        "1 tbsp tangy sweet chilli glaze\n" +
                        "1 tbsp fresh lime extract\n" +
                        "A touch of sunflower oil for pan greasing\n" +
                        "2 crisp Little Gem lettuce hearts, sectioned into wedges\n" +
                        "¼ refreshing cucumber, halved and sliced\n" +
                        "1 delicate banana shallot, thinly sliced\n" +
                        "Freshly chopped coriander\n" +
                        "Seeds from half a vibrant pomegranate");
                values.put("process", "STEP 1: Combine tamari, curry powder, cumin, garlic, and honey in a dish. Slice chicken into 4 fillets, coat with marinade, and refrigerate for at least 1 hr or overnight.\n" +
                        "STEP 2: Blend peanut butter, chilli sauce, lime juice, and 1 tbsp water for the sauce. For cooking, lightly oil a non-stick pan. Cook chicken on medium for 5-6 mins, flipping for the last minute. Let it rest covered.\n" +
                        "STEP 3: Mix lettuce, cucumber, shallot, coriander, and pomegranate on plates. Drizzle some sauce. Top with sliced chicken and remaining sauce. Serve warm.");

        fullPath = context.getFilesDir().getAbsolutePath() + File.separator + "imgs" + File.separator + "4.jpg";
        imgFile = new File(fullPath);
        if (imgFile.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(imgFile);
                byte[] imageByteArray = new byte[(int) imgFile.length()];
                fis.read(imageByteArray);
                fis.close();
                values.put("photo", imageByteArray);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        db.insert("Recipes", null, values);


        //и тд
    }

    // Обновление таблицы
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Recipes");
//        db.execSQL("DROP TABLE IF EXISTS Favorite");
//        db.execSQL("DROP TABLE IF EXISTS CookBook");
        onCreate(db);
    }
}
