
import java.util.ArrayList;
import java.util.List;

// Interface for passengers
interface Passenger {
    String getName();
    int getPassengerNumber();
}

// Enum for passenger types
enum PassengerType {
    STANDARD, GOLD, PREMIUM
}

// Passenger implementation
class TravelPassenger implements Passenger {
    private String name;
    private int passengerNumber;
    private PassengerType type;
    private double balance;
    private List<Activity> signedActivities;

    public TravelPassenger(String name, int passengerNumber, PassengerType type) {
        this.name = name;
        this.passengerNumber = passengerNumber;
        this.type = type;
        this.balance = 0.0;
        this.signedActivities = new ArrayList<>();
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public int getPassengerNumber() {
        return passengerNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public PassengerType getType() {
        return type;
    }

    public List<Activity> getSignedActivities() {
        return signedActivities;
    }
    
    public void addSignedActivity(Activity activity) {
        // Check if the destination of the activity is in the list of destinations for the travel package
        boolean isValidDestination = false;
        for (Destination destination : activity.getDestination()) {
            if (packageDestinations.contains(destination)) {
                isValidDestination = true;
                break;
            }
        }

        // If the destination is valid and the passenger has enough balance (for standard and gold passengers), add the activity
        if (isValidDestination) {
            if (type == PassengerType.PREMIUM || (type == PassengerType.GOLD && balance >= activity.getCost() * 0.9) ||
                (type == PassengerType.STANDARD && balance >= activity.getCost())) {
                
                // Check if the activity has space available
                if (activity.getCapacity() > 0) {
                    // Decrement the activity's capacity
                    activity.decreaseCapacity();

                    signedActivities.add(activity);
                    if (type != PassengerType.PREMIUM) {
                        // Deduct the cost from balance for standard and gold passengers
                        double costToPay = (type == PassengerType.GOLD) ? activity.getCost() * 0.9 : activity.getCost();
                        balance -= costToPay;
                    }
                    System.out.println("Activity " + activity.getName() + " added successfully.");
                } else {
                    System.out.println("Activity " + activity.getName() + " is already full.");
                }
            } else {
                System.out.println("Insufficient balance to add activity " + activity.getName());
            }
        } else {
            System.out.println("Invalid destination for activity " + activity.getName());
        }
    }
}

// Activity class
class Activity {
    private String name;
    private String description;
    private double cost;
    private int capacity;
    private Destination destination;

    public Activity(String name, String description, double cost, int capacity) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.capacity = capacity;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getCost() {
        return cost;
    }

    public int getCapacity() {
        return capacity;
    }

    // Method to check if activity has space available
    public void decreaseCapacity() {
        capacity--;
    }
    
    public Destination getDestination() {
        return destination;
    }

    // Method to check if activity is associated with a destination
    public boolean isAssociatedWithDestination() {
        return destination != null;
    }

    // Method to associate activity with a destination
    public void associateWithDestination(Destination destination) {
        this.destination = destination;
    }
}

// Destination class
class Destination {
    private String name;
    private List<Activity> activities;

    public Destination(String name) {
        this.name = name;
        this.activities = new ArrayList<>();
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void addActivity(Activity activity) {
        // Check if the activity is already associated with another destination
        if (!activity.isAssociatedWithDestination()) {
            activities.add(activity);
            activity.associateWithDestination(this);
        } else {
            System.out.println("Activity " + activity.getName() + " is already associated with another destination.");
        }
    }
}

// Travel Package class
class TravelPackage {
    private String name;
    private int passengerCapacity;
    private List<Destination> destinations;
    private List<TravelPassenger> passengers;

    public TravelPackage(String name, int passengerCapacity) {
        this.name = name;
        this.passengerCapacity = passengerCapacity;
        this.destinations = new ArrayList<>();
        this.passengers = new ArrayList<>();
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    public List<Destination> getDestinations() {
        return destinations;
    }

    public List<TravelPassenger> getPassengers() {
        return passengers;
    }

    public void addDestination(Destination destination) {
        destinations.add(destination);
    }

    public void addPassenger(TravelPassenger passenger) {
        if (passengers.size() < passengerCapacity) {
            passengers.add(passenger);
        } else {
            System.out.println("Passenger capacity reached. Cannot add more passengers.");
        }
    }

    // Method to print itinerary
    public void printItinerary() {
        System.out.println("Travel Package: " + name);
        for (Destination destination : destinations) {
            System.out.println("Destination: " + destination.getName());
            for (Activity activity : destination.getActivities()) {
                System.out.println("Activity: " + activity.getName() + ", Description: " + activity.getDescription() +
                        ", Cost: " + activity.getCost() + ", Capacity: " + activity.getCapacity());
            }
        }
    }

    // Method to print passenger list
    public void printPassengerList() {
        System.out.println("Travel Package: " + name);
        System.out.println("Passenger Capacity: " + passengerCapacity);
        System.out.println("Number of Passengers Enrolled: " + passengers.size());
        for (TravelPassenger passenger : passengers) {
            System.out.println("Passenger Name: " + passenger.getName() + ", Passenger Number: " + passenger.getPassengerNumber());
        }
    }

    // Method to print details of an individual passenger
    public void printPassengerDetails(TravelPassenger passenger) {
        System.out.println("Passenger Name: " + passenger.getName());
        System.out.println("Passenger Number: " + passenger.getPassengerNumber());
        System.out.println("Passenger Type: " + passenger.getType());
        if (passenger.getType() != PassengerType.PREMIUM) {
            System.out.println("Balance: " + passenger.getBalance());
        }
        System.out.println("Signed Activities:");
        for (Activity activity : passenger.getSignedActivities()) {
            System.out.println("Activity: " + activity.getName() + ", Destination: " + activity.getDestination().getName() +
                    ", Price Paid: " + calculatePrice(passenger, activity));
        }
    }

    // Method to calculate price for a passenger based on type and activity
    private double calculatePrice(TravelPassenger passenger, Activity activity) {
        double price = activity.getCost();
        if (passenger.getType() == PassengerType.GOLD) {
            price *= 0.9; // 10% discount for gold passengers
        }
        return price;
    }

    // Method to print details of activities with available space
    public void printAvailableActivities() {
        System.out.println("Activities with Available Space:");
        for (Destination destination : destinations) {
            for (Activity activity : destination.getActivities()) {
                if (activity.hasSpaceAvailable()) {
                    System.out.println("Activity: " + activity.getName() + ", Destination: " + destination.getName() +
                            ", Available Spaces: " + activity.getCapacity());
                }
            }
        }
    }
}

// Unit tests for the methods
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class TravelPackageTest {

    @Test
    public void testPrintItinerary() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        TravelPackage travelPackage = new TravelPackage("Europe Trip", 50);
        Destination destination = new Destination("Paris");
        Activity activity = new Activity("Sightseeing", "City tour", 50.0, 20);
        destination.addActivity(activity);
        travelPackage.addDestination(destination);
        travelPackage.printItinerary();

        assertEquals("Travel Package: Europe Trip\nDestination: Paris\nActivity: Sightseeing, Description: City tour, Cost: 50.0, Capacity: 20\n", outContent.toString());
    }

    @Test
    public void testPrintPassengerList() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        TravelPackage travelPackage = new TravelPackage("Europe Trip", 50);
        TravelPassenger passenger = new TravelPassenger("John", 1, PassengerType.STANDARD);
        travelPackage.addPassenger(passenger);
        travelPackage.printPassengerList();

        assertEquals("Travel Package: Europe Trip\nPassenger Capacity: 50\nNumber of Passengers Enrolled: 1\nPassenger Name: John, Passenger Number: 1\n", outContent.toString());
    }

   
}
