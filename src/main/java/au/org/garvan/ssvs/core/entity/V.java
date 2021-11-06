/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2021 Dmitry Degrave
 * Copyright (c) 2017-2021 Garvan Institute of Medical Research
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package au.org.garvan.ssvs.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@SuppressWarnings("restriction")
@XmlRootElement
@Entity
@Table(name="summstats")
public class V implements Serializable {

    @Id
    @Column(name="VARIANT")
    private String v;

    @Column(name="CHROMOSOME")
    private String chr;

    @Column(name="START")
    private int start;

    @Column(name="RSID")
    private String rsid;

    @Column(name="AF")
    private double af;

    @Column(name="nHet")
    private int nHet;

    @Column(name="nHomVar")
    private int nHom;

    public V() {
        // needed for JAXB
    }

    public V(String v, String chr, int start, String rsid, double af, int nHet, int nHom) {
        this.v = v;
        this.chr = chr;
        this.start = start;
        this.rsid = rsid;
        this.af = af;
        this.nHet = nHet;
        this.nHom = nHom;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getChr() {
        return chr;
    }

    public void setChr(String chr) {
        this.chr = chr;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getRsid() {
        return rsid;
    }

    public void setRsid(String rsid) {
        this.rsid = rsid;
    }

    public double getAf() {
        return af;
    }

    public void setAf(double af) {
        this.af = af;
    }

    public int getnHet() {
        return nHet;
    }

    public void setnHet(int nHet) {
        this.nHet = nHet;
    }

    public int getnHom() {
        return nHom;
    }

    public void setnHom(int nHom) {
        this.nHom = nHom;
    }
}