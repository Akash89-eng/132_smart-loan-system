package com.loan;

public class LoanApplication {
    private final String applicationId;
    private final Customer customer;
    private final double requestedAmount;

    public LoanApplication(String applicationId, Customer customer, double requestedAmount)
            throws InvalidApplicationException {
        if (applicationId == null || applicationId.trim().isEmpty()) {
            throw new InvalidApplicationException("Application ID cannot be empty.");
        }
        if (customer == null) {
            throw new InvalidApplicationException("Customer reference cannot be null.");
        }
        if (requestedAmount <= 0) {
            throw new InvalidApplicationException("Requested loan amount must be greater than zero.");
        }

        this.applicationId = applicationId;
        this.customer = customer;
        this.requestedAmount = requestedAmount;
    }

    public String getApplicationId() { return applicationId; }
    public Customer getCustomer() { return customer; }
    public double getRequestedAmount() { return requestedAmount; }
}
