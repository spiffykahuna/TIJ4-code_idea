package enumerated.exercises;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Exercise11Test {
    VendingMachine11 machine;
    HashMap<String, BigDecimal> config;

    @Before
    public void setUp() {
        machine = new VendingMachine11();
        config = new HashMap<String, BigDecimal>();

        machine.configure(new HashMap<String, BigDecimal>() {{
            put("monetaryUnit.NICKEL", BigDecimal.valueOf(5));
            put("monetaryUnit.DIME", BigDecimal.valueOf(10));
            put("monetaryUnit.QUARTER", BigDecimal.valueOf(25));
            put("monetaryUnit.DOLLAR", BigDecimal.valueOf(100));

            put("vendedItem.Soda", BigDecimal.valueOf(100));
            put("vendedItem.Juice", BigDecimal.valueOf(125));
            put("vendedItem.HotChocolate", BigDecimal.valueOf(100));
            put("vendedItem.Coffee", BigDecimal.valueOf(45));
            put("vendedItem.CandyBar", BigDecimal.valueOf(90));

        }});
    }

    @Test
    public  void validScenario(){
        VendingItem[] inputItems = new VendingItem[]{

        };
//        machine.input( Arrays.<VendingItem>asList(new MonetaryUnit("hehe", 2)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void configurationFailsIfConfigIsEmpty() {
        assertTrue(config.isEmpty());
        machine.configure(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void configurationFailsIfConfigIsNull() {
        machine.configure(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void configurationFailsIfConfigIsNoMonetaryItemsProvided() {
        config.put("event.STOP", BigDecimal.valueOf(Double.MIN_VALUE));
        machine.configure(config);
    }

    @Test
    public void configurationContainsValidMonetaryUnits() {
        machine.configure(new HashMap<String, BigDecimal>() {{
            put("monetaryUnit.NICKEL", BigDecimal.valueOf(5));
            put("monetaryUnit.DIME", BigDecimal.valueOf(10));
            put("monetaryUnit.QUARTER", BigDecimal.valueOf(25));
            put("monetaryUnit.DOLLAR", BigDecimal.valueOf(100));

            put("vendedItem.Soda", BigDecimal.valueOf(100));
        }});

        assertEquals(4, machine.monetaryUnits.size());
    }


    @Test(expected = IllegalArgumentException.class)
    public void configurationFailsIfConfigIsNoVendedItemsProvided() throws Exception {
        config.put("monetaryUnit.DOLLAR", BigDecimal.valueOf(100));
        config.put("event.STOP", BigDecimal.valueOf(45));

        machine.configure(config);
    }

}
