import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Преобразует входные данные формата файлов *lastYearResults.txt в данные пригодные для добавления в
 * файлы типа *db.txt
 * @author  Сергей Шершавин
 * @*/

/*TODO: 1. Форматировать и записывать только строки с отметкой NEW
*       2. Осуществлять запись непосредственно в файлы *db.txt*/
public class SourceFileUpdate {
    public static void main(String[] args) {
        System.out.println("Введите полное имя исходного файла: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String songsTitle = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(br.readLine()), StandardCharsets.UTF_8))) {
            br.close();
            while(reader.ready()) {
                String temp = reader.readLine();
                int indexOfPoint = temp.indexOf(".");
                int length = temp.length();
                int indexOfParenthese = temp.lastIndexOf("(", length - 7);
                songsTitle += temp.substring(indexOfPoint + 2, indexOfParenthese - 1) +'\n';
            }
        }
        catch(IOException ex) {System.out.println("Can't read it!");}
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("d:\\time.txt"), StandardCharsets.UTF_8))) {
            writer.write(songsTitle, 0, songsTitle.length());
        }
        catch(IOException ex) {System.out.println("Can't write it!");}
    }
}
