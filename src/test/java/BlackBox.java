import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Black-Box Testing - Assignment 2
 * Student: Malek Ibrahim
 * ASU ID: Mibrah23
 * Date: October 29, 2025
 */

/**
 * Black-box test for processOrderItem method.
 * Students will expand this test suite with comprehensive test cases.
 * This parameterized structure allows testing all Order implementations with the same tests.
 */
public class BlackBox {

    private Order order;
    private Restaurant restaurant;

    // Provide the list of classes to test
    @SuppressWarnings("unchecked")
    static Stream<Class<? extends Order>> orderClassProvider() {
        return (Stream<Class<? extends Order>>) Stream.of(
//                Order.class,
                Order0.class,
                Order1.class,
                Order2.class,
                Order3.class,
                Order4.class
        );
    }

    @BeforeEach
    public void setUp() {
        restaurant = new Restaurant("Test Restaurant");
    }

    // Helper method to create order instance from class
    private Order createOrder(Class<? extends Order> clazz) throws Exception {
        Constructor<? extends Order> constructor = clazz.getConstructor();
        return constructor.newInstance();
    }

    /**
     * SAMPLE TEST - Students should create many more like this
     * Tests successful addition of a basic item with no modifiers
     * This is testing the valid equivalence partition for a basic order
     */
    @ParameterizedTest
    @MethodSource("orderClassProvider")
    public void testBasicItemAddition(Class<? extends Order> orderClass) throws Exception {
        order = createOrder(orderClass);
        Table table = new Table(1, 2);
        order.initOrder(table, "Test Customer");

        MenuItem burger = restaurant.getMenuItem("B001");
        List<String> noModifiers = new ArrayList<>();

        double result = order.processOrderItem(burger, noModifiers);

        // Should return 0.0 for successful addition
        assertEquals(0.0, result, 0.01,
                "Expected successful addition (0.0) for " + orderClass.getSimpleName());

        // Should have 1 item in order
        assertEquals(1, order.getItems().size(),
                "Should have 1 item in order for " + orderClass.getSimpleName());

        // Total price should equal burger price
        assertEquals(12.99, order.getTotalPrice(), 0.01,
                "Total should equal burger price for " + orderClass.getSimpleName());

        // Order status should still be pending
        assertEquals(0, order.getOrderStatus(),
                "Order status should be pending for " + orderClass.getSimpleName());
    }

    /**
     * SAMPLE TEST - Tests the quantity limit boundary
     * Students should expand with more boundary tests
     */
    @ParameterizedTest
    @MethodSource("orderClassProvider")
    public void testQuantityLimitBoundary(Class<? extends Order> orderClass) throws Exception {
        order = createOrder(orderClass);
        Table table = new Table(1, 2);
        order.initOrder(table, "Test Customer");

        MenuItem salad = restaurant.getMenuItem("S001");

        // Add 5 salads (should all succeed)
        // Note: Salad is APPETIZER, so returns 0.20 (20% promotion)
        for (int i = 0; i < 5; i++) {
            double result = order.processOrderItem(salad, new ArrayList<>());
            assertTrue(result >= 0.0 && result < 1.0,
                    "Adding salad " + (i+1) + " should succeed (0.0-0.99) for " + orderClass.getSimpleName());
        }

        // 6th salad should hit quantity limit
        double result = order.processOrderItem(salad, new ArrayList<>());
        assertEquals(2.0, result, 0.01,
                "Should return 2.0 for quantity limit for " + orderClass.getSimpleName());
    }

    /**
     * SAMPLE TEST - Tests invalid modifier
     * Students should create more tests for different error conditions
     */
    @ParameterizedTest
    @MethodSource("orderClassProvider")
    public void testInvalidModifier(Class<? extends Order> orderClass) throws Exception {
        order = createOrder(orderClass);
        Table table = new Table(1, 2);
        order.initOrder(table, "Test Customer");

        MenuItem soda = restaurant.getMenuItem("D001");
        List<String> modifiers = new ArrayList<>();
        modifiers.add("EXTRA_CHEESE"); // Not allowed on soda

        double result = order.processOrderItem(soda, modifiers);

        // Should return 2.1 for invalid modifier
        assertEquals(2.1, result, 0.01,
                "Should return 2.1 for invalid modifier for " + orderClass.getSimpleName());

        // Item should not be added
        assertEquals(0, order.getItems().size(),
                "Item should not be added for " + orderClass.getSimpleName());
    }


