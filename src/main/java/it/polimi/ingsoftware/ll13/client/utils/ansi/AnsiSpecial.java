package it.polimi.ingsoftware.ll13.client.utils.ansi;

/**
 * AnsiSpecial is an enumeration that provides constants for special ANSI
 * terminal sequences and graphical characters. These sequences are often used
 * to apply terminal formatting or represent basic graphical components.
 * Each constant in this enumeration contains a specific ANSI code or character
 * representation. The ANSI codes can be used to reset terminal formatting or
 * create graphical elements such as corners, borders, and blocks. This can be
 * useful for creating advanced CLI text-based user interfaces or enhancing
 * terminal output.
 * Use the getCode() method to retrieve the ANSI code or character
 * representation associated with each constant.
 */
public enum AnsiSpecial {
    RESET("\u001B[0m");

    private final String code;

    AnsiSpecial(String code) {
        this.code = code;
    }

    /**
     * Retrieves the ANSI code or character representation associated with the current instance.
     *
     * @return a String representing the ANSI code or character for terminal formatting or graphical components.
     */
    public String getCode() {
        return code;
    }
}
