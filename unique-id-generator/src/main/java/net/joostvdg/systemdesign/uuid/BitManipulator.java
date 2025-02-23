package net.joostvdg.systemdesign.uuid;

/**
 * A convenience class to manipulate bits of a 64-bit long value.
 */
public class BitManipulator {

    private long data;

    /**
     * Constructor, initializes the data with all bits set to 0.
     */
    public BitManipulator() {
        this.data = 0; // initialize with all bits set to 0
    }

    /**
     * Get the bit at the given position.
     * @param position the position of the bit to get
     */
    public void setBit(int position) {
        this.data |= 1L << position;
    }

    /**
     * Clear the bit at the given position.
     * @param position the position of the bit to clear
     */
    public void clearBit(int position) {
        this.data &= ~(1L << position);
    }

    /**
     * Toggle the bit at the given position.
     * @param position the position of the bit to toggle
     */
    public void toggleBit(int position) {
        this.data ^= (1L << position);
    }

    /**
     * Check if the bit at the given position is set.
     * @param position the position of the bit to check
     * @return true if the bit is set, false otherwise
     */
    public boolean isBitSet(int position) {
        return (this.data & (1L << position)) != 0;
    }

    /**
     * Get the value of the data.
     * @return the value of the data
     */
    public long getData() {
        return this.data;
    }
}
