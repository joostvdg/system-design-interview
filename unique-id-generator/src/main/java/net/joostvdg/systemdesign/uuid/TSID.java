package net.joostvdg.systemdesign.uuid;

public record TSID(long id) {
    public TSID {
        if (id < 0) {
            throw new IllegalArgumentException("ID must be positive");
        }
    }

}
