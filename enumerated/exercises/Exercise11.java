package enumerated.exercises;

import net.mindview.util.Generator;
import net.mindview.util.TextFile;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;

import java.math.BigDecimal;
import java.util.*;

import static java.math.BigDecimal.ZERO;
import static net.mindview.util.Print.print;


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
// TODO finish here.
interface VendingItem { String name(); }

abstract class AbstractVendingItem {
    protected String name;

    public AbstractVendingItem(String name) {
        this.name = name;
    }

    public String name() { return name; }
}

class MonetaryUnit extends AbstractVendingItem implements VendingItem {
    private BigDecimal amount = ZERO;

    MonetaryUnit(String name, BigDecimal amount) {
        super(name);
        this.amount = amount;
    }

    public BigDecimal amount() { return amount; }
}

class VendedItem extends AbstractVendingItem implements VendingItem {

    private BigDecimal price = ZERO;

    VendedItem(String name, BigDecimal price) {
        super(name);
        this.price = price;
    }

    public BigDecimal price() { return price; }
}

class VendingEvent extends AbstractVendingItem implements VendingItem {
    VendingEvent(String name) {
        super(name);
    }
}

class VendingMachine11 {

    private static State state = State.RESTING;

    private static BigDecimal amount = ZERO;

    private static VendedItem selection = null;

    List<MonetaryUnit> monetaryUnits;
    List<VendedItem> vendingItemsAvailable;


    enum StateDuration {TRANSIENT;} // Tagging enum
    enum State {
        RESTING {
            @Override
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
            @Override
            void next(VendingItem in) {
                if (MonetaryUnit.class.isInstance(in)) {
                    amount = amount.add(((MonetaryUnit) in).amount());
                }
                if (VendedItem.class.isInstance(in)) {
                    selection = (VendedItem) in;
                    if (amount.compareTo(selection.price()) == -1 )
                        print("Insufficient money for " + selection.name());
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
            @Override
            void next() {
                print("here is your " + selection.name());
                amount = amount.subtract(selection.price());
                state = GIVING_CHANGE;
            }
        },
        GIVING_CHANGE(StateDuration.TRANSIENT) {
            @Override
            void next() {
                if (amount.compareTo(ZERO) == 1) {
                    print("Your change: " + amount);
                    amount = ZERO;
                }
                state = RESTING;
            }
        },
        TERMINAL {
            @Override
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
    void input(VendingItem item) {

    }

    void input(Collection<VendingItem> items) {
        for(VendingItem item: items) {
            input(item);
        }
    }

    BigDecimal getAmount() {return amount;}


    State currentState() { return state;}

    public void configure(Map<String, BigDecimal> machineConfig) {
        if(null == machineConfig) throw new IllegalArgumentException("Config is null");
        if(machineConfig.isEmpty()) throw new IllegalArgumentException("Empty config");

        monetaryUnits = new ArrayList<MonetaryUnit>();
        vendingItemsAvailable = new ArrayList<VendedItem>();

        for(Map.Entry<String,BigDecimal> param: machineConfig.entrySet()) {
            String key = param.getKey();
            if(key.startsWith("monetaryUnit")) {
                String unitName = parseConfigKey(key);
                monetaryUnits.add(new MonetaryUnit(unitName.toUpperCase(), param.getValue()));
            }

            if(key.startsWith("vendedItem")) {
                String itemName = parseConfigKey(key);
                vendingItemsAvailable.add(new VendedItem(itemName, param.getValue()));
            }
        }

        if(monetaryUnits.isEmpty())
            throw new IllegalArgumentException("No monetary units specified in config");

        if(vendingItemsAvailable.isEmpty())
            throw new IllegalArgumentException("No vending items specified");
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
                vIn = new MonetaryUnit(sa[0], new BigDecimal(sa[1]));
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

    @Override
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