package test;

import clock.StableClock;
import eventstats.EventsStatistic;
import eventstats.EventsStatisticImpl;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import static junit.framework.TestCase.*;

public class StatTest {

    EventsStatistic eventsStatistic;
    StableClock clock;
    private final String METHOD1 = "Method1";
    private final String METHOD2 = "Method2";
    private final String METHOD3 = "Method3";

    @Before
    public void createMyLinkedHashMap() {
        clock = new StableClock(Instant.now());
        eventsStatistic = new EventsStatisticImpl(clock);
    }

    @Test
    public void logOneEvent() {
        eventsStatistic.incEvent(METHOD1);
        checkCount(1, METHOD1);
    }

    @Test
    public void logTwoEvents() {
        eventsStatistic.incEvent(METHOD1);
        eventsStatistic.incEvent(METHOD2);
        checkCount(1, METHOD1);
        checkCount(1, METHOD2);
    }

    @Test
    public void logOneEventTwoTimes() {
        eventsStatistic.incEvent(METHOD1);
        eventsStatistic.incEvent(METHOD1);
        checkCount(2, METHOD1);
    }

    @Test
    public void logOneEventAfterHour() {
        eventsStatistic.incEvent(METHOD1);
        checkCount(1, METHOD1);
        clock.setInstant(clock.now().plus(1, ChronoUnit.HOURS));
        eventsStatistic.incEvent(METHOD1);
        checkCount(1, METHOD1);
    }

    @Test
    public void logSomeEventsAfterHour() {
        incEventSomeTimes(1, METHOD1);
        incEventSomeTimes(2, METHOD2);
        incEventSomeTimes(3, METHOD3);
        checkCount(1, METHOD1);
        checkCount(2, METHOD2);
        checkCount(3, METHOD3);
        clock.setInstant(clock.now().plus(2, ChronoUnit.HOURS));
        incEventSomeTimes(2, METHOD1);
        incEventSomeTimes(3, METHOD2);
        incEventSomeTimes(1, METHOD3);
        checkCount(2, METHOD1);
        checkCount(3, METHOD2);
        checkCount(1, METHOD3);
    }

    @Test
    public void stressTest() {
        Random rand = new Random();
        int countCallsMethod1 = 0;
        int countCallsMethod2 = 0;
        int countCallsMethod3 = 0;
        for (int i = 0; i < 1000; i++) {
            int t = rand.nextInt(4);
            int countTimes = rand.nextInt(10) + 1;
            switch (t) {
                case 0 -> {
                    incEventSomeTimes(countTimes, METHOD1);
                    countCallsMethod1 += countTimes;
                }
                case 1 -> {
                    incEventSomeTimes(countTimes, METHOD2);
                    countCallsMethod2 += countTimes;
                }
                case 2 -> {
                    incEventSomeTimes(countTimes, METHOD3);
                    countCallsMethod3 += countTimes;
                }
                case 3 -> {
                    clock.setInstant(clock.now().plus(countTimes, ChronoUnit.HOURS));
                    countCallsMethod1 = 0;
                    countCallsMethod2 = 0;
                    countCallsMethod3 = 0;
                }
            }
            checkCount(countCallsMethod1, METHOD1);
            checkCount(countCallsMethod2, METHOD2);
            checkCount(countCallsMethod3, METHOD3);
        }
    }

    private void checkCount(int expectedCount, String methodName) {
        assertEquals(String.format("%.4f", expectedCount / 60f), String.format("%.4f", eventsStatistic.getEventStatisticByName(methodName)));
        if (eventsStatistic.getEventStatisticByName(methodName) != 0) {
            assertEquals(String.format("%.4f", expectedCount / 60f), String.format("%.4f", eventsStatistic.getAllEventStatistic().get(methodName)));
        }
    }

    private void incEventSomeTimes(int times, String name) {
        for (int i = 0; i < times; i++) {
            eventsStatistic.incEvent(name);
        }
    }

}
