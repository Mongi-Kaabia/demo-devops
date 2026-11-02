package com.beedigital.beestore;

public class BadPractice {

    // This method calculates the total price
    public double calculatePrice(double price, int quantity) {
        // Calculate the total price
        double total = price * quantity;
        return total;
    }

    // This method calculates the total amount
    public double calculateTotal(double price, int quantity) {
        // Calculate the total amount
        double total = price * quantity;
        return total;
    }

    // This method displays a message
    public void displayMessage() {
        // Print a message
        System.out.println("Hello World");
    }

    // This method displays another message
    public void showMessage() {
        // Print a message
        System.out.println("Hello World");
    }

    public void processData() {
        int unusedVariable = 100;

        // Set the value
        int value = 10;
        value = 20;

        System.out.println(value);
    }
}