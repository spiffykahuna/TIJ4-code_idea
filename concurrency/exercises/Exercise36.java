package concurrency.exercises;

import enumerated.menu.Course;
import enumerated.menu.Food;

import java.util.*;
import java.util.concurrent.*;

import static net.mindview.util.Print.print;

/**
 * Exercise 36: (10) Modify RestaurantWithQueues.java so there’s one OrderTicket
 object per table. Change order to orderTicket, and add a Table class, with multiple
 Customers per table.
 */
public class Exercise36 {

    static class Order { // (A data-transfer object)
        private static int counter = 0;
        private final int id = counter++;
        private final Customer customer;
        private final Table table;
        private final Food food;
        public Order(Customer customer, Table table, Food f) {
            this.customer = customer;
            this.table = table;
            this.food = f;
        }
        public Food item() { return food; }
        public Customer getCustomer() { return customer; }
        public Table getTable() { return table; }
        public String toString() {
            return "{ Order: " + id + " item: " + food +
                    " for customer: " + customer +
                    " served on table: " + table +
                    " served by waitPerson: " + table.getWaitPerson()
                    + "}";
        }
    }

    // This is what comes back from the chef:
    static class Plate {
        private Order order;
        private Food food;
        public Plate(Order ord, Food f) {
            order = ord;
            food = f;
        }
        public Order getOrder() { return order; }
        public Food getFood() { return food; }
        public String toString() { return food.toString(); }

        public void setOrder(Order order) {
            this.order = order;
        }

        public void setFood(Food food) {
            this.food = food;
        }
    }

    static class Customer implements Runnable {
        private static int counter = 0;
        private final int id = counter++;

        private final Table table;
        // Only one course at a time can be received:
        private SynchronousQueue<Plate> placeSetting = new SynchronousQueue<Plate>();

        public Customer(Table table) {
            this.table = table;
        }

        public void run() {
            for (Course course : Course.values()) {
                Food food = course.randomSelection();
                try {
                    WaitPerson waitPerson = table.getWaitPerson();
                    waitPerson.placeOrder(this, food, table);

                    // Blocks until course has been delivered:
                    Plate plate = table.getDeliveredPlateFor(this);
                    print(this + "eating " + plate);
                    table.getEmptyPlates().put(plate);

                } catch (InterruptedException e) {
                    print(this + "waiting for " +
                            course + " interrupted");
                    break;
                }
            }

            print(this + "finished meal, leaving");
            table.leave(this);

        }
        public String toString() {
            return "Customer " + id + " ";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Customer customer = (Customer) o;

            if (id != customer.id) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return id;
        }
    }

    static class OrderTicket {
        private static int counter = 0;
        private final int id = counter++;

        private BlockingQueue<Order> orders;
        private BlockingQueue<Plate> filledOrders;

        private final Table table;
        private final WaitPerson waitPerson;

        private int orderCount;

        OrderTicket(Table table, WaitPerson waitPerson) {
            if(null == table) throw new IllegalArgumentException("Cannot create orderTicket: Null table");
            if(null == waitPerson) throw new IllegalArgumentException("Cannot create orderTicket: Null waitPerson");

            this.table = table;
            this.waitPerson = waitPerson;
        }

        public BlockingQueue<Order> getOrders() {
            if (null == orders)
                orders = new LinkedBlockingQueue<Order>();
            return orders;
        }


        public synchronized void addOrder(Order order) throws InterruptedException {
            getOrders().put(order);
            orderCount++;
        }

        public WaitPerson getWaitPerson() {
            return waitPerson;
        }


        public BlockingQueue<Plate> getFilledOrders() {
            if(null == filledOrders)
                filledOrders = new LinkedBlockingQueue<Plate>();;
            return filledOrders;
        }


        public synchronized boolean isCompleted() {
            return orderCount == getFilledOrders().size();
        }

        public synchronized Table getTable() {
            return table;
        }

