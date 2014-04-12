package enumerated.exercises;

import java.util.*;

import net.mindview.util.*;

import static net.mindview.util.Print.*;

enum Input10 {
    NICKEL(5), DIME(10), QUARTER(25), DOLLAR(100),
    TOOTHPASTE(200), CHIPS(75), SODA(100), SOAP(50),
    ABORT_TRANSACTION {
        public int amount() { // Disallow
            throw new IllegalStateException("ABORT.amount()");
        }
    },
    STOP { // This must be the last instance.
        public int amount() { // Disallow
            throw new IllegalStateException("SHUT_DOWN.amount()");
        }
    };
    int value; // In cents

    Input10(int value) {
        this.value = value;
    }

    Input10() { }

    int amount() {
        return value;
    }

    ; // In cents

    static Random rand = new Random(47);

    public static Input10 randomSelection() {
        // Don't include STOP:
        return values()[rand.nextInt(values().length - 1)];
    }
}

public class Exercise10 {
    public static void main(String[] args) {
        VendingMachine10.main(args);
    }
}


enum Category {
    MONEY(Input10.NICKEL, Input10.DIME, Input10.QUARTER, Input10.DOLLAR),
    ITEM_SELECTION(Input10.TOOTHPASTE, Input10.CHIPS, Input10.SODA, Input10.SOAP),
    QUIT_TRANSACTION(Input10.ABORT_TRANSACTION),
    SHUT_DOWN(Input10.STOP);
    private Input10[] values;

    Category(Input10... types) {
        values = types;
    }

    private static EnumMap<Input10, Category> categories =
            new EnumMap<Input10, Category>(Input10.class);

    static {
        for (Category c : Category.class.getEnumConstants())
            for (Input10 type : c.values)
                categories.put(type, c);
    }

    public static Category categorize(Input10 input) {
        return categories.get(input);
    }
}

interface Command {  // In order to use a Command Design Pattern

    void next(Input10 input);

    void next();
}

enum State {
    RESTING,
    ADDING_MONEY,
    DISPENSING,
    GIVING_CHANGE,
    TERMINAL
}

// For a basic sanity check:
class RandomInput10Generator implements Generator<Input10> {
    public Input10 next() {
        return Input10.randomSelection();
    }
}

class VendingMachine10 {
    int id = ++count;
    static int count = 0;
    State state = State.RESTING;
    int amount = 0; // for each transaction
    int banked = 0; // retained after transactions
    Input10 input = null;
    Input10 selection = null;
    boolean isTransient = false;

    // Enums must be static, sot use classes instead:
    class RestingCmd implements Command {
        public void next(Input10 in) {
            isTransient = false;
            input = in;
            switch (Category.categorize(in)) {
                case MONEY:
                    amount += in.amount();
                    state = State.ADDING_MONEY;
                    break;
                case SHUT_DOWN:
                    state = State.TERMINAL;
                default:
            }
        }

        public void next() {
            isTransient = false;
        }
    }

    class AddingMoneyCmd implements Command {
        public void next(Input10 input) {
            isTransient = false;
            switch (Category.categorize(input)) {
                case MONEY:
                    amount += input.amount();
                    break;
                case ITEM_SELECTION:
                    selection = input;
                    if (amount < selection.amount()) {
                        print("Insufficient money for " + selection);
                    } else {
                        state = State.DISPENSING;
                        isTransient = true;
                    }
                    break;
                case QUIT_TRANSACTION:
                    state = State.GIVING_CHANGE;
                    isTransient = true;
                    break;
                case SHUT_DOWN:
                    state = State.TERMINAL;
                    banked = banked += amount;
                default:
            }
        }

        public void next() {
            isTransient = false;
        }
    }

    class DispensingCmd implements Command {
        public void next() {
            isTransient = true;
            print("Here is your " + selection);
            state = State.GIVING_CHANGE;
        }

        public void next(Input10 input) {
            isTransient = true;
            print("Here is your " + selection);
            state = State.GIVING_CHANGE;
        }
    }

    class GivingChangeCmd implements Command {
        public void next(Input10 input) {
            isTransient = true;
            if (amount > selection.amount()) {
                print("Your change: " + (amount - selection.amount()));
            }
            banked = banked += selection.amount();
            amount = 0; // reset
            state = State.RESTING;
        }

        public void next() {
            isTransient = true;
            if(null == selection) {
                print("Returning your: " + amount);
                amount = 0;
                state = State.RESTING;
                return;
            }

            if (amount < selection.amount())
                print("Returning your: " + amount);
            if (amount > selection.amount()) {
                print("Your change: " + (amount - selection.amount()));
                banked = banked += selection.amount();
            }
            if (amount == selection.amount())
                banked = banked += selection.amount();
            amount = 0;
            state = State.RESTING;
        }
    }

    class TerminalCmd implements Command {
        public void next(Input10 input) {
            print("state TERMINAL");
            isTransient = false;
        }

        public void next() {
            print("state TERMINAL");
            isTransient = false;
        }
    }

    Map<State, Command> stateCommands =
            Collections.synchronizedMap(new EnumMap<State, Command>(State.class));

    VendingMachine10() { // Load up the EnumMap in the constructor
        print("VendingMachine10()#" + id);

        stateCommands.put(State.RESTING, new RestingCmd());
        stateCommands.put(State.ADDING_MONEY, new AddingMoneyCmd());
        stateCommands.put(State.DISPENSING, new DispensingCmd());
        stateCommands.put(State.GIVING_CHANGE, new GivingChangeCmd());
        stateCommands.put(State.TERMINAL, new TerminalCmd());
    }

    void showAmount() {
        print("amount = " + amount);
    }

    void showBanked() {
        print("banked = " + banked);
    }

    public static void main(String[] args) {
        Generator<Input10> gen = new FileInput10Generator10("VendingMachine10aInput10.txt");

        VendingMachine10 vm10a = new VendingMachine10();
        VendingMachine10 vm10b = new VendingMachine10();
        VendingMachine10 vm10c = new VendingMachine10();

        print();
        print("Testing VendingMachine 10a:");
        testVendingMachine(gen, vm10a);
        vm10a.showBanked();

        print();
        print("Testing VendingMachine 10b:");
        gen = new FileInput10Generator10("VendingMachine10bInput10.txt");
        testVendingMachine(gen, vm10b);
        print();
        print("Testing VendingMachine 10c:");
        gen = new FileInput10Generator10("VendingMachine10cInput10.txt");
        testVendingMachine(gen, vm10c);
    }

    private static void testVendingMachine(Generator<Input10> inputGenerator, VendingMachine10 vendingMachine) {
        while (vendingMachine.state != State.TERMINAL) {
            Input10 input = inputGenerator.next();
            (vendingMachine.stateCommands.get(vendingMachine.state)).next(input);
            while (vendingMachine.isTransient) {
                (vendingMachine.stateCommands.get(vendingMachine.state)).next();
            }
            vendingMachine.showAmount();
        }
    }
}

// Create inputs from a file of ';'-separated strings;
class FileInput10Generator10 implements Generator<Input10> {
    private Iterator<String> input;

    public FileInput10Generator10(String fileName) {
        input = new TextFile(fileName, ";").iterator();
    }

    public Input10 next() {
        if (input.hasNext()) {
            return Input10.valueOf(input.next().trim());
        }
        return null;
    }
}