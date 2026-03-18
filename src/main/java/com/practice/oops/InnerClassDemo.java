package com.practice.oops;

/**
 * Demonstrates the differences between Inner classes and Static Inner classes in Java.
 */
public class InnerClassDemo {
    private String instanceMessage = "Hello from Outer Instance Member!";
    private static String staticMessage = "Hello from Outer Static Member!";

    // 1. Non-static Inner Class (Member Inner Class)
    // - Requires an instance of the Outer class to exist.
    // - Can access both static and instance members of the Outer class.
    public class NonStaticInner {
        public void display() {
            System.out.println("--- Non-Static Inner Class ---");
            System.out.println("Accessing Outer static message: " + staticMessage);
            System.out.println("Accessing Outer instance message: " + instanceMessage);
        }
    }

    // 2. Static Inner Class
    // - Can be instantiated without an instance of the Outer class.
    // - Can ONLY access static members of the Outer class.
    public static class StaticInner {
        public void display() {
            System.out.println("--- Static Inner Class ---");
            System.out.println("Accessing Outer static message: " + staticMessage);
            // System.out.println("Accessing Outer instance message: " + instanceMessage); // ERROR: Cannot access non-static member
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Inner Classes Demo ===");

        // --- Usage of Static Inner Class ---
        // Instantiated directly using ClassName.StaticInner
        StaticInner staticInner = new StaticInner();
        staticInner.display();

        System.out.println();

        // --- Usage of Non-static Inner Class ---
        // Requires an instance of the Outer class
        InnerClassDemo outer = new InnerClassDemo();
        NonStaticInner nonStaticInner = outer.new NonStaticInner();
        nonStaticInner.display();

        System.out.println("\n--- Key Differences Summary ---");
        System.out.println("1. Memory: Non-static inner class keeps a reference to the outer class instance, " +
                           "which can lead to memory leaks if not handled correctly.");
        System.out.println("2. Access: Static inner class cannot access non-static members of the Outer class.");
        System.out.println("3. Instantiation: Static inner class doesn't need an Outer instance; Non-static inner does.");
    }
}