        @Override
        public String toString() {
            return "OrderTicket{" +
                    "id=" + id +
                    '}';
        }

        public synchronized Order takeWaitingOrder() throws InterruptedException {
            return getOrders().take();
        }

        public synchronized void putCompletedPlate(Plate plate) throws InterruptedException {
            getFilledOrders().put(plate);
        }

        public synchronized boolean canBeSubmitted() {
            return getOrders().size() >= table.getCustomers().size();
        }
    }

    static class Table implements Comparable<Table> {
        private final int seatsCount;
        private List<Customer> customers;

        private OrderTicket orderTicket;

        private WaitPerson waitPerson;

        private boolean isFree;
        private BlockingQueue<Plate> emptyPlates;
        private BlockingQueue<Plate> deliveredPlates;

        public Table(int seatsCount) {
            this.seatsCount = seatsCount;
            this.isFree = false;
            this.deliveredPlates = new ArrayBlockingQueue<Plate>(seatsCount*2);
        }

        public synchronized boolean isFree() {
            return isFree;
        }

        public synchronized void setFree(boolean isFree) {
            this.isFree = isFree;
        }

        public int getSeatsCount() {
            return seatsCount;
        }

        public synchronized List<Customer> getCustomers() {
            if(null == customers)
                customers = new ArrayList<Customer>(seatsCount);
            return customers;
        }

        public synchronized void setCustomers(List<Customer> customers) {
            this.customers = customers;
        }

        public synchronized boolean addCustomer(Customer customer) {
            return getCustomers().add(customer);
        }

        public synchronized int getFreeSeats() {
            return getSeatsCount() - getCustomers().size();
        }

        @Override
        public int compareTo(Table table) {
            int bySeatsCount = getSeatsCount() < table.getSeatsCount() ? -1 :
                    (getSeatsCount() == table.getSeatsCount() ? 0 : 1);

            if(bySeatsCount != 0) return bySeatsCount;

            return getCustomers().size() < table.getCustomers().size() ? -1:
                    (getCustomers().size() == table.getCustomers().size() ? 0 : 1);
        }

        public synchronized WaitPerson getWaitPerson() {
            return waitPerson;
        }

        public synchronized void setWaitPerson(WaitPerson waitPerson) {
            this.waitPerson = waitPerson;
        }

        public synchronized  OrderTicket getOrderTicket() {
            return orderTicket;
        }

        public synchronized void setOrderTicket(OrderTicket orderTicket) {
            this.orderTicket = orderTicket;
        }

        public synchronized BlockingQueue<Plate> getEmptyPlates() {
            if(null == emptyPlates)
                emptyPlates = new ArrayBlockingQueue<Plate>(getSeatsCount());
            return emptyPlates;
        }

        public synchronized void leave(Customer customer) {
            if(getCustomers().contains(customer)) {
                getCustomers().remove(customer);
            }
        }

        private static int counter = 0;
        private final int id = counter++;

        @Override
        public String toString() {
            return "Table{" +
                    "id=" + id +
                    '}';
        }

        public synchronized boolean hasNoCustomers() {
            return getCustomers().size() == 0;
        }

        public synchronized boolean hasCustomer(Customer customer) {
            return getCustomers().contains(customer);
        }

        public void deliver(Plate plate) throws InterruptedException {
            deliveredPlates.put(plate);
        }

        public Plate getDeliveredPlateFor(Customer customer) throws InterruptedException {
            Plate customerPlate = null;

            while(customerPlate == null) {
                Plate deliveredPlate =  deliveredPlates.take();;
                if(customer.equals(deliveredPlate.getOrder().getCustomer())) {
                    customerPlate = deliveredPlate;
                } else {
                    deliveredPlates.put(deliveredPlate);
                }
            }

            return customerPlate;
        }
    }

    static class WaitPerson implements Runnable {
        private static int counter = 0;
        private final int id = counter++;
        private final Restaurant restaurant;
        BlockingQueue<Plate> filledOrders = new LinkedBlockingQueue<Plate>();

