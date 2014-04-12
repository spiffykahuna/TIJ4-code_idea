package enumerated.exercises;


import java.util.*;
import net.mindview.util.*;
import static net.mindview.util.Print.*;

class Mail9 {
    // The NO's lower the probability of random selection:
    enum GeneralDelivery { YES, NO1, NO2, NO3, NO4, NO5 }
    enum Scannability { UNSCANNABLE, YES1, YES2, YES3, YES4 }
    enum Readability { ILLEGIBLE, YES1, YES2, YES3, YES4 }
    enum Address { INCORRECT, OK1, OK2, OK3, OK4, OK5, OK6 }
    enum ReturnAddress { MISSING, OK1, OK2, OK3, OK4, OK5 }
    GeneralDelivery generalDelivery;
    Scannability scannability;
    Readability readability;
    Address address;
    ReturnAddress returnAddress;
    static long counter = 0;
    long id = counter++;
    public String toString() { return "Mail " + id; }
    public String details() {
        return toString() +
                ", General Delivery: " + generalDelivery +
                ", Address Scannability: " + scannability +
                ", Address Readability: " + readability +
                ", Address Address: " + address +
                ", Return address: " + returnAddress;
    }
    // Generate test Mail:
    public static Mail9 randomMail() {
        Mail9 m = new Mail9();
        m.generalDelivery = Enums.random(GeneralDelivery.class);
        m.scannability = Enums.random(Scannability.class);
        m.readability = Enums.random(Readability.class);
        m.address = Enums.random(Address.class);
        m.returnAddress = Enums.random(ReturnAddress.class);
        return m;
    }

    public static Iterable<Mail9> generator(final int count) {
        return new Iterable<Mail9>() {
            int n = count;
            public Iterator<Mail9> iterator() {
                return new Iterator<Mail9>() {
                    public boolean hasNext() { return n-- > 0; }
                    public Mail9 next() { return randomMail(); }
                    public void remove() { // not implemented
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}

interface Handler { abstract boolean handle(Mail9 m); } // Command design pattern

public class Exercise9 {
    enum MailHandler { GENERAL_DELIVERY, MACHINE_SCAN, VISUAL_INSPECTION, RETURN_TO_SENDER }
    public static void handle(Mail9 m, EnumMap<MailHandler,Handler> em) {
        for(Map.Entry<MailHandler,Handler> e : em.entrySet()) {
            if(e.getValue().handle(m)) return;
        }
        print(m + " is a dead letter");
    }
    public static void main(String[] args) {
        EnumMap<MailHandler,Handler> em = new EnumMap<MailHandler,Handler>(MailHandler.class);
        em.put(MailHandler.GENERAL_DELIVERY, new Handler() {
            public boolean handle(Mail9 m) {
                switch(m.generalDelivery) {
                    case YES:
                        print("Using general delivery for " + m);
                        return true;
                    default: return false;
                }
            }
        });
        em.put(MailHandler.MACHINE_SCAN, new Handler() {
            public boolean handle(Mail9 m) {
                switch(m.scannability) {
                    case UNSCANNABLE: return false;
                    default:
                        switch(m.address) {
                            case INCORRECT: return false;
                            default:
                                print("Delivering " + m + " automatically");
                                return true;
                        }
                }
            }
        });
        em.put(MailHandler.VISUAL_INSPECTION, new Handler() {
            public boolean handle(Mail9 m) {
                switch(m.readability) {
                    case ILLEGIBLE: return false;
                    default:
                        switch(m.address) {
                            case INCORRECT: return false;
                            default:
                                print("Delivering " + m + " normally");
                                return true;
                        }
                }
            }
        });
        em.put(MailHandler.RETURN_TO_SENDER, new Handler() {
            public boolean handle(Mail9 m) {
                switch(m.returnAddress) {
                    case MISSING: return false;
                    default:
                        print("Return " + m + " to sender");
                        return true;
                }
            }
        });
        for(Mail9 mail : Mail9.generator(10)) {
            print(mail.details());
            handle(mail, em);
            print("*****");
        }
    }
}


