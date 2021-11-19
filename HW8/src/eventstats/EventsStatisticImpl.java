package eventstats;

import clock.Clock;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static resourses.Config.MINUTES_PER_HOUR;
import static resourses.Config.STATISTICS_TEXT;

public class EventsStatisticImpl implements EventsStatistic {

    HashMap<String, LinkedList<Instant>> map = new HashMap<>();
    Clock clock;

    public EventsStatisticImpl(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void incEvent(String name) {
        map.putIfAbsent(name, new LinkedList<>());
        map.get(name).add(clock.now());
    }

    @Override
    public double getEventStatisticByName(String name) {
        return getEventStatisticByName(name, clock.now());
    }

    @Override
    public Map<String, Double> getAllEventStatistic() {
        HashMap<String, Double> stats = new HashMap<>();
        Instant now = clock.now();
        for (String name : map.keySet()) {
            stats.put(name, getEventStatisticByName(name, now));
        }
        return stats;
    }

    @Override
    public void printStatistic() {
        for (String name : map.keySet()) {
            System.out.printf(STATISTICS_TEXT, name, getEventStatisticByName(name));
        }
    }

    private double getEventStatisticByName(String name, Instant now) {
        double countCallsHourBefore = 0;
        double countCallsHourAgo = 0;
        if (!map.containsKey(name)) return 0;
        for (Instant i : map.get(name)) {
            Duration timeElapsed = Duration.between(i, now);
            if (timeElapsed.toMinutes() < MINUTES_PER_HOUR) {
                countCallsHourBefore++;
            } else {
                countCallsHourAgo++;
            }
        }
        for (int i = 0; i < countCallsHourAgo; i++) {
            map.get(name).removeFirst();
        }
        return countCallsHourBefore / MINUTES_PER_HOUR;
    }
}
