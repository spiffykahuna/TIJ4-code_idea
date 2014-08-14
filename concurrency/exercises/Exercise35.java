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

    // Teach the customer line to display itself:
    static class WebQueue extends LinkedBlockingQueue<WebClient> {
        public String toString() {
            if(this.size() == 0)
                return "[Empty]";
            StringBuilder result = new StringBuilder();
            for(WebClient webClient : this)
                result.append(webClient);
            return result.toString();
        }
    }

    // Randomly add webQueue to a queue:
    static class WebClientGenerator implements Runnable {
        private final int maxHitRatePerSecond;
        private final int maxServiceTimeMs;

        private WebQueue webClients;
        private static Random rand = new Random(47);
        public WebClientGenerator(WebQueue cq, int maxHitRatePerSecond, int maxServiceTimeMs) {
            webClients = cq;
            this.maxHitRatePerSecond = maxHitRatePerSecond;
            this.maxServiceTimeMs = maxServiceTimeMs;
        }

        @Override
        public void run() {
            try {
                while(!Thread.interrupted()) {
                    TimeUnit.MILLISECONDS.sleep(rand.nextInt(1000));
                    for (int i = 0; i < rand.nextInt(maxHitRatePerSecond); i++) {
                        webClients.put(new WebClient(rand.nextInt(maxServiceTimeMs)));
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
                    TimeUnit.MILLISECONDS.sleep(
                            webClient.getServiceTime());
                    synchronized(this) {
                        webClientsServed++;
//                        while(!servingCustomerLine)
//                            wait();
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

        private List<WebServer> workingWebServers =
                new ArrayList<WebServer>();

        //private long secondCounter;
        private long totalProcessed;

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
                while(!Thread.interrupted()) {
                    TimeUnit.SECONDS.sleep(1);
                    long totalHits = 0;
                    for(WebServer webServer : workingWebServers)
                        totalHits += webServer.getWebClientsServed();

                    System.out.format("Total servers: %d, Load per second: %d Clients waiting: %d%n", workingWebServers.size(), (totalHits - totalProcessed), webClients.size());
                    totalProcessed = totalHits;
                }
            } catch(InterruptedException e) {
                System.out.println(this + "interrupted");
            }
            System.out.println(this + "terminating");
        }
        public String toString() { return "ServerLoadRecorder "; }
    }

    static class WebServerSimulation {
        static final int SERVER_COUNT = 4;

        public static void main(String[] args) throws Exception {
            ExecutorService exec = Executors.newCachedThreadPool();
            // If line is too long, webQueue will leave:
            WebQueue webQueue = new WebQueue();

            int maxHitRatePerSecond = 10000;
            int maxServiceTimeMs = 1;


            exec.execute(new WebClientGenerator(webQueue, maxHitRatePerSecond, maxServiceTimeMs));
            // Manager will add and remove tellers as necessary:
            exec.execute(new ServerLoadRecorder(
                    exec, webQueue, SERVER_COUNT));
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
