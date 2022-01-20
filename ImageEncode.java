import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

class ImageEncode {
    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(null);
        File file = fileChooser.getSelectedFile();
        try {

            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            String[] bitData = new String[data.length];
            for (int i = 0; i < data.length; i++) {
                bitData[i] = (String.format("%8s", Integer.toBinaryString(data[i] & 0xFF)).replace(' ', '0'));
            }
            String s = Base64.getEncoder().encodeToString(data);
            System.out.println("Inbuilt encrpted string");
            System.out.println(s);

            int len = data.length;
            boolean flag2 = false;
            boolean flag4 = false;

            ArrayList<String> bits = new ArrayList<>();
            StringBuilder str = new StringBuilder();
            HashMap<Integer, Character> map = new HashMap<>();
            for (int i = 0; i < 26; i++) {
                map.put(i, (char) ('A' + i));
            }
            for (int i = 26, count = 0; i < 52; i++, count++) {
                map.put(i, (char) ('a' + count));
            }
            int count = 0;
            for (int i = 52; i < 62; i++) {
                map.put(i, (char) (count + '0'));
                count += 1;
            }
            map.put(62, '+');
            map.put(63, '/');
            System.out.println();

            int j;
            for (int i = 0; i < len; i++) {
                int sizeStr = str.length();
                for (j = 0; j < (6 - sizeStr); j++) {
                    str.append(bitData[i].charAt(j));
                }
                if (str.length() == 6) {
                    String temp = str.toString();
                    bits.add(temp);
                    str = new StringBuilder();

                }
                while (j < bitData[i].length()) {
                    str.append(bitData[i].charAt(j));
                    j += 1;
                }
                if (str.length() == 6) {
                    bits.add(str.toString());
                    str = new StringBuilder();
                }
                if (str.length() == 2 && i == bitData.length - 1) {
                    str.append('0');
                    str.append('0');
                    str.append('0');
                    str.append('0');
                    String temp = str.toString();
                    bits.add(temp);
                    flag2 = true;
                } else if (str.length() == 4 && i == bitData.length - 1) {
                    str.append('0');
                    str.append('0');
                    String temp = str.toString();
                    bits.add(temp);
                    flag4 = true;
                }
            }
            ArrayList<Integer> values = new ArrayList<>();
            for (int i = 0; i < bits.size(); i++) {
                int value = Integer.parseInt(bits.get(i), 2);
                values.add(value);
            }

            StringBuilder encodedString = new StringBuilder();
            for (int i = 0; i < values.size(); i++) {
                encodedString.append(map.get(values.get(i)));
            }
            if (flag2) {
                encodedString.append('=');
                encodedString.append('=');

            }
            if (flag4) {
                encodedString.append('=');
            }
            System.out.println();
            System.out.println("My enrypted string");
            System.out.println(encodedString.toString()); // my encoded string
            decodeString(encodedString.toString(), map);
            fis.close();

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public static void decodeString(String encodedString, HashMap<Integer, Character> map) throws IOException {
        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 0; i < encodedString.length(); i++) {
            for (int num : map.keySet()) {
                if (encodedString.charAt(i) == '=') {
                    break;
                }
                if (map.get(num) == encodedString.charAt(i)) {
                    values.add(num);
                }
            }
        }
        ArrayList<String> bitData = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            String s = String.format("%6s", Integer.toBinaryString(values.get(i) & 0xFF)).replace(' ', '0');
            bitData.add(s);
        }

        StringBuilder str = new StringBuilder();
        ArrayList<String> EightBit = new ArrayList<>();
        for (int i = 0; i < bitData.size(); i++) {
            for (int j = 0; j < bitData.get(i).length(); j++) {
                str.append(bitData.get(i).charAt(j));
                if (str.length() == 8) {
                    String s = str.toString();
                    EightBit.add(s);
                    str = new StringBuilder();
                }
            }
        }

        ArrayList<Byte> ans = new ArrayList<>();
        for (int i = 0; i < EightBit.size(); i++) {
            byte b1 = (byte) Integer.parseInt(EightBit.get(i), 2);
            ans.add(b1);
        }
        byte[] finalAns = new byte[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            finalAns[i] = ans.get(i);
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(finalAns);
        BufferedImage image = ImageIO.read(bais);
        ImageIO.write(image, "png", new File("final_image.png"));
    }

}