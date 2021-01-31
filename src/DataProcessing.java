import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * На основании данных, содержащихся в файлах, начинающихся с цифры:
 * 1 - скробблинг исполнителей
 * 2 - скробблинг композиций
 * расшифровка содержания файлов:
 *
 * *result.txt хранит текущие результаты, скопированные (как есть) с сайта last.fm, за констистентность и корректность
 * данных отвечает конечный пользователь
 *
 * *lastYearResults.txt хранит прошлогодние результаты скробблинга для вычисления измнений за год
 *
 * *db.txt хранит перечень всех исполнителей/песен за всю историю скробблинга, используется с целью определения
 * новинки или возвращения в чарт
 *
 * программа выводит на экран результаты скробблинга песен на сайте last.fm в обратном порядке с указанием номера места,
 * количества воспроизведений, изменения позиции и количества воспроизведений за год.
 * @author  Сергей Шершавин
 * */

public class DataProcessing {
    public static void main(String[] args) throws Exception {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String num = r.readLine();
        r.close();
        BufferedReader reader = new BufferedReader(new FileReader("d:\\" + num + "result.txt"));
        ArrayList<CurrentArtist> list = new ArrayList<>(); //создание списка для хранение моделей, созданных на основании данных из файла
        ArrayList<String> db = new ArrayList<>(); // представление "базы данных" - архива композиций/артистов
        Map<String, CurrentArtist> lastYear = new HashMap<>(); //карта поиска результатов прошлогоднего чарта по наименованию
        String place;
        if (num.equals("1")) {
            while (reader.ready()){
                place = reader.readLine();
                reader.readLine();
                list.add(new CurrentArtist(Integer.parseInt(place), reader.readLine(), Integer.parseInt(reader.readLine())));
            }
        }
        else {
            while (reader.ready()){
                place = reader.readLine();
                reader.readLine(); reader.readLine(); reader.readLine();
                String name = reader.readLine();
                name = reader.readLine() + " — " + name;
                list.add(new CurrentArtist(Integer.parseInt(place), name, Integer.parseInt(reader.readLine())));
            }
        }
        reader = new BufferedReader(new FileReader("d:\\" + num + "db.txt"));
        while (reader.ready()){
            place = reader.readLine();
            db.add(place);
        }
        reader.close();

        reader = new BufferedReader(new FileReader("d:\\" + num + "lastYearResults.txt"));
        while (reader.ready()){
            place = reader.readLine();
            int indexOfPoint = place.indexOf(".");
            int length = place.length();
            int indexOfParenthese = place.lastIndexOf("(", length - 7);
            String name = place.substring(indexOfPoint + 2, indexOfParenthese - 1);
            String currentPlace = place.substring(0, indexOfPoint);
            String numberOfPlayings = place.substring(indexOfParenthese + 1, place.lastIndexOf(")", length - 5));
            lastYear.put(name, new CurrentArtist(Integer.parseInt(currentPlace), name, Integer.parseInt(numberOfPlayings)));
        }
        reader.close();
        /* TODO: одинаковому числу прослушиваний должно соответствовать одинаковое место в чарте */
        for (CurrentArtist artist : list){
            if (lastYear.containsKey(artist.name)){
                CurrentArtist current = lastYear.get(artist.name);
                artist.diffPlace = current.place - artist.place; //изменение позиции в чарте за год
                artist.diffNumberPlays = artist.numberPlays - current.numberPlays; //измненение количества прослушиваний за год
                artist.mark = "";
            }
            else {
                if (db.contains(artist.name)) artist.mark = "RETURN!"; //отметка, что композиция когда-то была в чарте и вернулась в него
                else artist.mark = "NEW!"; //отметка о новинке чарта
            }
        }
        for(int i = list.size()-1; i>=0; i--){
            CurrentArtist current = list.get(i);
            if (current.mark.equals("")){
                if ((current.diffPlace >= 0) && (current.diffNumberPlays >= 0)) System.out.println(current.place + ". " + current.name + " (" + current.numberPlays + ") +" + current.diffPlace + " (+" + current.diffNumberPlays + ")");
                else if (current.diffPlace >= 0) System.out.println(current.place + ". " + current.name + " (" + current.numberPlays + ") +" + current.diffPlace + " (" + current.diffNumberPlays + ")");
                else if (current.diffNumberPlays >= 0) System.out.println(current.place + ". " + current.name + " (" + current.numberPlays + ") " + current.diffPlace + " (+" + current.diffNumberPlays + ")");
                else System.out.println(current.place + ". " + current.name + " (" + current.numberPlays + ") " + current.diffPlace + " (" + current.diffNumberPlays + ")");
            }
            else System.out.println(current.place + ". " + current.name + " (" + current.numberPlays + ") " + current.mark);
        }

    }
    /**
     * Класс-модель артиста/композиции, полученный из результатов скробблинга*/
    public static class CurrentArtist {
        private final int place;
        private final String name;
        private final int numberPlays;
        public int diffPlace;
        public int diffNumberPlays;
        public String mark;
    /**Конструктор, содержит параметры:
     * @param place - занимаемое место
     * @param name - имя исполнителя/наименование композиции
     * @param numberPlays - количество прослушиваний*/
        public CurrentArtist(int place, String name, int numberPlays) {
            this.place = place;
            this.name = name;
            this.numberPlays = numberPlays;
        }
    }
}