         // TC03: Tests item with null ID (EP3)

        @ParameterizedTest
        @MethodSource("orderClassProvider")
        public void testItemWithNullId(Class<? extends Order> orderClass) throws Exception {
            order = createOrder(orderClass);
            Table table = new Table(1, 2);
            order.initOrder(table, "Test Customer");

            MenuItem item = new MenuItem(null, "Test Item", 10.0, "ENTREE");
            double result = order.processOrderItem(item, null);

            assertEquals(4.1, result, 0.01,
                    "Should return 4.1 for null item ID for " + orderClass.getSimpleName());
            assertEquals(0, order.getItems().size(),
                    "Should have no items for " + orderClass.getSimpleName());
        }


         // TC04: Tests item with invalid ID containing special characters (EP3)

        @ParameterizedTest
        @MethodSource("orderClassProvider")
        public void testItemWithInvalidId(Class<? extends Order> orderClass) throws Exception {
            order = createOrder(orderClass);
            Table table = new Table(1, 2);
            order.initOrder(table, "Test Customer");

            MenuItem item = new MenuItem("B@01!", "Test Item", 10.0, "ENTREE");
            double result = order.processOrderItem(item, null);

            assertEquals(4.1, result, 0.01,
                    "Should return 4.1 for invalid item ID for " + orderClass.getSimpleName());
            assertEquals(0, order.getItems().size(),
                    "Should have no items for " + orderClass.getSimpleName());
        }


         // TC05: Tests unavailable item (EP4)

        @ParameterizedTest
        @MethodSource("orderClassProvider")
        public void testUnavailableItem(Class<? extends Order> orderClass) throws Exception {
            order = createOrder(orderClass);
            Table table = new Table(1, 2);
            order.initOrder(table, "Test Customer");

            MenuItem burger = restaurant.getMenuItem("B001");
            burger.setAvailable(false);

            double result = order.processOrderItem(burger, null);

            assertEquals(3.0, result, 0.01,
                    "Should return 3.0 for unavailable item for " + orderClass.getSimpleName());
            assertEquals(0, order.getItems().size(),
                    "Should have no items for " + orderClass.getSimpleName());
        }


         // TC06: Tests order status = 3 (delivered/finalized) (EP9)

        @ParameterizedTest
        @MethodSource("orderClassProvider")
        public void testOrderFinalized_Status3(Class<? extends Order> orderClass) throws Exception {
            order = createOrder(orderClass);
            Table table = new Table(1, 2);
            order.initOrder(table, "Test Customer");
            order.setOrderStatus(3);

            MenuItem burger = restaurant.getMenuItem("B001");
            double result = order.processOrderItem(burger, null);

            assertEquals(5.0, result, 0.01,
                    "Should return 5.0 for finalized order for " + orderClass.getSimpleName());
            assertEquals(0, order.getItems().size(),
                    "Should have no items for " + orderClass.getSimpleName());
        }


         // TC07: Tests order status = 4 (paid) (EP9)

        @ParameterizedTest
        @MethodSource("orderClassProvider")
        public void testOrderPaid_Status4(Class<? extends Order> orderClass) throws Exception {
            order = createOrder(orderClass);
            Table table = new Table(1, 2);
            order.initOrder(table, "Test Customer");
            order.setOrderStatus(4);

            MenuItem burger = restaurant.getMenuItem("B001");
            double result = order.processOrderItem(burger, null);

            assertEquals(5.0, result, 0.01,
                    "Should return 5.0 for paid order for " + orderClass.getSimpleName());
            assertEquals(0, order.getItems().size(),
                    "Should have no items for " + orderClass.getSimpleName());
        }


         // TC09: Tests quantity below limit - adding 5th item should succeed (EP10)

