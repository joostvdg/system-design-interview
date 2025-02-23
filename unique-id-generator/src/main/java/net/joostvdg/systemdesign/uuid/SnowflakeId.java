package net.joostvdg.systemdesign.uuid;

public class SnowflakeId {
    private final BitManipulator bitManipulator;
    private final Long id;

    // Sequence: rightmost 12 bits (0-11)
    private static final int SEQUENCE_BITS = 12;
    private static final int SEQUENCE_START = 0;

    // Machine: next 5 bits (12-16)
    private static final int MACHINE_CODE_BITS = 5;
    private static final int MACHINE_CODE_START = 12;

    // Datacenter: next 5 bits (17-21)
    private static final int DATACENTER_CODE_BITS = 5;
    private static final int DATACENTER_CODE_START = 17;

    // Timestamp: next 41 bits (22-62)
    private static final int TIMESTAMP_BITS = 41;
    private static final int TIMESTAMP_START = 22;

    // Bit 63 remains 0 (sign bit)

    private static final long SINCE_EPOCH = 1721681612166L;

    public SnowflakeId() {
        this.bitManipulator = new BitManipulator();
        this.id = 0L;
    }

    public long id() {
        return this.id;
    }

    /**
     * Set the timestamp of the SnowflakeId.
     * These are the bits 1-42 of the SnowflakeId.
     */
    public void setMillisecondSequence() {
        // Need the millliseconds elapsed since the epoch
        long milliseconds = System.currentTimeMillis() - SINCE_EPOCH;
        System.out.println("Setting milliseconds: " + milliseconds);
        setSegment(TIMESTAMP_START, TIMESTAMP_BITS, (int) milliseconds);
    }

    /**
     * Set the dataCenterId of the SnowflakeId.
     * These are the bits 48-52 of the SnowflakeId.
     */
    public void setDataCenterId(int dataCenterId) {
        System.out.println("Setting dataCenterId: " + dataCenterId);
        setSegment(DATACENTER_CODE_START, DATACENTER_CODE_BITS, dataCenterId);
    }

    /**
     * Set the machineId of the SnowflakeId.
     * These are the bits 43-47 of the SnowflakeId.
     */
    public void setMachineId(int machineId) {
        // Set the bits 43-47 of the SnowflakeId
        System.out.println("Setting machineId: " + machineId);
        setSegment(MACHINE_CODE_START, MACHINE_CODE_BITS, machineId);
    }

    /**
     * Set the sequence of the SnowflakeId.
     * These are the bits 53-64 of the SnowflakeId.
     */
    public void setSequence(int sequence) {
        System.out.println("Setting sequence: " + sequence);
        setSegment(SEQUENCE_START, SEQUENCE_BITS, sequence);
    }

    private void setSegment(int start, int numberOfBits, int value) {
        for (int i = 0; i < numberOfBits; i++) {
            int dataPosition = start + i;  // Write in reverse order
            int bitValue = (value >> (numberOfBits - i - 1)) & 1;
            System.out.println("Setting bit at position: " + dataPosition + " to: " + bitValue);
            if (bitValue == 1) {
                this.bitManipulator.setBit(dataPosition);
            } else {
                this.bitManipulator.clearBit(dataPosition);
            }
        }
    }

    public long getId() {
        return this.bitManipulator.getData();
    }

    public String toBinaryString() {
        return Long.toBinaryString(this.bitManipulator.getData());
    }

}