        private int maxOrderCount;

        private static Random rand = new Random(47);

        public WaitPerson(Restaurant rest) { restaurant = rest; }

        public synchronized void placeOrder(Customer cust, Food food, Table table) {
            try {
                OrderTicket orderTicket = table.getOrderTicket();
                if(null == orderTicket) {
                    orderTicket = new OrderTicket(table, this);
                    table.setOrderTicket(orderTicket);
                }

                Order order = new Order(cust, table, food);
                print("Adding new " + order + " to " + orderTicket);
                orderTicket.addOrder(order);

                if(orderTicket.canBeSubmitted()) {
                    print("Submitting new " + orderTicket + " for " + table + " with " + orderTicket.getOrders().size() + " orders");
                    submitOrderTicket(table, orderTicket);
                }

            } catch(InterruptedException e) {
                print(this + " placeOrder interrupted");
            }
        }

        private void submitOrderTicket(Table table, OrderTicket orderTicket) throws InterruptedException {
            restaurant.orderTickets.put(orderTicket);
            table.setOrderTicket(new OrderTicket(table, this));
        }

        public void run() {
            try {
                while (!Thread.interrupted()) {
                    // Blocks until a course is ready
                    deliverCompletedOrders();

                    submitNewOrders();

                    collectEmptyPlates();
                    cleanEmptyTables();

                    print(this + " having a small rest");
                    TimeUnit.MILLISECONDS.sleep(rand.nextInt(5000));
                }
            } catch (InterruptedException e) {
                print(this + " interrupted");
            }
            print(this + " off duty");
        }

        private void submitNewOrders() throws InterruptedException {
            for(Table table: restaurant.tables) {
                if(this.equals(table.getWaitPerson())
                        && table.getOrderTicket() != null
                        && table.getOrderTicket().canBeSubmitted()) {

                    submitOrderTicket(table, table.getOrderTicket());
                }
            }
        }

        private void deliverCompletedOrders() throws InterruptedException {
            OrderTicket orderTicket = pickupCompletedOrderTicket();
            if (null != orderTicket) {
                print(this + "received " + orderTicket +
                        " delivering to " +
                        orderTicket.getTable());

                Table table = orderTicket.getTable();
                while(orderTicket.filledOrders.size() > 0) {
                    Plate plate = orderTicket.filledOrders.take();

                    if(!table.hasCustomer(plate.getOrder().getCustomer()))
                        throw new IllegalStateException(table + " has no " + plate.getOrder().getCustomer());

                    print(this + " is delivering" + plate + " to " + table);
                    table.deliver(plate);
                }
            }
        }

        private void cleanEmptyTables() throws InterruptedException {
            for(Table table: restaurant.tables) {
                if(this.equals(table.getWaitPerson())
                        && table.hasNoCustomers()
                        && !table.isFree()) {
                    TimeUnit.MILLISECONDS.sleep(rand.nextInt(500));
                    print(this + "cleaning empty " + table);

                    for(Plate emptyPlate: collectEmptyPlatesFromTable(table)) {
                        restaurant.emptyPlates.put(emptyPlate);
                    }
                    table.setFree(true);
                }
            }
        }

        private void collectEmptyPlates() throws InterruptedException {

            Collection<Plate> emptyPlates = new ArrayList<Plate>();
            for(Table table: restaurant.tables) {
                if(this.equals(table.getWaitPerson())
                        && table.getEmptyPlates().size() > 0) {
                    emptyPlates = collectEmptyPlatesFromTable(table);
                }
            }
            for(Plate emptyPlate: emptyPlates) {
                restaurant.emptyPlates.put(emptyPlate);
            }

            TimeUnit.MILLISECONDS.sleep(rand.nextInt(500));
        }