        @ParameterizedTest
        @MethodSource("orderClassProvider")
        public void testQuantityLimit_BelowLimit(Class<? extends Order> orderClass) throws Exception {
            System.out.println("EGG9: Got me");
            order = createOrder(orderClass);
            Table table = new Table(1, 2);
            order.initOrder(table, "Test Customer");

            MenuItem burger = restaurant.getMenuItem("B001");

            // Add 4 burgers
            for (int i = 0; i < 4; i++) {
                order.processOrderItem(burger, null);
            }

            // Add 5th should work
            double result = order.processOrderItem(burger, null);

            assertEquals(0.0, result, 0.01,
                    "Should return 0.0 when below limit for " + orderClass.getSimpleName());
            assertEquals(5, order.getItemCountById("B001"),
                    "Should have 5 items for " + orderClass.getSimpleName());
        }


         // TC11: Tests incompatible modifiers - NO_CHEESE + EXTRA_CHEESE (EP7)

        @ParameterizedTest
        @MethodSource("orderClassProvider")
        public void testIncompatibleModifiers_Cheese(Class<? extends Order> orderClass) throws Exception {
            order = createOrder(orderClass);
            Table table = new Table(1, 2);
            order.initOrder(table, "Test Customer");

            MenuItem burger = restaurant.getMenuItem("B001");
            List<String> modifiers = Arrays.asList("NO_CHEESE", "EXTRA_CHEESE");

            double result = order.processOrderItem(burger, modifiers);

            assertEquals(2.2, result, 0.01,
                    "Should return 2.2 for incompatible modifiers for " + orderClass.getSimpleName());
            assertEquals(0, order.getItems().size(),
                    "Should have no items for " + orderClass.getSimpleName());
        }


         // TC12: Tests incompatible modifiers - NO_ONIONS + EXTRA_ONIONS (EP7)

        @ParameterizedTest
        @MethodSource("orderClassProvider")
        public void testIncompatibleModifiers_Onions(Class<? extends Order> orderClass) throws Exception {
            order = createOrder(orderClass);
            Table table = new Table(1, 2);
            order.initOrder(table, "Test Customer");

            MenuItem burger = restaurant.getMenuItem("B001");
            List<String> modifiers = Arrays.asList("NO_ONIONS", "EXTRA_ONIONS");

            double result = order.processOrderItem(burger, modifiers);

            assertEquals(2.2, result, 0.01,
                    "Should return 2.2 for incompatible modifiers for " + orderClass.getSimpleName());
            assertEquals(0, order.getItems().size(),
                    "Should have no items for " + orderClass.getSimpleName());
        }


         // TC13: Tests appetizer promotion - 20% discount (EP12)

        @ParameterizedTest
        @MethodSource("orderClassProvider")
        public void testAppetizerPromotion(Class<? extends Order> orderClass) throws Exception {
            order = createOrder(orderClass);
            Table table = new Table(1, 2);
            order.initOrder(table, "Test Customer");

            MenuItem salad = restaurant.getMenuItem("S001"); // APPETIZER, $8.99

            double result = order.processOrderItem(salad, null);

            assertEquals(0.20, result, 0.01,
                    "Should return 0.20 for appetizer promotion for " + orderClass.getSimpleName());
            assertEquals(7.19, order.getTotalPrice(), 0.01,
                    "Total should reflect 20% discount for " + orderClass.getSimpleName());
            assertEquals(1, order.getItems().size(),
                    "Should have 1 item for " + orderClass.getSimpleName());
        }


         // TC14: Tests ENTREE with 2+ modifiers gets 15% promotion (EP13, BVA)

        @ParameterizedTest
        @MethodSource("orderClassProvider")
        public void testEntreePromotion_TwoModifiers(Class<? extends Order> orderClass) throws Exception {
            order = createOrder(orderClass);
            Table table = new Table(1, 2);
            order.initOrder(table, "Test Customer");

            MenuItem burger = restaurant.getMenuItem("B001"); // ENTREE, $12.99
            List<String> modifiers = Arrays.asList("EXTRA_CHEESE", "NO_ONIONS");

            double result = order.processOrderItem(burger, modifiers);

            assertEquals(0.15, result, 0.01,
                    "Should return 0.15 for entree with 2+ modifiers for " + orderClass.getSimpleName());
            assertTrue(order.getTotalPrice() > 11.00 && order.getTotalPrice() < 13.00,
                    "Total should be around $11.74 with 15% discount for " + orderClass.getSimpleName());
        }


         // TC15: Tests ENTREE with 1 modifier - no promotion (EP14, BVA)

