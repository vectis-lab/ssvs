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

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * CoreResponse
 */
@XmlRootElement(name = "CoreResponse")
public class CoreResponse {

    private CoreQuery coreQuery;
    private Long ssvsTimeMs;  // ms
    private List<Variant> variants;
    private Long total;  // total # variants
    private Error error;

    public CoreResponse() {
        // needed for JAXB
    }

    public CoreResponse(CoreQuery coreQuery, Long ssvsTimeMs, Error error) {
        this.coreQuery = coreQuery;
        this.ssvsTimeMs = ssvsTimeMs;
        this.variants = null;
        this.total = 0L;
        this.error = error;
    }

    public CoreResponse(CoreQuery coreQuery, Long ssvsTimeMs, List<Variant> variants, Long total, Error error) {
        this.coreQuery = coreQuery;
        this.ssvsTimeMs = ssvsTimeMs;
        this.variants = variants;
        this.total = total;
        this.error = error;
    }

    public CoreQuery getCoreQuery() {
        return coreQuery;
    }

    public void setCoreQuery(CoreQuery coreQuery) {
        this.coreQuery = coreQuery;
    }

    public Long getSsvsTimeMs() {
        return ssvsTimeMs;
    }

    public void setSsvsTimeMs(Long ssvsTimeMs) {
        this.ssvsTimeMs = ssvsTimeMs;
    }

    public List<Variant> getVariants() {
        return variants;
    }

    public void setVariants(List<Variant> variants) {
        this.variants = variants;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}