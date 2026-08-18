package com.jiwan.rabbitmorph;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class RabbitConfig {
    public static final String TYPE_NORMAL = "Normal";
    public static final String TYPE_BROWN = "Brown";
    public static final String TYPE_BLACK = "Black";
    public static final String TYPE_WHITE = "White";
    public static final String TYPE_GOLDEN = "Golden";

    public static final double DEFAULT_HEALTH = 20.0D;
    public static final double DEFAULT_SPEED = 0.13D;
    public static final double DEFAULT_JUMP = 0.75D;
    public static final double DEFAULT_FALL = 1.0D;

    // Presets
    public static final int NORMAL_BODY_R = 180, NORMAL_BODY_G = 130, NORMAL_BODY_B = 80;
    public static final int NORMAL_EAR_R = 255, NORMAL_EAR_G = 170, NORMAL_EAR_B = 170;
    public static final int NORMAL_EYE_R = 30, NORMAL_EYE_G = 20, NORMAL_EYE_B = 20;
    public static final int NORMAL_TAIL_R = 255, NORMAL_TAIL_G = 255, NORMAL_TAIL_B = 255;

    public static final int BROWN_BODY_R = 145, BROWN_BODY_G = 92, BROWN_BODY_B = 52;
    public static final int BROWN_EAR_R = 220, BROWN_EAR_G = 135, BROWN_EAR_B = 125;
    public static final int BROWN_EYE_R = 25, BROWN_EYE_G = 18, BROWN_EYE_B = 15;
    public static final int BROWN_TAIL_R = 230, BROWN_TAIL_G = 210, BROWN_TAIL_B = 195;

    public static final int BLACK_BODY_R = 35, BLACK_BODY_G = 35, BLACK_BODY_B = 40;
    public static final int BLACK_EAR_R = 90, BLACK_EAR_G = 70, BLACK_EAR_B = 75;
    public static final int BLACK_EYE_R = 255, BLACK_EYE_G = 80, BLACK_EYE_B = 80;
    public static final int BLACK_TAIL_R = 70, BLACK_TAIL_G = 70, BLACK_TAIL_B = 75;

    public static final int WHITE_BODY_R = 235, WHITE_BODY_G = 235, WHITE_BODY_B = 235;
    public static final int WHITE_EAR_R = 255, WHITE_EAR_G = 170, WHITE_EAR_B = 175;
    public static final int WHITE_EYE_R = 30, WHITE_EYE_G = 30, WHITE_EYE_B = 30;
    public static final int WHITE_TAIL_R = 245, WHITE_TAIL_G = 245, WHITE_TAIL_B = 245;

    public static final int GOLDEN_BODY_R = 235, GOLDEN_BODY_G = 185, GOLDEN_BODY_B = 45;
    public static final int GOLDEN_EAR_R = 255, GOLDEN_EAR_G = 205, GOLDEN_EAR_B = 100;
    public static final int GOLDEN_EYE_R = 70, GOLDEN_EYE_G = 40, GOLDEN_EYE_B = 10;
    public static final int GOLDEN_TAIL_R = 255, GOLDEN_TAIL_G = 225, GOLDEN_TAIL_B = 120;

    public static class RabbitConfigData {
        public String type = TYPE_NORMAL;
        public double health = DEFAULT_HEALTH;
        public double speed = DEFAULT_SPEED;
        public double jump = DEFAULT_JUMP;
        public double fall = DEFAULT_FALL;

        public float scaleOverall = 1.0F;
        public float scaleHead = 1.0F;
        public float scaleEar = 1.0F;
        public float scaleBody = 1.0F;
        public float scaleLegs = 1.0F;
        public float scaleTail = 1.0F;

        public int bodyR = NORMAL_BODY_R, bodyG = NORMAL_BODY_G, bodyB = NORMAL_BODY_B, bodyA = 255;
        public int earR = NORMAL_EAR_R, earG = NORMAL_EAR_G, earB = NORMAL_EAR_B, earA = 255;
        public int eyeR = NORMAL_EYE_R, eyeG = NORMAL_EYE_G, eyeB = NORMAL_EYE_B, eyeA = 255;
        public int tailR = NORMAL_TAIL_R, tailG = NORMAL_TAIL_G, tailB = NORMAL_TAIL_B, tailA = 255;
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static File getConfigDir() {
        File dir = new File("config" + File.separator + ".rabbitmorph");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static String saveJsonConfig(RabbitConfigData data) {
        try {
            File dir = getConfigDir();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String filename = "rabbitmorph_" + sdf.format(new Date()) + ".json";
            File configFile = new File(dir, filename);

            FileWriter writer = new FileWriter(configFile);
            GSON.toJson(data, writer);
            writer.close();
            return configFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static RabbitConfigData loadLatestJsonConfig() {
        try {
            File dir = getConfigDir();
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                Arrays.sort(files, new Comparator<File>() {
                    @Override
                    public int compare(File f1, File f2) {
                        return Long.compare(f2.lastModified(), f1.lastModified()); // Latest first
                    }
                });
                for (File f : files) {
                    if (f.getName().endsWith(".json")) {
                        FileReader reader = new FileReader(f);
                        RabbitConfigData data = GSON.fromJson(reader, RabbitConfigData.class);
                        reader.close();
                        if (data != null) return data;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RabbitConfigData();
    }
}
