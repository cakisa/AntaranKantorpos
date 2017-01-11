package com.horirevens.antarankantorpos.antaran;

/**
 * Created by horirevens on 11/25/16.
 */
public class Antaran {
    private String akditem, anippos, akdstatus, awktlokal, aketerangan;

    public Antaran(String akditem, String anippos, String akdstatus,
                   String awktlokal, String aketerangan) {
        this.akditem = akditem;
        this.anippos = anippos;
        this.awktlokal = awktlokal;
        this.akdstatus = akdstatus;
        this.aketerangan = aketerangan;
    }

    public String getAkditem() {
        return akditem;
    }
    public void setAkditem(String akditem) {
        this.akditem = akditem;
    }

    public String getAnippos() {
        return anippos;
    }
    public void setAnippos(String anippos) {
        this.anippos = akditem;
    }

    public String getAwktlokal() {
        return awktlokal;
    }
    public void setAwktlokal(String awktlokal) {
        this.awktlokal = awktlokal;
    }

    public String getAkdstatus() {
        return akdstatus;
    }
    public void setAkdstatus(String akdstatus) {
        this.akdstatus = akdstatus;
    }

    public String getAdaAketerangan() {
        return aketerangan;
    }
    public void setAdaAketerangan(String aketerangan) {
        this.aketerangan = aketerangan;
    }
}
