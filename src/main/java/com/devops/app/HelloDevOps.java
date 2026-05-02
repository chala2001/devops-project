package com.devops.app;

/**
 * Main application class for our DevOps demo project.
 * This simple app demonstrates a buildable, testable Java application
 * that we'll deploy through a CI/CD pipeline.
 */
public class HelloDevOps {

    /**
     * Returns a greeting message.
     */
    public String getGreeting() {
        return "Hello from DevOps Pipeline!";
    }

    /**
     * Returns application version info.
     */
    public String getVersion() {
        return "1.0-SNAPSHOT";
    }

    /**
     * Performs a simple calculation (to show testing).
     */
    public int add(int a, int b) {
        return a + b;
    }

    /**
     * Entry point of the application.
     */
    public static void main(String[] args) {
        HelloDevOps app = new HelloDevOps();
        
        System.out.println("================================");
        System.out.println("  " + app.getGreeting());
        System.out.println("  Version: " + app.getVersion());
        System.out.println("  2 + 3 = " + app.add(2, 3));
        System.out.println("================================");
        System.out.println("  Build successful!");
        System.out.println("  Deployed via Jenkins CI/CD");
        System.out.println("================================");
    }
}
