import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeMap;

public class B {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        TreeMap<Integer,Boolean> map=new TreeMap<Integer,Boolean>();
        while (reader.ready()) {
            String[] split = reader.readLine().split("[\\s]");
            switch (split[0]) {
                case ("insert"):
                    if (!map.containsKey(Integer.parseInt(split[1]))){
                        map.put(Integer.parseInt(split[1]),true);
                    }
                    break;
                case ("exists"):
                    if (map.containsKey(Integer.parseInt(split[1]))) {
                        System.out.println("true");
                    } else {
                        System.out.println("false");
                    }
                    break;
                case ("delete"):
                    map.remove(Integer.parseInt(split[1]));
                    break;
                case ("prev"):
                    if (((TreeMap<Integer, Boolean>) map).floorEntry(Integer.parseInt(split[1])-1)==null){
                        System.out.println("none");
                    }else{
                        System.out.println(((TreeMap<Integer, Boolean>) map).floorEntry(Integer.parseInt(split[1])-1).getKey());
                    }
                    break;
                case ("next"):
                    if (((TreeMap<Integer, Boolean>) map).higherEntry(Integer.parseInt(split[1]))==null){
                        System.out.println("none");
                    }else{
                        System.out.println(((TreeMap<Integer, Boolean>) map).higherEntry(Integer.parseInt(split[1])).getKey());
                    }
                    break;
            }
        }
    }
}