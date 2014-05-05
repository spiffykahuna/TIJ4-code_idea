package enumerated.exercises;

import java.math.BigDecimal;
import java.util.*;



import net.mindview.util.*;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;

import static java.math.BigDecimal.ZERO;
import static net.mindview.util.Print.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/** Files required for input, in same package:
 VendingMachineInput.txt
 QUARTER;QUARTER;QUARTER;CHIPS;
 DOLLAR; DOLLAR; TOOTHPASTE;
 QUARTER; DIME; SODA;
 QUARTER; DIME; NICKEL; SODA;
 ABORT_TRANSACTION;
 STOP;
 VendingMachine11Input.txt:
 Money:Nickel(5),Dime(10),Quarter(25),Dollar(100);Selection:Soda(100),Juice(125),
 HotChocolate(100),Coffee(145),CandyBar(90);VendingEvent:AbortTransaction,Stop,
 **/

interface VendingItem { String name(); }

abstract class AbstractVendingItem implements VendingItem{
    protected String name;

    public AbstractVendingItem(String name) {
        this.name = name;
    }

    public String name() { return name; }
}

class MonetaryUnit extends AbstractVendingItem {
    private BigDecimal amount = ZERO;

    MonetaryUnit(BigDecimal amount) {
        super("Money");
        this.amount = amount;
    }

    public BigDecimal amount() { return amount; }
}

class VendedItem extends AbstractVendingItem {

    private BigDecimal price = ZERO;

    VendedItem(String name, BigDecimal price) {
        super(name);
        this.price = price;
    }

    public BigDecimal price() { return price; }
}

class VendingEvent extends AbstractVendingItem {
    VendingEvent(String name) {
        super(name);
    }
}


class InsufficientFoundsException extends RuntimeException {
    public InsufficientFoundsException(String msg) {
        super(msg);
    }
}

class InvalidVendingItemException extends RuntimeException {
    public InvalidVendingItemException(String msg) {
        super(msg);
    }
}

class VendingMachine11 {

    private static State state = State.RESTING;

    private static BigDecimal amount = ZERO;

    private static VendedItem selection = null;

    static List<VendedItem> vendingItemsAvailable;

    public static void init() {
        if(null != vendingItemsAvailable)
            vendingItemsAvailable.clear();
        else {
            vendingItemsAvailable = new ArrayList<VendedItem>();
        }
        amount = ZERO;
        selection = null;
    }


    enum StateDuration {TRANSIENT;} // Tagging enum
    enum State {
        RESTING {
            void next(VendingItem in) {
                if (MonetaryUnit.class.isInstance(in)) {
                    amount = amount.add(((MonetaryUnit) in).amount());
                    state = ADDING_MONEY;
                }
                if (VendingEvent.class.isInstance(in)) {
                    if (((VendingEvent) in).name().equals("Stop"))
                        state = TERMINAL;
                }
            }
        },
        ADDING_MONEY {
            void next(VendingItem in) {
                if (MonetaryUnit.class.isInstance(in)) {
                    amount = amount.add(((MonetaryUnit) in).amount());
                }
                if (VendedItem.class.isInstance(in)) {
                    selection = (VendedItem) in;
                    if (amount.compareTo(selection.price()) == -1 ) {
                        throw new InsufficientFoundsException("Insufficient money for " + selection.name());
                    } else if(!isValidVendingItem(selection)) {
                        throw new InvalidVendingItemException("Invalid vending item: " + selection
                                + " !  Should be one of " + vendingItemsAvailable);
                    }
                    else state = DISPENSING;
                }
                if (VendingEvent.class.isInstance(in)) {
                    if (((VendingEvent) in).name().equals("AbortTransaction"))
                        state = GIVING_CHANGE;
                    if (((VendingEvent) in).name().equals("Stop"))
                        state = TERMINAL;
                }
            }
        },
        DISPENSING(StateDuration.TRANSIENT) {
            void next() {
                print("here is your " + selection.name());
                amount = amount.subtract(selection.price());
                state = GIVING_CHANGE;
            }
        },
        GIVING_CHANGE(StateDuration.TRANSIENT) {
            void next() {
                if (amount.compareTo(ZERO) == 1) {
                    print("Your change: " + amount);
                    amount = ZERO;
                }
                state = RESTING;
            }
        },
        TERMINAL {
            void output() {
                print("Halted");
            }
        };

        private boolean isTransient = false;

        State() {
        } // no-arg constructor

        State(StateDuration trans) {
            isTransient = true;
        }

        void next(VendingItem in) {
            throw new RuntimeException("Only call " +
                    "next(VendingItem in) for non-transient states");
        }

        void next() {
            throw new RuntimeException("Only call next() for " +
                    "StateDuration.TRANSIENT states");
        }

        void output() {
            print(amount);
        }

    }
    static void input(VendingItem item) {
        state.next(item);
        while (state.isTransient)
            state.next();
        state.output();
    }

