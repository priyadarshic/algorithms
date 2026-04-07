package com.practice.concurrency.locking;

/**
 * Main class to demonstrate various Java Lock mechanisms.
 * This class coordinates the execution of all individual lock demonstrations.
 */
public class LocksMain {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   JAVA CONCURRENCY: LOCKS DEMONSTRATION         ");
        System.out.println("=================================================");

        // 1. ReentrantLock Demo
        new ReentrantLockDemo().runDemo();

        // 2. ReadWriteLock Demo
        new ReadWriteLockDemo().runDemo();

        // 3. StampedLock Demo
        new StampedLockDemo().runDemo();

        // 4. Condition Demo
        new ConditionDemo().runDemo();

        System.out.println("=================================================");
        System.out.println("   DEMONSTRATION COMPLETED                       ");
        System.out.println("=================================================");
    }
}
