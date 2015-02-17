package concurrency.exercises;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static net.mindview.util.Print.print;

/**
 *
 Exercise 41: (6) Add a message handler to ActiveObject.java that has no return
 value, and call this within main( ).
 */
public class Exercise41 {
    private static class Message {
        private final String msgText;

        public Message(String msgText) {
            this.msgText = msgText;
        }

        public String getMsgText() {
            return msgText;
        }
    }

    private interface MessageHandler {
        public void handleMessage(final Message message);
        public void handleMessage(final Message message, final MessageHandler notifier);
    }

    private static class ActiveObject implements MessageHandler {
        private ExecutorService ex =
                Executors.newSingleThreadExecutor();
        private Random rand = new Random(47);

        private static long counter;
        private final long id = counter++;

        // Insert a random delay to produce the effect
        // of a calculation time:
        private void pause(int factor) {
            try {
                TimeUnit.MILLISECONDS.sleep(
                        100 + rand.nextInt(factor));
            } catch(InterruptedException e) {
                print("sleep() interrupted");
            }
        }
        public Future<Integer>
        calculateInt(final int x, final int y) {
            return ex.submit(new Callable<Integer>() {
                public Integer call() {
                    print("starting " + x + " + " + y);
                    pause(500);
                    return x + y;
                }
            });
        }
        public Future<Float>
        calculateFloat(final float x, final float y) {
            return ex.submit(new Callable<Float>() {
                public Float call() {
                    print("starting " + x + " + " + y);
                    pause(2000);
                    return x + y;
                }
            });
        }
        public void shutdown() { ex.shutdown(); }

        public void handleMessage(final Message message) {
            handleMessage(message, null);
        }

        @Override
        public void handleMessage(final Message message, final MessageHandler notifier) {
            final String activeObjectName = this.toString();
            ex.submit(new Runnable() {
                @Override
                public void run() {
                    print("New Message arrived to " + activeObjectName);
                    pause(2000);
                    print("Message: " + message.getMsgText());
                    if(null != notifier) {
                        String replyTxt = String.format(
                                "<processed>%s</processed>",
                                message.getMsgText()
                        );
                        notifier.handleMessage(new Message(replyTxt));
                    }
                }
            });
        }

        @Override
        public String toString() {
            return "ActiveObject{" +
                    "id=" + id +
                    '}';
        }

        public static void main(String[] args) throws InterruptedException {
            ActiveObject d1 = new ActiveObject();
            // Prevents ConcurrentModificationException:
            List<Future<?>> results =
                    new CopyOnWriteArrayList<Future<?>>();
            for(float f = 0.0f; f < 1.0f; f += 0.2f)
                results.add(d1.calculateFloat(f, f));
            for(int i = 0; i < 5; i++)
                results.add(d1.calculateInt(i, i));
            print("All asynch calls made");
            while(results.size() > 0) {
                for(Future<?> f : results)
                    if(f.isDone()) {
                        try {
                            print(f.get());
                        } catch(Exception e) {
                            throw new RuntimeException(e);
                        }
                        results.remove(f);
                    }
            }

            for(int i = 0; i < 5; i++)
                d1.handleMessage(new Message("Message_" + i));
            print("All messages are submitted");

            d1.shutdown();

            List<ActiveObject> senders = new ArrayList<ActiveObject>(10);
            List<ActiveObject> receivers = new ArrayList<ActiveObject>(10);
            for(int i = 0; i < 10; i++) {
                senders.add(new ActiveObject());
                receivers.add(new ActiveObject());
            }
            for(ActiveObject sender: senders) {
                for(ActiveObject receiver: receivers) {
                    receiver.handleMessage(new Message("Greeting from " + sender), sender);
                }
            }

            print("All greetings are also submitted");

            print("Main finished");


            TimeUnit.SECONDS.sleep(60);

            for(ActiveObject receiver: receivers) {
                receiver.shutdown();
            }
            for(ActiveObject sender: senders) {
                sender.shutdown();
            }


        }
    }

    public static void main(String[] args) throws InterruptedException {
        ActiveObject.main(args);
    }

}
