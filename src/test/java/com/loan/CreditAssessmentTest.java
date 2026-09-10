package com.loan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CreditAssessmentTest {
    private CreditAssessment assessment;

    @BeforeEach
    public void setup() {
        assessment = new CreditAssessment();
    }

    @Test
    public void testLowRiskApproval() throws InvalidApplicationException {
        Customer c = new Customer("C01", "Alice", 28, "GOV12345", 60000, 6000, 790); // DTI = 10%
        LoanApplication app = new LoanApplication("A01", c, 300000);
        CreditAssessment.AssessmentResult res = assessment.assess(app);

        assertEquals("Low Risk", res.riskCategory);
        assertEquals(600000.0, res.maxPermissibleLoan);
        assertTrue(res.rejectionReasons.isEmpty());
    }

    @Test
    public void testBoundaryConditionsExactPass() throws InvalidApplicationException {
        // Exact boundary inputs: age 21, credit score 600, DTI exactly 50% (15000 / 30000)
        Customer c = new Customer("C02", "Bob", 21, "GOV67890", 30000, 15000, 600);
        LoanApplication app = new LoanApplication("A02", c, 300000); // 10x income boundary
        CreditAssessment.AssessmentResult res = assessment.assess(app);

        assertEquals("Medium Risk", res.riskCategory);
        assertEquals(300000.0, res.maxPermissibleLoan);
        assertTrue(res.rejectionReasons.isEmpty());
    }

    @Test
    public void testMultipleFailureReasonsAccumulated() throws InvalidApplicationException {
        // Fails: Age (19 < 21), Income (18000 < 25000), Credit (510 < 600), Requested Amount > Limit
        Customer c = new Customer("C03", "Charlie", 19, "GOV11223", 18000, 4000, 510);
        LoanApplication app = new LoanApplication("A03", c, 350000); // Limit is 180,000
        CreditAssessment.AssessmentResult res = assessment.assess(app);

        assertEquals("High Risk/Rejected", res.riskCategory);
        assertEquals(4, res.rejectionReasons.size());
    }

    @Test
    public void testDtiBoundaryExceededFails() throws InvalidApplicationException {
        // DTI = 16000 / 30000 = 53.33% (> 50%)
        Customer c = new Customer("C04", "Dave", 32, "GOV44556", 30000, 16000, 720);
        LoanApplication app = new LoanApplication("A04", c, 200000);
        CreditAssessment.AssessmentResult res = assessment.assess(app);

        assertEquals("High Risk/Rejected", res.riskCategory);
        assertEquals(1, res.rejectionReasons.size());
        assertTrue(res.rejectionReasons.get(0).contains("DTI ratio"));
    }

    @Test
    public void testInvalidInputExceptions() {
        assertThrows(InvalidApplicationException.class, () -> {
            new Customer("C05", "Emma", -3, "GOV99999", 40000, 2000, 700);
        });

        assertThrows(InvalidApplicationException.class, () -> {
            new Customer("C06", "Frank", 25, "", 40000, 2000, 700);
        });

        assertThrows(InvalidApplicationException.class, () -> {
            new Customer("C07", "Grace", 25, "GOV99999", 40000, 2000, 250);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            assessment.assess(null);
        });
    }
}
