import dao.DataBaseDao;
import model.Product;

public class Main {
    public static String packages = "";

    public static void main(String[] args) {
        if (args.length == 1) {
            packages = args[0];
        }
        DataBaseDao<Integer, Product> db = new DataBaseDao<>();
        db.put(0, new Product("book", 100));
        db.put(1, new Product("cola", 50));
        db.put(2, new Product("banana", 20));
        Product p = db.get(2);
        System.out.printf("Old product : %s with old price %s\n", p.getName(), p.getPrice());
        db.remove(2);
        db.put(2, new Product("orange", 10));
        p = db.get(2);
        System.out.printf("New product : %s with new price %s\n", p.getName(), p.getPrice());
        System.out.println(LogAspect.getStatistics());
    }

}