        @ParameterizedTest
        @MethodSource("orderClassProvider")
        public void testEntreeNoPromotion_OneModifier(Class<? extends Order> orderClass) throws Exception {
            order = createOrder(orderClass);
            Table table = new Table(1, 2);
            order.initOrder(table, "Test Customer");

            MenuItem burger = restaurant.getMenuItem("B001");
            List<String> modifiers = Arrays.asList("EXTRA_CHEESE");

            double result = order.processOrderItem(burger, modifiers);

            assertEquals(0.0, result, 0.01,
                    "Should return 0.0 (no promotion) for " + orderClass.getSimpleName());
            assertEquals(14.49, order.getTotalPrice(), 0.01,
                    "Total should be base + modifier for " + orderClass.getSimpleName());
        }


         // TC16: Tests order total at max boundary ($100) (EP15, BVA)

        @ParameterizedTest
        @MethodSource("orderClassProvider")
        public void testOrderTotal_AtMaxBoundary(Class<? extends Order> orderClass) throws Exception {
            order = createOrder(orderClass);
            Table table = new Table(1, 2);
            order.initOrder(table, "Test Customer");

            MenuItem testItem = new MenuItem("T001", "TestItem", 10.0, "ENTREE");
            testItem.addAllowedModifier("EXTRA_CHEESE");

            // Add items to bring total to $90
            for (int i = 0; i < 9; i++) {
                order.processOrderItem(testItem, null);
            }

            // Adding $10 item should work (total = $100)
            double result = order.processOrderItem(testItem, null);

            assertEquals(0.0, result, 0.01,
                    "Should allow item at $100 boundary for " + orderClass.getSimpleName());
            assertEquals(100.0, order.getTotalPrice(), 0.01,
                    "Total should be exactly $100 for " + orderClass.getSimpleName());
        }


         // TC17: Tests order total exceeds max ($100) (EP16, BVA)

        @ParameterizedTest
        @MethodSource("orderClassProvider")
        public void testOrderTotal_ExceedsMax(Class<? extends Order> orderClass) throws Exception {
            order = createOrder(orderClass);
            Table table = new Table(1, 2);
            order.initOrder(table, "Test Customer");

            MenuItem testItem = new MenuItem("T001", "TestItem", 10.0, "ENTREE");
            testItem.addAllowedModifier("EXTRA_CHEESE");
            MenuItem smallItem = new MenuItem("T002", "SmallItem", 1.0, "BEVERAGE");

            // Add items to bring total to $91
            for (int i = 0; i < 9; i++) {
                order.processOrderItem(testItem, null);
            }
            order.processOrderItem(smallItem, null);

            // Adding $10 item should fail (would be $101)
            double result = order.processOrderItem(testItem, null);

            assertEquals(5.1, result, 0.01,
                    "Should return 5.1 when exceeding max total for " + orderClass.getSimpleName());
            assertEquals(91.0, order.getTotalPrice(), 0.01,
                    "Total should remain at $91 for " + orderClass.getSimpleName());
        }


         // TC18: Tests valid multiple modifiers (EP5)

        @ParameterizedTest
        @MethodSource("orderClassProvider")
        public void testValidMultipleModifiers(Class<? extends Order> orderClass) throws Exception {
            order = createOrder(orderClass);
            Table table = new Table(1, 2);
            order.initOrder(table, "Test Customer");

            MenuItem burger = restaurant.getMenuItem("B001");
            List<String> modifiers = Arrays.asList("EXTRA_CHEESE", "NO_TOMATOES");

            double result = order.processOrderItem(burger, modifiers);

            assertTrue(result == 0.0 || result == 0.15,
                    "Should succeed with valid modifiers for " + orderClass.getSimpleName());
            assertTrue(order.getTotalPrice() > 11.00,
                    "Total should include modifiers for " + orderClass.getSimpleName());
            assertEquals(1, order.getItems().size(),
                    "Should have 1 item for " + orderClass.getSimpleName());
        }


         // TC19: Tests empty modifier list (EP5)