        private Collection<Plate> collectEmptyPlatesFromTable(Table table) throws InterruptedException {
            List<Plate> emptyPlates = new ArrayList<Plate>();
            print(this + "collects empty plates on " + table);
            while(table.getEmptyPlates().size() > 0) {
                emptyPlates.add(table.getEmptyPlates().take());
                TimeUnit.MILLISECONDS.sleep(rand.nextInt(500));
            }
            return emptyPlates;
        }

        private OrderTicket pickupCompletedOrderTicket() throws InterruptedException {
            OrderTicket completetOrderTicket = null;
            for(OrderTicket orderTicket : restaurant.completedOrderTickets) {
                if(this.equals(orderTicket.getWaitPerson())) {
                    completetOrderTicket = orderTicket;
                    restaurant.completedOrderTickets.remove(orderTicket);
                    return completetOrderTicket;
                }
            }
            print(this + "has not found its orderTicket. Waiting for some time");
            completetOrderTicket = restaurant.completedOrderTickets.poll(10, TimeUnit.MILLISECONDS);

            if(null == completetOrderTicket){
                print(this + " still nothing. Lets do something else");
                return null;
            }

            if(this.equals(completetOrderTicket.getWaitPerson())) {
                return completetOrderTicket;
            } else {

                restaurant.completedOrderTickets.put(completetOrderTicket);
                return null;
            }
        }

        public String toString() {
            return "WaitPerson " + id + " ";
        }

        public Table findTableForCompany(int companySize) throws InterruptedException {
            Table tableForNewCompany = null;

            while(tableForNewCompany == null) {
                for(Table table: restaurant.tables) {
                    if(table.isFree() && table.getSeatsCount() >= companySize) {
                        tableForNewCompany = table;
                        break;
                    }
                }

                System.out.format("No suitable  table found for %d people. Waiting while one of tables becomes available%n",
                        companySize
                );
                TimeUnit.MILLISECONDS.sleep(1000);
            }

            print(tableForNewCompany + " became available");
            return tableForNewCompany;
        }

        public int getMaxOrderCount() {
            return restaurant.getMaxOrderCount();
        }
    }

    static class Chef implements Runnable {
        private static int counter = 0;
        private final int id = counter++;
        private final Restaurant restaurant;
        private static Random rand = new Random(47);
        public Chef(Restaurant rest) { restaurant = rest; }

        public void run() {
            try {
                while (!Thread.interrupted()) {
                    // Blocks until an orderTicket appears:
                    OrderTicket orderTicket = restaurant.orderTickets.take();
                    print(this + " takes " + orderTicket);
                    if (orderTicket.getOrders().size() > 0) {
                        Order order = orderTicket.takeWaitingOrder();

                        print(this + " is preparing " + order);
                        Food requestedItem = order.item();
                        // Time to prepare order:
                        TimeUnit.MILLISECONDS.sleep(rand.nextInt(500));

                        Plate plate = restaurant.getFreePlate();

                        plate.setOrder(order);
                        plate.setFood(requestedItem);

                        orderTicket.putCompletedPlate(plate);
                    }

                    if (orderTicket.isCompleted()) {
                        print(orderTicket + "is completed by " + this);
                        restaurant.completedOrderTickets.put(orderTicket);
                    } else {
                        restaurant.orderTickets.put(orderTicket);
                    }

                    if(restaurant.orderTickets.size() < 1) {
                        print(this + " is having a rest");
                        TimeUnit.MILLISECONDS.sleep(rand.nextInt(1000));
                    }
                }
            } catch (InterruptedException e) {
                print(this + " interrupted");
            }
            print(this + " off duty");
        }
        public String toString() { return "Chef " + id + " "; }
    }

    static class Restaurant implements Runnable {
        private static final int MAX_TABLE_SEATS_COUNT = 10;
        /* max 2 orders for each customer */
        private static final int MAX_ORDER_COUNT = MAX_TABLE_SEATS_COUNT * 2;

        private BlockingQueue<Table> tables = new PriorityBlockingQueue<Table>();
        private List<WaitPerson> waitPersons = new ArrayList<WaitPerson>();

