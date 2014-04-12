package enumerated.exercises;

import net.mindview.util.Enums;

public class Exercise6 {
    public static void main(String[] args) {
        Meal6.main(args);
    }
}

// Standalone enum technic works (see below), but code is not as clear.

interface Food2 {}

enum Appetizer implements Food2 {
    SALAD, SOUP, SPRING_ROLLS;
}
enum MainCourse implements Food2 {
    LASAGNE, BURRITO, PAD_THAI,
    LENTILS, HUMMOUS, VINDALOO;
}
enum Dessert implements Food2 {
    TIRAMISU, GELATO, BLACK_FOREST_CAKE,
    FRUIT, CREME_CARAMEL;
}
enum Coffee implements Food2 {
    BLACK_COFFEE, DECAF_COFFEE, ESPRESSO,
    LATTE, CAPPUCCINO, TEA, HERB_TEA;
}

enum Meal6 {
    APPETIZER(Appetizer.class),
    MAINCOURSE(MainCourse.class),
    DESSERT(Dessert.class),
    COFFEE(Coffee.class);
    private Food2[] values;
    private Meal6(Class<? extends Food2> kind) {
        values = kind.getEnumConstants();
    }
    public Food2 randomSelection() {
        return Enums.random(values);
    }
    public static void main(String[] args) {
        for(int i = 0; i < 5; i++) {
            for(Meal6 meal6 : Meal6.values()) {
                Food2 food2 = meal6.randomSelection();
                System.out.println(food2);
            }
            System.out.println("---");
        }
    }
}