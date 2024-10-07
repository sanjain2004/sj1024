package cf.service;

import cf.exception.ValidationException;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Map;


public class TestValidations {

    @Test
    void testValidatePercent() {
        Map<String, String> args = ImmutableMap.of("code", "CHNS", "checkoutDate", "10/05/2024", "numDays", "1", "discount", "101");

        //cf.exception.ValidationException thrown
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> new GenerateRentalAgreementOperation(args)
        );

        assertTrue(exception.getMessage().contains("percent"));
    }

    @Test
    void testValidateGood() {
        Map<String, String> args = ImmutableMap.of("code", "CHNS", "checkoutDate", "10/09/2024", "numDays", "1", "discount", "11");

        GenerateRentalAgreementOperation op = new GenerateRentalAgreementOperation(args);
        assertEquals(op.toolOperationData.discountPercent, 11);

    }

}