    static void input(Collection<VendingItem> items) {
        for(VendingItem item: items) {
            input(item);
        }
    }

    static BigDecimal getClientBalance() {return amount;}


    static State currentState() { return state;}

    public static void loadVendingItems(Collection<VendedItem> vendedItems) {
        if(null == vendedItems) throw new IllegalArgumentException("Vending items collection is null");
        if(vendedItems.isEmpty()) throw new IllegalArgumentException("Empty vending items collection");

        vendingItemsAvailable = new ArrayList<VendedItem>(vendedItems.size());

        for(VendedItem vendedItem: vendedItems) {
            if(null == vendedItem.price() || vendedItem.price().compareTo(ZERO) < 0) {
                vendingItemsAvailable = new ArrayList<VendedItem>();
                throw new IllegalArgumentException(
                        String.format(
                                "Wrong vended item price: item=%s price=%s",
                                vendedItem.name(), vendedItem.price())
                );
            }

            vendingItemsAvailable.add(vendedItem);
        }
    }

    private static  boolean isValidVendingItem(VendedItem item) {
        for(VendedItem loadedItem: vendingItemsAvailable) {
            if(loadedItem.name().equalsIgnoreCase(item.name())){
                return true;
            }
        }
        return false;
    }

    private String parseConfigKey(String key) {
        return key.split("\\.")[1];
    }

    static void run(Generator<VendingItem> gen) {
        while (state != State.TERMINAL) {
            state.next(((FileInputGenerator11) gen).next());
            while (state.isTransient)
                state.next();
            state.output();
        }
        state = State.RESTING;
        print();
    }

    static void runRandom(Generator<VendingItem> gen) {
        while (state != State.TERMINAL) {
            state.next(((FileInputGenerator11) gen).randomNext());
            while (state.isTransient)
                state.next();
            state.output();
        }
        state = State.RESTING;
        print();
    }

    static void runTextExample(Generator<VendingItem> gen) {
        while (state != State.TERMINAL) {
            // for(int i = 0; i < 16; i++) {
            state.next(((FileInputGenerator11) gen).textExampleNext("VendingMachineInput.txt"));
            while (state.isTransient)
                state.next();
            state.output();
        }
        state = State.RESTING;
        print();
    }

    public static void main(String[] args) {
        FileInputGenerator11 gen =
                new FileInputGenerator11("VendingMachine11Input.txt");
        runRandom(gen); // random vending inputs
        run(gen);
        runTextExample(gen); // inputs from VendingMachineInput.txt
    }
}

class FileInputGenerator11 implements Generator<VendingItem> {
    private ArrayList<String> list;
    private List<VendingItem> vendList = new ArrayList<VendingItem>();
    private Iterator<VendingItem> it;
    private Random rand = new Random();
    private Iterator<String> input = new TextFile("VendingMachineInput.txt", ";").iterator();

    // Construct List of VendingItem from appropriately formatted txt file:
    public FileInputGenerator11(String fileName) {
        list = new TextFile(fileName, ",|;|:");
        int m = list.indexOf("Money");
        int se = list.indexOf("Selection");
        int e = list.indexOf("VendingEvent");
        VendingItem vIn;
        for (String s : list) {
            int x = list.indexOf(s);
            if (m < x && x < se) {
                String[] sa = s.split("\\(|\\)");
                vIn = new MonetaryUnit(new BigDecimal(sa[1]));
                vendList.add(vIn);
            } else if (se < x && x < e) {
                String[] sa = s.split("\\(|\\)");
                vIn = new VendedItem(sa[0], new BigDecimal(sa[1]));
                vendList.add(vIn);
            } else if (e < x) {
                vIn = new VendingEvent(s);
                vendList.add(vIn);
            }
        }
        it = vendList.iterator();
    }

    public VendingItem next() {
        if (list.isEmpty()) return null;
        if (it.hasNext()) {
            return it.next();
        }
        return null;
    }

    public VendingItem randomNext() {
        return vendList.get(rand.nextInt(vendList.size()));
    }

    public VendingItem textExampleNext(String fileName) {
        if (!input.hasNext()) return null;
        String s = input.next().trim();
        String s1 = s.charAt(0) + s.substring(1).toLowerCase();
        for (int i = 0; i < this.vendList.size(); i++) {
            if (vendList.get(i).name().equals(s1)) {
                return vendList.get(i);
            }
        }
        return null;
    }
}

@RunWith(Suite.class)
@Suite.SuiteClasses({
    Exercise11Test.class
})
class JunitTestSuite {}


public class Exercise11 {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(JunitTestSuite.class);

        for(Failure fail:result.getFailures()) {
            System.out.println(fail.toString());
        }

        if(result.wasSuccessful()){
            System.out.println("All tests finished successfully...");
        }
    }
}