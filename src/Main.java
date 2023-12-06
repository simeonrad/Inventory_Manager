import java.util.*;

public class Main {
    public static void main(String[] args) {
        InventoryManager manager = new InventoryManager();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String[] command = scanner.nextLine().split(" ");
            if (command[0].equals("end")) {
                break;
            }

            switch (command[0]) {
                case "add":
                    System.out.println(manager.addItem(command[1], Double.parseDouble(command[2]), command[3]));
                    break;
                case "filter":
                    if (command[2].equals("type")) {
                        System.out.println(manager.filterByType(command[3]));
                    } else if (command[2].equals("price")) {
                        if (command.length == 7) {
                            System.out.println(manager.filterByPrice(Double.parseDouble(command[4]), Double.parseDouble(command[6])));
                        } else if (command[3].equals("from")) {
                            System.out.println(manager.filterByPrice(Double.parseDouble(command[4]), null));
                        } else if (command[3].equals("to")) {
                            System.out.println(manager.filterByPrice(0, Double.parseDouble(command[4])));
                        }
                    }
                    break;
            }
        }
    }
}

class InventoryManager {

    private final Map<String, Item> inventory;

    public InventoryManager() {
        this.inventory = new HashMap<>();
    }

    public String addItem(String name, double price, String itemType) {
        if (inventory.containsKey(name)) {
            return "Error: Item " + name + " already exists";
        } else {
            inventory.put(name, new Item(name, price, itemType));
            return "Ok: Item " + name + " added successfully";
        }
    }

    public String filterByType(String itemType) {
        List<Item> filteredItems = new ArrayList<>();
        for (Item item : inventory.values()) {
            if (item.getItemType().equals(itemType)) {
                filteredItems.add(item);
            }
        }

        filteredItems.sort(Comparator.comparing(Item::getPrice).thenComparing(Item::getName)
                .thenComparing(Item::getItemType));

        if (filteredItems.isEmpty()) {
            return String.format("Error: Type %s does not exist", itemType);
        } else {
            return "Ok: " + getFormattedItemList(filteredItems);
        }
    }

    public String filterByPrice(double minPrice, Double maxPrice) {
        List<Item> filteredItems = new ArrayList<>();
        for (Item item : inventory.values()) {
            if ((minPrice <= item.getPrice()) && (maxPrice == null || item.getPrice() <= maxPrice)) {
                filteredItems.add(item);
            }
        }

        filteredItems.sort(Comparator.comparing(Item::getPrice).thenComparing(Item::getName)
                .thenComparing(Item::getItemType));

        if (filteredItems.isEmpty()) {
            return "Ok: ";
        } else {
            return "Ok: " + getFormattedItemList(filteredItems);
        }
    }

    private String getFormattedItemList(List<Item> items) {
        StringBuilder result = new StringBuilder();
        for (Item item : items.subList(0, Math.min(items.size(), 10))) {
            result.append(item.getName()).append("(").append(String.format("%.2f", item.getPrice())).append("), ");
        }
        return result.substring(0, Math.max(0, result.length() - 2));
    }
}

class Item {
    private final String name;
    private final double price;
    private final String itemType;

    public Item(String name, double price, String itemType) {
        this.name = name;
        this.price = price;
        this.itemType = itemType;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getItemType() {
        return itemType;
    }
}