        @ParameterizedTest
        @MethodSource("orderClassProvider")
        public void testEmptyModifierList(Class<? extends Order> orderClass) throws Exception {
            order = createOrder(orderClass);
            Table table = new Table(1, 2);
            order.initOrder(table, "Test Customer");

            MenuItem burger = restaurant.getMenuItem("B001");
            List<String> modifiers = new ArrayList<>();

            double result = order.processOrderItem(burger, modifiers);

            assertEquals(0.0, result, 0.01,
                    "Should return 0.0 for empty modifier list for " + orderClass.getSimpleName());
            assertEquals(12.99, order.getTotalPrice(), 0.01,
                    "Total should be base price for " + orderClass.getSimpleName());
        }


         // TC20: Tests order status = 2 (ready, still modifiable) (EP8, BVA)

        @ParameterizedTest
        @MethodSource("orderClassProvider")
        public void testOrderStatus_StillModifiable(Class<? extends Order> orderClass) throws Exception {
            order = createOrder(orderClass);
            Table table = new Table(1, 2);
            order.initOrder(table, "Test Customer");
            order.setOrderStatus(2); // Ready, but still modifiable

            MenuItem burger = restaurant.getMenuItem("B001");
            double result = order.processOrderItem(burger, null);

            assertEquals(0.0, result, 0.01,
                    "Should allow modifications at status 2 for " + orderClass.getSimpleName());
            assertEquals(12.99, order.getTotalPrice(), 0.01,
                    "Total should update for " + orderClass.getSimpleName());
        }


         // TC21: Tests modifier price calculation (EP5)

        @ParameterizedTest
        @MethodSource("orderClassProvider")
        public void testModifierPriceCalculation(Class<? extends Order> orderClass) throws Exception {
            order = createOrder(orderClass);
            Table table = new Table(1, 2);
            order.initOrder(table, "Test Customer");

            MenuItem burger = restaurant.getMenuItem("B001");
            List<String> modifiers = Arrays.asList("EXTRA_CHEESE");

            double result = order.processOrderItem(burger, modifiers);

            assertEquals(0.0, result, 0.01,
                    "Should succeed for " + orderClass.getSimpleName());
            assertEquals(14.49, order.getTotalPrice(), 0.01,
                    "Should add $1.50 for EXTRA_CHEESE for " + orderClass.getSimpleName());
        }

         // TC22: Tests item with zero stock (EP4)
        @ParameterizedTest
        @MethodSource("orderClassProvider")
        public void testItemWithZeroStock(Class<? extends Order> orderClass) throws Exception {
            order = createOrder(orderClass);
            Table table = new Table(1, 2);
            order.initOrder(table, "Test Customer");

            MenuItem burger = restaurant.getMenuItem("B001");
            burger.setStockCount(0);

            double result = order.processOrderItem(burger, null);

            assertEquals(3.0, result, 0.01,
                    "Should return 3.0 for zero stock for " + orderClass.getSimpleName());
            assertEquals(0, order.getItems().size(),
                    "Should have no items for " + orderClass.getSimpleName());
        }


         // TC23: Tests dessert category - no promotion (EP14)

        @ParameterizedTest
        @MethodSource("orderClassProvider")
        public void testDessertNoPromotion(Class<? extends Order> orderClass) throws Exception {
            order = createOrder(orderClass);
            Table table = new Table(1, 2);
            order.initOrder(table, "Test Customer");

            MenuItem cake = restaurant.getMenuItem("C001"); // DESSERT

            double result = order.processOrderItem(cake, null);

            assertEquals(0.0, result, 0.01,
                    "Desserts should not have promotion for " + orderClass.getSimpleName());
            assertEquals(6.99, order.getTotalPrice(), 0.01,
                    "Total should be base price for " + orderClass.getSimpleName());
        }


         // TC24: Tests item with dietary flag (EP17) - Salad with GLUTEN_FREE

        @ParameterizedTest
        @MethodSource("orderClassProvider")
        public void testItemWithDietaryFlag(Class<? extends Order> orderClass) throws Exception {
            order = createOrder(orderClass);
            Table table = new Table(1, 2);
            order.initOrder(table, "Test Customer");

            MenuItem salad = restaurant.getMenuItem("S001"); // Has GLUTEN_FREE flag

            double result = order.processOrderItem(salad, null);

            assertEquals(0.20, result, 0.01,
                    "Should process item with dietary flag for " + orderClass.getSimpleName());
            assertEquals(7.19, order.getTotalPrice(), 0.01,
                    "Total should include promotion for " + orderClass.getSimpleName());
        }
    }