        private List<Chef> chefs = new ArrayList<Chef>();

        private ExecutorService exec;
        private static Random rand = new Random(47);

        BlockingQueue<OrderTicket> orderTickets = new LinkedBlockingQueue<OrderTicket>();
        BlockingQueue<OrderTicket> completedOrderTickets = new LinkedBlockingQueue<OrderTicket>();

        public BlockingQueue<Plate> emptyPlates;


        public Restaurant(ExecutorService e,
                          int nWaitPersons,
                          int nChefs,
                          int tableCount
        ) {
            exec = e;
            for(int i = 0; i < nWaitPersons; i++) {
                WaitPerson waitPerson = new WaitPerson(this);
                waitPersons.add(waitPerson);
                exec.execute(waitPerson);
            }
            for(int i = 0; i < nChefs; i++) {
                Chef chef = new Chef(this);
                chefs.add(chef);
                exec.execute(chef);
            }

            int totalSeats = 0;
            for(int i = 0; i < tableCount; i++) {
                int seats = MAX_TABLE_SEATS_COUNT;
                totalSeats += seats;
                Table table = new Table(seats);
                table.setFree(true);
                tables.offer(table);
            }


            int plateCountTotal = totalSeats*3;
            emptyPlates = new ArrayBlockingQueue<Plate>(plateCountTotal);
            for(int i = 0; i < totalSeats*3; i++) {
                emptyPlates.add(new Plate(null, null));
            }

            System.out.format("Opening new restaurant: waiters=%d, chefs=%d, tables=%s, seats_total=%s, platesTotal=%d%n",
                nWaitPersons,
                nChefs,
                tableCount,
                totalSeats,
                plateCountTotal
            );
        }

        public void run() {
            try {
                while(!Thread.interrupted()) {
                    // A new customer arrives; assign a WaitPerson:
                    WaitPerson waitPerson = waitPersons.get(
                            rand.nextInt(waitPersons.size()));

                    int companySize = getRandomCompanySize();
                    print("<< new company of " + companySize + " people arrived. Assigning a waitperson=" + waitPerson + " >>");

                    Table tableForNewCompany = waitPerson.findTableForCompany(companySize);
                    tableForNewCompany.setWaitPerson(waitPerson);
                    tableForNewCompany.setFree(false);

                    for(int i = 0; i < companySize; i++) {
                        Customer c = new Customer(tableForNewCompany);
                        tableForNewCompany.addCustomer(c);
                        exec.execute(c);
                    }

                    TimeUnit.MILLISECONDS.sleep(2000);
                }
            } catch(InterruptedException e) {
                print("Restaurant interrupted");
            }
            print("Restaurant closing");
        }

        private int getRandomCompanySize() {
            int companySize = rand.nextInt(MAX_TABLE_SEATS_COUNT);

            while((companySize < 1)) {
                companySize = rand.nextInt(MAX_TABLE_SEATS_COUNT);
            }
            return companySize;
        }

        public int getMaxOrderCount() {
            return MAX_ORDER_COUNT;
        }

        public synchronized Plate getFreePlate() {
            Plate plate = null;
            try {
                plate = emptyPlates.poll(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException("There is no more empty plates", e);
            }
            return plate;
        }
    }

    public static class RestaurantWithQueuesAndTables {
        public static void main(String[] args) throws Exception {
            ExecutorService exec = Executors.newCachedThreadPool();
            Restaurant restaurant = new Restaurant(exec, 5, 2, 10);
            exec.execute(restaurant);
            if (args.length > 0) // Optional argument
                TimeUnit.SECONDS.sleep(new Integer(args[0]));
            else {
                print("Press 'Enter' to quit");
                System.in.read();
            }
            exec.shutdownNow();
        }
    }

    public static void main(String[] args) throws Exception {
        RestaurantWithQueuesAndTables.main(args);
    }
}
