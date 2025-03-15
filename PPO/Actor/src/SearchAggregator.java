import akka.actor.*;
import akka.japi.pf.DeciderBuilder;
import scala.Option;
import scala.concurrent.duration.FiniteDuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SearchAggregator {

    private static HashMap<ServerType, ArrayList<String>> map = new HashMap<>();
    private static ActorSystem system = ActorSystem.create("MySystem");
    private static AtomicInteger countActiveChildren = new AtomicInteger(0);
    private static final String[] services = {"Google", "Yandex", "Bing"};
    private static Server server;
    private static final boolean isLogEnabled = false;
    public static final int TIMEOUT = 3000;

    private static class RestartException extends RuntimeException {
        public RestartException() {
            super();
        }
    }

    private static class StopException extends RuntimeException {
        public StopException() {
            super();
        }
    }

    private static class EscalateException extends RuntimeException {
        public EscalateException() {
            super();
        }
    }

    private static class Query {
        String query;

        Query(String query) {
            this.query = query;
        }
    }

    private static class Answer {
        ArrayList<String> answers;

        Answer(ArrayList<String> answers) {
            this.answers = answers;
        }
    }

    private static class ChildActor extends UntypedActor {

        @Override
        public void postStop() {
            if (isLogEnabled) System.out.println(self().path() + " was stopped");
        }

        @Override
        public void postRestart(Throwable cause) {
            if (isLogEnabled) System.out.println(self().path() + " was restarted after: " + cause);
        }

        @Override
        public void preRestart(Throwable cause, Option<Object> message) {
            if (isLogEnabled) System.out.println(self().path() + " is dying because of " + cause);
        }

        @Override
        public void onReceive(Object message) {
            if (message instanceof Query) {
                ServerType serverType = server.getServerTypeFromString(getSelf().path().toString());
                ArrayList<String> answers = server.getRequest(((Query) message).query, serverType);
                if (isLogEnabled) System.out.println("Query : " + ((Query) message).query);
                getSender().tell(new Answer(answers), getSelf());
            }
        }

    }

    private static class Supervisor extends UntypedActor {
        @Override
        public SupervisorStrategy supervisorStrategy() {
            return new OneForOneStrategy(false, DeciderBuilder
                    .match(RestartException.class, e -> OneForOneStrategy.restart())
                    .match(StopException.class, e -> OneForOneStrategy.stop())
                    .match(EscalateException.class, e -> OneForOneStrategy.escalate())
                    .build());
        }

        @Override
        public void onReceive(Object message) {
            if (message instanceof Query) {
                ArrayList<ActorRef> children = new ArrayList<>();
                for (String site : services) {
                    children.add(getContext().actorOf(Props.create(ChildActor.class), site));
                }
                for (ActorRef child : children) {
                    child.tell(message, getSelf());
                }
            }

            if (message instanceof Answer) {
                if (isLogEnabled) System.out.println("Answer : " + ((Answer) message).answers);
                map.put(server.getServerTypeFromString(getSender().path().toString()), ((Answer) message).answers);
                countActiveChildren.decrementAndGet();
                system.stop(getSender());
            }
        }

    }

    public HashMap<ServerType, ArrayList<String>> getRequest(String request, Server server) {
        initMap();
        if (request == null || request.isBlank() || server == null) return map;
        system = ActorSystem.create("MySystem");
        SearchAggregator.server = server;
        countActiveChildren = new AtomicInteger(services.length);
        ActorRef parent = system.actorOf(Props.create(Supervisor.class), "parent");
        parent.tell(new Query(request), parent);
        system.scheduler().scheduleOnce(FiniteDuration.create(TIMEOUT, TimeUnit.MILLISECONDS),
                () -> {
                    countActiveChildren = new AtomicInteger(0);
                    system.shutdown();
                    system.awaitTermination();
                }, system.dispatcher());
        while (countActiveChildren.get() > 0) {
            if (isLogEnabled) System.out.println("waiting...");
        }
        return map;
    }

    private void initMap() {
        map = new HashMap<>();
        map.put(ServerType.GOOGLE, new ArrayList<>());
        map.put(ServerType.YANDEX, new ArrayList<>());
        map.put(ServerType.BING, new ArrayList<>());
    }

}
