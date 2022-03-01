package com.company;

public class Main {

    public static void main(String[] args) {

        ItemDAO itemDAO = new ItemDAO();

        Item item = new Item(3, "Spurga", "Kepyklele", 1.05, 3);
        Item item2 = new Item(2, "Varztai", "Gamykla", 11, 1);
        // itemDAO.insert(item);

        Item item3 = new Item(2, "Kompiuteris", "IT", 2000, 1);
        // itemDAO.update(item3);

        // itemDAO.searchByDescription("Spurga");

        // itemDAO.deleteById(1);

        itemDAO.searchByQuantityGreaterThan(0);
    }
}
