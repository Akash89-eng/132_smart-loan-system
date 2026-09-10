package com.loan;

public class Customer {
    private final String customerId;
    private final String name;
    private final int age;
    private final String govtId;
    private final double monthlyIncome;
    private final double existingMonthlyDebt;
    private final int creditScore;

    public Customer(String customerId, String name, int age, String govtId,
                    double monthlyIncome, double existingMonthlyDebt, int creditScore)
            throws InvalidApplicationException {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new InvalidApplicationException("Customer ID cannot be empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidApplicationException("Customer name cannot be empty.");
        }
        if (age <= 0 || age > 120) {
            throw new InvalidApplicationException("Age must be realistic and positive.");
        }
        if (govtId == null || govtId.trim().isEmpty()) {
            throw new InvalidApplicationException("Government ID cannot be empty.");
        }
        if (monthlyIncome < 0 || existingMonthlyDebt < 0) {
            throw new InvalidApplicationException("Income and debt values cannot be negative.");
        }
        if (creditScore < 300 || creditScore > 850) {
            throw new InvalidApplicationException("Credit score must be between 300 and 850.");
        }

        this.customerId = customerId;
        this.name = name;
        this.age = age;
        this.govtId = govtId;
        this.monthlyIncome = monthlyIncome;
        this.existingMonthlyDebt = existingMonthlyDebt;
        this.creditScore = creditScore;
    }

    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGovtId() { return govtId; }
    public double getMonthlyIncome() { return monthlyIncome; }
    public double getExistingMonthlyDebt() { return existingMonthlyDebt; }
    public int getCreditScore() { return creditScore; }
}
