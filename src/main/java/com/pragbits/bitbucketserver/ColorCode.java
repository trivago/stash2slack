package com.pragbits.bitbucketserver;

public enum ColorCode {

    BLUE("#2267c4"),
    PALE_BLUE("#439fe0"),
    GREEN("#2dc422"),
    DARK_GREEN("#1e8217"),
    PURPLE("#9055fc"),
    GRAY("#aabbcc"),
    RED("#ff0024"),
    DARK_RED("#990016");

    private String code;

    ColorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
