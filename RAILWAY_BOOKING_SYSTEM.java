package BU_PROJECT;
import java.util.*;

public class RAILWAY_BOOKING_SYSTEM{

    static Scanner scanner = new Scanner(System.in);

    // Mock databases
    static Map<String, String> users = new HashMap<>(); // username -> password
    static List<Train> trains = new ArrayList<>();
    static Map<String, List<Ticket>> bookings = new HashMap<>(); // username -> tickets

    public static void main(String[] args) {
        // Seed data
        seedTrains();

        System.out.println("Welcome to Railway Booking System");
        while (true) {
            System.out.println("1. Login\n2. Register\n3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    static void seedTrains() {
        trains.add(new Train(101, "Express A", "City A", "City B", "10:00 AM", "2:00 PM", 100));
        trains.add(new Train(102, "Express B", "City B", "City C", "3:00 PM", "7:00 PM", 50));
        trains.add(new Train(103, "Express C", "City C", "City A", "8:00 PM", "12:00 AM", 75));
    }

    static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (users.containsKey(username) && users.get(username).equals(password)) {
            System.out.println("Login successful!");
            userMenu(username);
        } else {
            System.out.println("Invalid credentials. Try again.");
        }
    }

    static void register() {
        System.out.print("Enter a username: ");
        String username = scanner.nextLine();
        if (users.containsKey(username)) {
            System.out.println("Username already exists. Try again.");
            return;
        }

        System.out.print("Enter a password: ");
        String password = scanner.nextLine();
        users.put(username, password);
        System.out.println("Registration successful! You can now log in.");
    }

    static void userMenu(String username) {
        while (true) {
            System.out.println("1. Book Ticket\n2. Check Seat Availability\n3. Train Schedule\n4. Cancel Ticket\n5. View Bookings\n6. Logout");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    bookTicket(username);
                    break;
                case 2:
                    checkSeatAvailability();
                    break;
                case 3:
                    viewTrainSchedule();
                    break;
                case 4:
                    cancelTicket(username);
                    break;
                case 5:
                    viewBookings(username);
                    break;
                case 6:
                    System.out.println("Logged out.");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    static void bookTicket(String username) {
        System.out.println("Available trains:");
        for (Train train : trains) {
            System.out.println(train);
        }
        System.out.print("Enter train ID to book: ");
        int trainId = scanner.nextInt();
        scanner.nextLine();

        Train selectedTrain = null;
        for (Train train : trains) {
            if (train.id == trainId) {
                selectedTrain = train;
                break;
            }
        }

        if (selectedTrain == null) {
            System.out.println("Invalid train ID.");
            return;
        }

        if (selectedTrain.availableSeats > 0) {
            System.out.print("Enter number of tickets to book: ");
            int tickets = scanner.nextInt();
            scanner.nextLine();

            if (tickets <= selectedTrain.availableSeats) {
                selectedTrain.availableSeats -= tickets;
                bookings.computeIfAbsent(username, k -> new ArrayList<>())
                        .add(new Ticket(selectedTrain, tickets));
                System.out.println("Booking successful!");
            } else {
                System.out.println("Not enough seats available.");
            }
        } else {
            System.out.println("No seats available.");
        }
    }

    static void checkSeatAvailability() {
        System.out.println("Train ID | Train Name | Available Seats");
        for (Train train : trains) {
            System.out.printf("%d | %s | %d\n", train.id, train.name, train.availableSeats);
        }
    }

    static void viewTrainSchedule() {
        System.out.println("Train ID | Train Name | From | To | Departure | Arrival");
        for (Train train : trains) {
            System.out.printf("%d | %s | %s | %s | %s | %s\n",
                    train.id, train.name, train.from, train.to, train.departure, train.arrival);
        }
    }

    static void cancelTicket(String username) {
        List<Ticket> userBookings = bookings.get(username);
        if (userBookings == null || userBookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        System.out.println("Your bookings:");
        for (int i = 0; i < userBookings.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, userBookings.get(i));
        }

        System.out.print("Enter booking number to cancel: ");
        int bookingNumber = scanner.nextInt();
        scanner.nextLine();

        if (bookingNumber < 1 || bookingNumber > userBookings.size()) {
            System.out.println("Invalid booking number.");
            return;
        }

        Ticket ticket = userBookings.remove(bookingNumber - 1);
        ticket.train.availableSeats += ticket.seats;
        System.out.println("Booking cancelled successfully.");
    }

    static void viewBookings(String username) {
        List<Ticket> userBookings = bookings.get(username);
        if (userBookings == null || userBookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        System.out.println("Your bookings:");
        for (Ticket ticket : userBookings) {
            System.out.println(ticket);
        }
    }

    static class Train {
        int id;
        String name;
        String from;
        String to;
        String departure;
        String arrival;
        int availableSeats;

        Train(int id, String name, String from, String to, String departure, String arrival, int availableSeats) {
            this.id = id;
            this.name = name;
            this.from = from;
            this.to = to;
            this.departure = departure;
            this.arrival = arrival;
            this.availableSeats = availableSeats;
        }

        @Override
        public String toString() {
            return id + " - " + name + " (" + from + " to " + to + ")";
        }
    }

    static class Ticket {
        Train train;
        int seats;

        Ticket(Train train, int seats) {
            this.train = train;
            this.seats = seats;
        }

        @Override
        public String toString() {
            return seats + " seats on " + train;
        }
    }
}

