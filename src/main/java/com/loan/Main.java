package com.loan;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        CreditAssessment assessment = new CreditAssessment();
        try {
            Customer c1 = new Customer("C101", "Alice", 29, "GOV-IN-101", 70000, 7000, 780);
            Customer c2 = new Customer("C102", "Bob", 21, "GOV-IN-102", 30000, 12000, 640);
            Customer c3 = new Customer("C103", "Charlie", 19, "GOV-IN-103", 18000, 12000, 520);

            List<LoanApplication> apps = List.of(
                new LoanApplication("APP-1", c1, 300000),
                new LoanApplication("APP-2", c2, 250000),
                new LoanApplication("APP-3", c3, 400000)
            );

            for (LoanApplication app : apps) {
                CreditAssessment.AssessmentResult result = assessment.assess(app);
                System.out.println("--------------------------------------------------");
                System.out.println("Application ID: " + app.getApplicationId() + " (" + app.getCustomer().getName() + ")");
                System.out.println("Status: " + result.riskCategory);
                System.out.println("Max Loan Permissible: $" + result.maxPermissibleLoan);
                System.out.println("DTI Ratio: " + String.format("%.2f%%", result.dtiRatio * 100));
                if (!result.rejectionReasons.isEmpty()) {
                    System.out.println("Rejection Reasons:");
                    result.rejectionReasons.forEach(r -> System.out.println(" - " + r));
                }
            }
        } catch (InvalidApplicationException e) {
            System.err.println("Validation Error: " + e.getMessage());
        }
    }
}
