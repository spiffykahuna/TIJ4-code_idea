package enumerated.exercises;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;


import static enumerated.exercises.VendingMachine11.State.*;
import static java.math.BigDecimal.ONE;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class Exercise11Test {


    @Before
    public void setUp() {
        VendingMachine11.init();

        VendingMachine11.loadVendingItems(new ArrayList<VendedItem>() {{
            add(new VendedItem("Soda", BigDecimal.valueOf(1)));
            add(new VendedItem("Juice", BigDecimal.valueOf(1.25)));
            add(new VendedItem("HotChocolate", BigDecimal.valueOf(1)));
            add(new VendedItem("Coffee", BigDecimal.valueOf(0.45)));
            add(new VendedItem("CandyBar", BigDecimal.valueOf(0.90)));
        }});
    }

    @Test
    public  void validScenario(){
        VendingMachine11.input(new MonetaryUnit(BigDecimal.valueOf(2)));
        assertEquals(ADDING_MONEY, VendingMachine11.currentState());
        assertEquals(BigDecimal.valueOf(2), VendingMachine11.getClientBalance());


        VendingMachine11.input(new VendedItem("Soda", BigDecimal.valueOf(2)));
        assertEquals(RESTING, VendingMachine11.currentState());
        assertEquals(BigDecimal.ZERO, VendingMachine11.getClientBalance());
    }

    @Test(expected = IllegalArgumentException.class)
    public void configurationFailsIfConfigIsEmpty() {
        VendingMachine11.loadVendingItems(new ArrayList<VendedItem>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void configurationFailsIfConfigIsNull() {
        VendingMachine11.loadVendingItems(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void thatLoadingFailsIfVendedItemsHaveNegativePrice() {
        VendingMachine11.loadVendingItems(asList(
                new VendedItem("SimpleCoffee", BigDecimal.valueOf(1)),
                new VendedItem("GiftToClient", BigDecimal.valueOf(-Double.MIN_VALUE)),
                new VendedItem("CreditForever", BigDecimal.valueOf(Double.MAX_VALUE))
        ));
    }

    @Test
    public void thatProperAmountOfItemsWasLoaded() {

        assertTrue(VendingMachine11.vendingItemsAvailable.size() > 1);

        VendingMachine11.loadVendingItems(new ArrayList<VendedItem>() {{
            add(new VendedItem("Soda", BigDecimal.valueOf(1.00)));
            add(new VendedItem("Juice", BigDecimal.valueOf(1.25)));
            add(new VendedItem("HotChocolate", BigDecimal.valueOf(1.00)));
            add(new VendedItem("Coffee", BigDecimal.valueOf(0.45)));
        }});

        assertEquals(4, VendingMachine11.vendingItemsAvailable.size());
    }

    @Test(expected = InsufficientFoundsException.class)
    public void thatMachineThrowsExceptionWhenThereIsInsufficientFoundsToBySmth() throws Exception {
        VendingMachine11.input(new MonetaryUnit(BigDecimal.valueOf(1)));
        assertEquals(ADDING_MONEY, VendingMachine11.currentState());
        assertEquals(BigDecimal.valueOf(1), VendingMachine11.getClientBalance());


        VendingMachine11.input(new VendedItem("Soda", BigDecimal.valueOf(2)));
    }

    @Test(expected = InvalidVendingItemException.class)
    public void thatOnlyRegisteredItemsAreAllowed() throws Exception {
        VendingMachine11.input(new MonetaryUnit(ONE));
        assertEquals(ADDING_MONEY, VendingMachine11.currentState());
        assertEquals(ONE, VendingMachine11.getClientBalance());


        VendingMachine11.input(new VendedItem("SomeUnknownVendingItem", ONE));
    }
}
