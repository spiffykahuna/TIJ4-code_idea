package concurrency.exercises;

import java.util.*;
import java.util.concurrent.*;

/**
 * Exercise 35: (8) Modify WebServerSimulation.java so that it represents Web clients making requests of a fixed number of servers.
 * The goal is to determine the load that the group of servers can handle.
 */
public class Exercise35 {
    // Read-only objects don't require synchronization:
    static class WebClient {
        private final int serviceTime;
        public WebClient(int tm) { serviceTime = tm; }
        public int getServiceTime() { return serviceTime; }
        public String toString() {
            return "[" + serviceTime + "]";
        }
    }

    static class WebQueue extends ArrayBlockingQueue<WebClient> {
        public WebQueue(int capacity) {
            super(capacity);
        }

        public String toString() {
            if(this.size() == 0)
                return "[Empty]";
            StringBuilder result = new StringBuilder();
            for(WebClient webClient : this)
                result.append(webClient.toString());
            return result.toString();
        }
    }

    // Randomly add webQueue to a queue:
    static class WebClientGenerator implements Runnable {
        private final int maxClientQueueSize;
        private final int maxServiceTimeMs;

        private WebQueue webClients;
        private static Random rand = new Random(47);
        public WebClientGenerator(WebQueue cq, int maxClientQueueSize, int maxServiceTimeMs) {
            webClients = cq;
            this.maxClientQueueSize = maxClientQueueSize;
            this.maxServiceTimeMs = maxServiceTimeMs;
        }

        @Override
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    TimeUnit.MILLISECONDS.sleep(rand.nextInt(2000));

                    while(webClients.size() < maxClientQueueSize) {
                        webClients.offer(new WebClient(rand.nextInt(maxServiceTimeMs)));
                    }
                }
            } catch(InterruptedException e) {
                System.out.println("WebClientGenerator interrupted");
            }
            System.out.println("WebClientGenerator terminating");
        }
    }

    static class WebServer implements Runnable, Comparable<WebServer> {
        private static int counter = 0;
        private final int id = counter++;
        // Customers served during this shift:
        private long webClientsServed = 0;
        private WebQueue webQueue;
        private boolean servingCustomerLine = true;
        public WebServer(WebQueue cq) { webQueue = cq; }
        @Override
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    WebClient webClient = webQueue.take();
                    TimeUnit.MILLISECONDS.sleep(webClient.getServiceTime());
                    synchronized(this) {
                        webClientsServed++;
                    }
                }
            } catch(InterruptedException e) {
                System.out.println(this + "interrupted");
            }
            System.out.println(this + "terminating");
        }

        public String toString() { return "WebServer " + id + " "; }

        public synchronized long getWebClientsServed() {
            return webClientsServed;
        }
        // Used by priority queue:
        @Override
        public synchronized int compareTo(WebServer other) {
            return webClientsServed < other.webClientsServed ? -1 :
                    (webClientsServed == other.webClientsServed ? 0 : 1);
        }
    }

    static class ServerLoadRecorder implements Runnable {
        private ExecutorService exec;
        private WebQueue webClients;

        private PriorityQueue<WebServer> workingWebServers = new PriorityQueue<WebServer>();

        private long secondCounter;
        private long maxServiceRate;

        private List<Long> serviceRateStats = new ArrayList<Long>();

        public ServerLoadRecorder(ExecutorService e,
                                  WebQueue webClients, int serverCount) {
            exec = e;
            this.webClients = webClients;

            for (int i = 0; i < serverCount; i++) {
                WebServer webServer = new WebServer(webClients);
                workingWebServers.add(webServer);
                exec.execute(webServer);
            }
        }

        @Override
        public void run() {
            try {
                secondCounter = 1;
                while(!Thread.interrupted()) {
                    TimeUnit.SECONDS.sleep(1);

                    long totalClientsServed = 0;
                    for(WebServer webServer : workingWebServers)
                        totalClientsServed += webServer.getWebClientsServed();

                    long currentServiceRate = totalClientsServed/secondCounter;
                    serviceRateStats.add(currentServiceRate);

                    if(currentServiceRate > maxServiceRate)
                        maxServiceRate = currentServiceRate;

                    StringBuilder sb = new StringBuilder()
                        .append("Total servers: ").append(workingWebServers.size()).append("\t")
                        .append("Load per second: ").append(currentServiceRate).append("\t")
                        .append("Avg rate: ").append(getAverageLoad()).append("\t")
                        .append("Max rate: ").append(maxServiceRate).append("\t")
                        .append("Queue size: ").append(webClients.size()).append("\t")
                        .append("Total clients served: ").append(totalClientsServed)
                    ;
                    System.out.println(sb.toString());

                    secondCounter++;
                }
            } catch(InterruptedException e) {
                System.out.println(this + "interrupted");
            }
            System.out.println(this + "terminating");
        }

        private long getAverageLoad() {
            long sum = 0;
            for(Long totalHitsPerSecond: serviceRateStats) {
                sum += totalHitsPerSecond;
            }
            return sum/serviceRateStats.size();
        }

        public String toString() { return "ServerLoadRecorder "; }
    }

    static class WebServerSimulation {

        static final int SERVER_COUNT = 10;
        static final int MAX_CLIENT_QUEUE_SIZE = 10000;
        static final int MAX_SERVICE_TIME_MS = 50;

        public static void main(String[] args) throws Exception {
            ExecutorService exec = Executors.newCachedThreadPool();

            WebQueue webQueue = new WebQueue(MAX_CLIENT_QUEUE_SIZE);

            exec.execute(new WebClientGenerator(webQueue, MAX_CLIENT_QUEUE_SIZE, MAX_SERVICE_TIME_MS));
            exec.execute(new ServerLoadRecorder(exec, webQueue, SERVER_COUNT));

            if(args.length > 0) // Optional argument
                TimeUnit.SECONDS.sleep(new Integer(args[0]));
            else {
                System.out.println("Press 'Enter' to quit");
                System.in.read();
            }
            exec.shutdownNow();
        }
    }

    public static void main(String[] args) throws Exception {
        WebServerSimulation.main(args);
    }
}
