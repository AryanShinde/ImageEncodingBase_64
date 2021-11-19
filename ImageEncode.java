import javax.sound.sampled.SourceDataLine;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.stream.StreamSupport;

class ImageEncode {
    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(null);
        File file = fileChooser.getSelectedFile();
        try {

            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            System.out.println("Byte array data of image: ");
            System.out.println(Arrays.toString(data));
            String[] bitData = new String[data.length];
            for (int i = 0; i < data.length; i++) {
                bitData[i] = (String.format("%8s", Integer.toBinaryString(data[i] & 0xFF)).replace(' ', '0'));
                // System.out.println(bitData[i]);
            }
            System.out.println();
            System.out.println("Length of byte array data: ");
            System.out.println(data.length);
            String s = Base64.getEncoder().encodeToString(data);
            System.out.println();
            System.out.println("Inbuilt encrpted string");
            System.out.println(s);

            int len = data.length;
            int size = (len * 8) / 6;
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
            System.out.println("Map values: the base_64 table: ");
            System.out.println(map);
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
            // System.out.println(bits);
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
            // System.out.println(values);

            fis.close();

        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}