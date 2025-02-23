package net.joostvdg.systemdesign.uuid;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SnowflakeGeneratorTest {

    @Test
    void testGenerateSingleID() {
        SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();
        snowflakeGenerator.start();
        long id = snowflakeGenerator.generate(1, 1).id();
        snowflakeGenerator.shutdown();
        String binaryStringOfId = String.format("%64s", Long.toBinaryString(id)).replace(' ', '0');
        StringBuilder reversed = new StringBuilder(binaryStringOfId).reverse();

        System.out.println("Reversed ID: " + reversed.toString());
        System.out.println("reversedSequence="+reversed.substring(0, 11));
        System.out.println("reversedMachineId="+reversed.substring(12, 17));
        System.out.println("reversedDatacenterId="+reversed.substring(17, 22));
        System.out.println("reversedTimestamp="+reversed.substring(22, 63));
        System.out.println("reversedSignBit="+reversed.substring(63, 64));
        System.out.println("size of reversed="+reversed.length());
//
//        // traverse the string from right to left
//        //
//        System.out.println("Sequence: rightmost 12 bits (0-11)");
//        for (int i = 0; i < binaryStringOfId.length(); i++) {
//            if (i == 12) {
//                System.out.println("Machine: next 5 bits (12-16)");
//            } else if (i == 17) {
//                System.out.println("Datacenter: next 5 bits (17-21)");
//            } else if (i == 22) {
//                System.out.println("Timestamp: next 41 bits (22-62)");
//            } else if (i == 63) {
//                System.out.println("Bit 63 remains 0 (sign bit)");
//            }
//
//            String count = ""+i;
//            if (i < 10) {
//                count = "0" + count;
//            }
//            System.out.println("bit["+count+"]="+binaryStringOfId.charAt(i));
//        }

//         1|1110011000011010001010011010100111100111|00001|00001|000000000000
//        ^|_______________________________________|______|______|____________
//         | Timestamp (41 bits)                      DC(5)  MC(5) Sequence(12)
//        Sign bit (should be 0!)

        String expectedSignBit = "0";
        String expectedMachineId = toBinaryString(1, 5);
        String expectedDatacenterId = toBinaryString(1, 5);

        assertEquals(expectedSignBit, reversed.substring(63, 64));
        assertEquals(expectedDatacenterId, reversed.substring(17, 22));
        assertEquals(expectedMachineId, reversed.substring(12, 17));
    }

    @Test
    void testDifferentDataCenterAndMachineIds() {
        SnowflakeGenerator generator = new SnowflakeGenerator();
        generator.start();

        TSID id1 = generator.generate(2, 3);
        String binary1 = String.format("%64s", Long.toBinaryString(id1.id())).replace(' ', '0');
        StringBuilder reversed1 = new StringBuilder(binary1).reverse();

        assertEquals("00010", reversed1.substring(12, 17)); // machineId 2
        assertEquals("00011", reversed1.substring(17, 22)); // dataCenter 3

        TSID id2 = generator.generate(31, 15);
        String binary2 = String.format("%64s", Long.toBinaryString(id2.id())).replace(' ', '0');
        StringBuilder reversed2 = new StringBuilder(binary2).reverse();

        assertEquals("11111", reversed2.substring(12, 17)); // machineId 31 (max 5 bits)
        assertEquals("01111", reversed2.substring(17, 22)); // dataCenter 15

        generator.shutdown();
    }

    @Test
    void testTimestampIncreases() {
        SnowflakeGenerator generator = new SnowflakeGenerator();
        generator.start();

        // Generate two IDs with a small delay
        TSID id1 = generator.generate(1, 1);
        try {
            Thread.sleep(2); // Wait 2ms to ensure timestamp difference
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        TSID id2 = generator.generate(1, 1);

        // Extract timestamp portion (bits 1-41) from both IDs
        String binary1 = String.format("%64s", Long.toBinaryString(id1.id())).replace(' ', '0');
        String binary2 = String.format("%64s", Long.toBinaryString(id2.id())).replace(' ', '0');

        String timestamp1 = binary1.substring(1, 42);
        String timestamp2 = binary2.substring(1, 42);

        // Convert binary timestamps to long values for comparison
        long ts1 = Long.parseLong(timestamp1, 2);
        long ts2 = Long.parseLong(timestamp2, 2);

        assertTrue(ts2 > ts1, "Second timestamp should be larger than first");

        generator.shutdown();
    }

    @Test
    void testSequenceOverflow() {
        SnowflakeGenerator generator = new SnowflakeGenerator();
        generator.start();

        // Generate enough IDs to overflow the sequence (4095 is max)
        TSID first = generator.generate(1, 1);
        for(int i = 0; i < 4096; i++) {
            generator.generate(1, 1);
        }
        TSID last = generator.generate(1, 1);

        assertTrue(last.id() > first.id(), "Last ID should be larger than first");

        generator.shutdown();
    }

    private String toBinaryString(long input, int expectedSize) {
        String binaryString = Long.toBinaryString(input);
        return String.format("%"+expectedSize+"s", binaryString).replace(' ', '0');
    }
}