package io.github.renatolsjf.utils.string.casestring;

import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;

class CaseStringTest {



    @org.junit.jupiter.api.Test
    void isCaseRepresentation() {

        CaseString cs = CaseString.parse(" This is A random-pieceOf string   MADE----to___bReaK");
        Assertions.assertTrue(cs.isCaseRepresentation("this-is-a-random-piece-of-string-m-a-d-e-to-b-rea-k"));
        Assertions.assertTrue(cs.isCaseRepresentation("this_is_a_random_piece_of_string_m_a_d_e_to_b_rea_k"));
        Assertions.assertTrue(cs.isCaseRepresentation("this is a random piece of string m a d e to b rea k"));
        Assertions.assertTrue(cs.isCaseRepresentation("thisIsARandomPieceOfStringMADEToBReaK"));
        Assertions.assertTrue(cs.isCaseRepresentation("ThisIsARandomPieceOfStringMADEToBReaK"));

        cs = CaseString.parse(" Just a normal string");
        Assertions.assertTrue(cs.isCaseRepresentation("just-a-normal-string"));
        Assertions.assertTrue(cs.isCaseRepresentation("just_a_normal_string"));
        Assertions.assertTrue(cs.isCaseRepresentation("just a normal string"));
        Assertions.assertTrue(cs.isCaseRepresentation("justANormalString"));
        Assertions.assertTrue(cs.isCaseRepresentation("JustANormalString"));

    }

    @org.junit.jupiter.api.Test
    void getCaseValues() {
        CaseString cs = CaseString.parse(" This is A random-pieceOf string   MADE----to___bReaK");
        List<String> matchingValues = cs.getCaseValues();
        Assertions.assertTrue(matchingValues.size() == 5
                && matchingValues.contains("this-is-a-random-piece-of-string-m-a-d-e-to-b-rea-k")
                && matchingValues.contains("this_is_a_random_piece_of_string_m_a_d_e_to_b_rea_k")
                && matchingValues.contains("this is a random piece of string m a d e to b rea k")
                && matchingValues.contains("thisIsARandomPieceOfStringMADEToBReaK")
                && matchingValues.contains("ThisIsARandomPieceOfStringMADEToBReaK"));

    }

    @org.junit.jupiter.api.Test
    void getCaseValue() {
        CaseString cs = CaseString.parse(" This is A random-pieceOf string   MADE----to___bReaK");
        Assertions.assertTrue("this-is-a-random-piece-of-string-m-a-d-e-to-b-rea-k".equals(cs.getCaseValue(CaseString.CaseType.KEBAB)));
        Assertions.assertTrue("this_is_a_random_piece_of_string_m_a_d_e_to_b_rea_k".equals(cs.getCaseValue(CaseString.CaseType.SNAKE)));
        Assertions.assertTrue("this is a random piece of string m a d e to b rea k".equals(cs.getCaseValue(CaseString.CaseType.SPACED)));
        Assertions.assertTrue("thisIsARandomPieceOfStringMADEToBReaK".equals(cs.getCaseValue(CaseString.CaseType.CAMEL)));
        Assertions.assertTrue("ThisIsARandomPieceOfStringMADEToBReaK".equals(cs.getCaseValue(CaseString.CaseType.PASCAL)));
    }

    @org.junit.jupiter.api.Test
    void original() {
        CaseString cs = CaseString.parse(" This is A random-pieceOf string   MADE----to___bReaK");
        Assertions.assertTrue(" This is A random-pieceOf string   MADE----to___bReaK".equals(cs.original()));
    }

    @org.junit.jupiter.api.Test
    void kebab() {
        CaseString cs = CaseString.parse(" This is A random-pieceOf string   MADE----to___bReaK");
        Assertions.assertTrue("this-is-a-random-piece-of-string-m-a-d-e-to-b-rea-k".equals(cs.kebab()));
    }

    @org.junit.jupiter.api.Test
    void snake() {
        CaseString cs = CaseString.parse(" This is A random-pieceOf string   MADE----to___bReaK");
        Assertions.assertTrue("this_is_a_random_piece_of_string_m_a_d_e_to_b_rea_k".equals(cs.snake()));
    }

    @org.junit.jupiter.api.Test
    void camel() {
        CaseString cs = CaseString.parse(" This is A random-pieceOf string   MADE----to___bReaK");
        Assertions.assertTrue("thisIsARandomPieceOfStringMADEToBReaK".equals(cs.camel()));
    }

