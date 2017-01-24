package com.horirevens.hornettest.adrstatus;

/**
 * Created by horirevens on 1/19/17.
 */
public class Adrstatus {
    private String astatus, aketerangan;

    public Adrstatus(String astatus, String aketerangan) {
        this.astatus = astatus;
        this.aketerangan = aketerangan;
    }

    public String getAstatus() {
        return astatus;
    }
    public void setAstatus(String astatus) {
        this.astatus = astatus;
    }

    public String getAketerangan() {
        return getAketerangan();
    }
    public void setAketerangan(String aketerangan) {
        this.aketerangan = aketerangan;
    }
}
