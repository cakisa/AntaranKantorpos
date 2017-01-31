package com.horirevens.antarankantorpos.antaran;

/**
 * Created by horirevens on 1/18/17.
 */
public class Antaran {
    private String akditem, akdstatus, awktlokal, ada_aketerangan, ads_aketerangan, astatuskirim, ado;

    public Antaran(String akditem, String akdstatus, String awktlokal, String ada_aketerangan,
                   String ads_aketerangan, String astatuskirim, String ado) {
        this.akditem = akditem;
        this.awktlokal = awktlokal;
        this.akdstatus = akdstatus;
        this.ada_aketerangan = ada_aketerangan;
        this.ads_aketerangan = ads_aketerangan;
        this.astatuskirim = astatuskirim;
        this.ado = ado;
    }

    public String getAkditem() {
        return akditem;
    }
    public void setAkditem(String akditem) {
        this.akditem = akditem;
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

    public String getAda_aketerangan() {
        return ada_aketerangan;
    }
    public void setAda_aketerangan(String ada_aketerangan) {
        this.ada_aketerangan = ada_aketerangan;
    }

    public String getAds_aketerangan() {
        return ads_aketerangan;
    }
    public void setAds_aketerangan(String ads_aketerangan) {
        this.ads_aketerangan = ads_aketerangan;
    }

    public String getAstatuskirim() {
        return astatuskirim;
    }
    public void setAstatuskirim(String astatuskirim) {
        this.astatuskirim = astatuskirim;
    }

    public String getAdo() {
        return ado;
    }
    public void setAdo(String ado) {
        this.ado = ado;
    }
}
