package com.project.app.util;

import com.project.app.execptions.MyRuntimeException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

public class CaptchaGenerator {

    private static final int CAPTCHA_WIDTH = 100; //
    private static final int CAPTCHA_HEIGHT = 32; //
    private static final int CODE_LENGTH = 4; //
    //    private static final String CODE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String CODE_CHARS = "1234567890"; //

    private static final Random random = new Random();
    private static Map<String, String> codeMap = new HashMap<>();

    public static Map<String, Object> generate() throws IOException {
        BufferedImage image = new BufferedImage(CAPTCHA_WIDTH, CAPTCHA_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();


        g.setColor(Color.WHITE);
        g.fillRect(0, 0, CAPTCHA_WIDTH, CAPTCHA_HEIGHT);


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(CODE_CHARS.charAt(random.nextInt(CODE_CHARS.length())));
        }
        String code = sb.toString();


        g.setColor(Color.BLACK);
        for (int i = 0; i < CODE_LENGTH; i++) {
            char c = code.charAt(i);
            g.drawString(Character.toString(c), 10 + i * 25, 25);
        }


        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(CAPTCHA_WIDTH);
            int y = random.nextInt(CAPTCHA_HEIGHT);
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g.fillRect(x, y, 1, 1);
        }

        for (int i = 0; i < 5; i++) {
            int x1 = random.nextInt(CAPTCHA_WIDTH);
            int y1 = random.nextInt(CAPTCHA_HEIGHT);
            int x2 = random.nextInt(CAPTCHA_WIDTH);
            int y2 = random.nextInt(CAPTCHA_HEIGHT);
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g.drawLine(x1, y1, x2, y2);
        }
        g.dispose();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "JPEG", byteArrayOutputStream);
        String codeImg = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        Map<String, Object> dto = new HashMap<>();
        String uuid = UUID.randomUUID().toString();
        dto.put("uuid", uuid);
        dto.put("code", "data:image/jpg;base64," + codeImg);
        codeMap.put(uuid, code);
        return dto;
    }

    public static boolean verification(String uuid, String userCode) {
        if (codeMap.containsKey(uuid)) {
            if (codeMap.get(uuid).equalsIgnoreCase(userCode)) {
                codeMap.remove(uuid);
                return true;
            }
        }
        throw new MyRuntimeException("The verification code is incorrect");
    }


}