    @org.junit.jupiter.api.Test
    void pascal() {
        CaseString cs = CaseString.parse(" This is A random-pieceOf string   MADE----to___bReaK");
        Assertions.assertTrue("ThisIsARandomPieceOfStringMADEToBReaK".equals(cs.pascal()));
    }

    @org.junit.jupiter.api.Test
    void spaced() {
        CaseString cs = CaseString.parse(" This is A random-pieceOf string   MADE----to___bReaK");
        Assertions.assertTrue("this is a random piece of string m a d e to b rea k".equals(cs.spaced()));
    }

    @org.junit.jupiter.api.Test
    void createMapExtractor() {

        Map<String, String> m = Map.of(
                "a-key", "a-value",
                "anotherKey", "anotherValue",
                "third key", "third value",
                "yet_another_key", "yet_another_value",
                "NotTheLastKey", "NotTheLastValue",
                "notTheLastKeyEither", "NotTheLastValueEither",
                "this-is a-MIXED_key", "this-is a-MIXED_value"
        );

        CaseString cs = CaseString.parse("NotAKey");
        ValueExtractor<String, Map> ve = cs.createMapExtractor(String.class);
        Assertions.assertNull(ve.extractValue(m, false));
        ValueExtractor<String, Map> aFinalVe = ve;
        Assertions.assertThrows(UnavailableKeyException.class, () -> aFinalVe.extractValueOrThrowException(m, false));

        cs = CaseString.parse("AKey");
        ve = cs.createMapExtractor(String.class);
        Assertions.assertTrue("a-value".equals(ve.extractValue(m, false)));

        cs = CaseString.parse("___a----- --Key");
        ve = cs.createMapExtractor(String.class);
        Assertions.assertTrue("a-value".equals(ve.extractValue(m, false)));

        cs = CaseString.parse("third_key");
        ve = cs.createMapExtractor(String.class);
        Assertions.assertTrue("third value".equals(ve.extractValue(m, false)));

        cs = CaseString.parse("Yet another-key");
        ve = cs.createMapExtractor(String.class);
        Assertions.assertTrue("yet_another_value".equals(ve.extractValue(m, false)));

        cs = CaseString.parse("notTheLast_- -_Key");
        ve = cs.createMapExtractor(String.class);
        Assertions.assertTrue("NotTheLastValue".equals(ve.extractValue(m, false)));

        cs = CaseString.parse("-_--notThe-Last_keyEither");
        ve = cs.createMapExtractor(String.class);
        Assertions.assertTrue("NotTheLastValueEither".equals(ve.extractValue(m, false)));

        cs = CaseString.parse("this-is a-MIXED_key");
        ve = cs.createMapExtractor(String.class);
        Assertions.assertNull(ve.extractValue(m, false));
        ValueExtractor<String, Map> anotherFinalVe = ve;
        Assertions.assertThrows(UnavailableKeyException.class, () -> anotherFinalVe.extractValueOrThrowException(m, false));
        Assertions.assertTrue("this-is a-MIXED_value".equals(ve.extractValue(m, true)));


    }

    @org.junit.jupiter.api.Test
    void equalsIgnoreInput() {
        CaseString cs = CaseString.parse(" This is-_---OneInput_Named-as-a");
        CaseString csEquals = CaseString.parse(" This is-_---OneInput_Named-as-a");
        CaseString csEqualsIgnoreInput = CaseString.parse("this_isOne-input named as_A");
        CaseString csNone = CaseString.parse("AnyString");

        Assertions.assertEquals(cs, csEquals);
        Assertions.assertNotEquals(cs, csEqualsIgnoreInput);
        Assertions.assertNotEquals(cs, csNone);

        Assertions.assertTrue(cs.equalsIgnoreInput(csEquals));
        Assertions.assertTrue(cs.equalsIgnoreInput(csEqualsIgnoreInput));
        Assertions.assertFalse(cs.equalsIgnoreInput(csNone));

    }

    @org.junit.jupiter.api.Test
    void equalsString() {
        CaseString cs = CaseString.parse(" This is-_---OneInput_Named-as-a");
        String s = "this_isOne-input named as_A";
        String s2 = "AnyString";

        Assertions.assertTrue(cs.equalsString(s));
        Assertions.assertFalse(cs.equalsString(s2));
    }

}