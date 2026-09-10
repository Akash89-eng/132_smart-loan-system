package com.loan;

import java.util.ArrayList;
import java.util.List;

public class CreditAssessment {
    public static final int MIN_AGE = 21;
    public static final double MIN_MONTHLY_INCOME = 25000.0;
    public static final int MIN_CREDIT_SCORE = 600;
    public static final double MAX_PERMISSIBLE_DTI = 0.50; // 50%
    public static final double INCOME_MULTIPLIER = 10.0;   // Max loan = 10x monthly income

    public static class AssessmentResult {
        public final String riskCategory;
        public final double maxPermissibleLoan;
        public final double dtiRatio;
        public final List<String> rejectionReasons;

        public AssessmentResult(String riskCategory, double maxPermissibleLoan,
                                double dtiRatio, List<String> rejectionReasons) {
            this.riskCategory = riskCategory;
            this.maxPermissibleLoan = maxPermissibleLoan;
            this.dtiRatio = dtiRatio;
            this.rejectionReasons = rejectionReasons;
        }
    }

    public AssessmentResult assess(LoanApplication application) {
        if (application == null) {
            throw new IllegalArgumentException("Application cannot be null.");
        }

        Customer c = application.getCustomer();
        List<String> reasons = new ArrayList<>();

        // 1. Age condition (at least 21)
        if (c.getAge() < MIN_AGE) {
            reasons.add("Customer age is below 21 (Current: " + c.getAge() + ")");
        }

        // 2. Valid government identification check
        if (c.getGovtId() == null || c.getGovtId().trim().length() < 5) {
            reasons.add("Invalid or missing government-issued identification");
        }

        // 3. Minimum income threshold
        if (c.getMonthlyIncome() < MIN_MONTHLY_INCOME) {
            reasons.add("Monthly income " + c.getMonthlyIncome() + " is below threshold of " + MIN_MONTHLY_INCOME);
        }

        // 4. Maximum permissible loan calculation
        double maxPermissibleLoan = c.getMonthlyIncome() * INCOME_MULTIPLIER;
        if (application.getRequestedAmount() > maxPermissibleLoan) {
            reasons.add("Requested amount " + application.getRequestedAmount()
                    + " exceeds maximum permissible limit of " + maxPermissibleLoan);
        }

        // 5. Minimum credit score requirement
        if (c.getCreditScore() < MIN_CREDIT_SCORE) {
            reasons.add("Credit score " + c.getCreditScore() + " is below minimum requirement of " + MIN_CREDIT_SCORE);
        }

        // 6. Debt-to-income ratio (DTI)
        double dti = (c.getMonthlyIncome() > 0) ? (c.getExistingMonthlyDebt() / c.getMonthlyIncome()) : 1.0;
        if (dti > MAX_PERMISSIBLE_DTI) {
            reasons.add("DTI ratio " + String.format("%.2f%%", dti * 100) + " exceeds maximum allowed 50.00%");
        }

        // Rejection / High Risk classification if any critical rule is violated
        if (!reasons.isEmpty()) {
            return new AssessmentResult("High Risk/Rejected", maxPermissibleLoan, dti, reasons);
        }

        // Low Risk: High credit score (>= 750) and Low DTI (<= 30%)
        if (c.getCreditScore() >= 750 && dti <= 0.30) {
            return new AssessmentResult("Low Risk", maxPermissibleLoan, dti, reasons);
        }

        // Medium Risk: Satisfies minimum requirements with moderate credit or moderate DTI
        return new AssessmentResult("Medium Risk", maxPermissibleLoan, dti, reasons);
    }
}
