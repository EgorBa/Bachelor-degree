import clock.NormalClock;
import eventstats.EventsStatisticImpl;

public class Main {

    public static void main(String[] args){
        EventsStatisticImpl events = new EventsStatisticImpl(new NormalClock());
        String name1 = "Method1";
        String name2 = "Method2";
        events.incEvent(name1);
        events.incEvent(name1);
        events.incEvent(name2);
        System.out.println(events.getEventStatisticByName(name1));
        System.out.println(events.getAllEventStatistic());
        events.printStatistic();
    }
}
