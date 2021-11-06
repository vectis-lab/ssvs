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

package au.org.garvan.ssvs.core.service;

import au.org.garvan.ssvs.core.entity.*;
import au.org.garvan.ssvs.core.entity.Error;
import au.org.garvan.ssvs.core.util.CoreJWT;
import au.org.garvan.ssvs.core.dao.SummStatsDao;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

import com.auth0.jwt.exceptions.JWTVerificationException;

import java.util.List;
import javax.ejb.EJB;

/**
 * VSAL core service.
 *
 * @author Dmitry Degrave (dmeetry@gmail.com)
 * @version 1.0
 */
@RequestScoped
public class CoreService {

    public static final int NANO_TO_MILLI = 1000000;

    @EJB
    private SummStatsDao summStatsDao;

    @PostConstruct
    public void init() {
    }

    public CoreResponse query(CoreQuery q) {

        final long start = System.nanoTime();

        if (q.getDataset() == null) {
            Error errorResource = new Error("Incomplete Query", "A valid dataset is required");
            Long elapsed = (System.nanoTime() - start) / NANO_TO_MILLI;
            return new CoreResponse(q, elapsed, errorResource);
        }

        if (q.getChromosome() == null) {
            Error errorResource = new Error("Incomplete Query", "Chromosome is required");
            Long elapsed = (System.nanoTime() - start) / NANO_TO_MILLI;
            return new CoreResponse(q, elapsed, errorResource);
        }

        if (q.getPositionStart() == null || q.getPositionEnd() == null) {
            Error errorResource = new Error("Incomplete Query", "Start/End positions are required.");
            Long elapsed = (System.nanoTime() - start) / NANO_TO_MILLI;
            return new CoreResponse(q, elapsed, errorResource);
        }

        if (q.getPositionStart() < 0 ||  q.getPositionEnd() < 0) {
            Error errorResource = new Error("Malformed Query", "Start/End positions must be non negative");
            Long elapsed = (System.nanoTime() - start) / NANO_TO_MILLI;
            return new CoreResponse(q, elapsed, errorResource);
        }

        if (q.getPositionEnd() < q.getPositionStart()) {
            Error errorResource = new Error("Malformed Query", "End position of a region should be >= start position");
            Long elapsed = (System.nanoTime() - start) / NANO_TO_MILLI;
            return new CoreResponse(q, elapsed, errorResource);
        }

        try {
            if (!q.getBeacon() && q.getJwt() == null) {
                System.out.println("Unauthorized: JWT is required");
                Long elapsed = (System.nanoTime() - start) / NANO_TO_MILLI;
                Error errorResource = new Error("Unauthorized", "JWT is required");
                return new CoreResponse(q, elapsed, errorResource);
            } else if (!q.getDataset().toString().equalsIgnoreCase("demo") &&
                !q.getDataset().toString().equalsIgnoreCase("mgrb") &&
                !q.getDataset().toString().equalsIgnoreCase("circa")) {
                CoreJWT.verifyJWT(q.getJwt(), q.getDataset().toString().toLowerCase() + "/summary");
            }
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            Long elapsed = (System.nanoTime() - start) / NANO_TO_MILLI;
            Error errorResource = new Error("JWT verification failed", e.getMessage());
            return new CoreResponse(q, elapsed, errorResource);
        } catch (Exception e) {
            e.printStackTrace();
            Error errorResource = new Error("VS Runtime Exception", e.getMessage());
            Long elapsed = (System.nanoTime() - start) / NANO_TO_MILLI;
            return new CoreResponse(q, elapsed, errorResource);
        }

        long total = -1;
        if (q.getCount())
            total = summStatsDao.count(q.getChromosome(), q.getPositionStart(), q.getPositionEnd());

        CoreResponse res;

        if (q.getAnnot()) {
            try {
                List<Variant> variants = summStatsDao.query(q.getChromosome(), q.getPositionStart(), q.getPositionEnd(),
                                                            q.getLimit(), q.getSkip(), q.getSortBy(), q.getDescend());
                Long elapsed = (System.nanoTime() - start) / NANO_TO_MILLI;
                res = new CoreResponse(q, elapsed, variants, total, null);
            } catch (Exception e) {
                e.printStackTrace();
                Error errorResource = new Error("VS Runtime Exception", e.getMessage());
                Long elapsed = (System.nanoTime() - start) / NANO_TO_MILLI;
                res = new CoreResponse(q, elapsed, errorResource);
            }
        } else {
            try {
                List<Variant> variants = summStatsDao.q(q.getChromosome(), q.getPositionStart(), q.getPositionEnd(),
                                                        q.getLimit(), q.getSkip());
                Long elapsed = (System.nanoTime() - start) / NANO_TO_MILLI;
                res = new CoreResponse(q, elapsed, variants, total, null);
            } catch (Exception e) {
                e.printStackTrace();
                Error errorResource = new Error("VS Runtime Exception", e.getMessage());
                Long elapsed = (System.nanoTime() - start) / NANO_TO_MILLI;
                res = new CoreResponse(q, elapsed, errorResource);
            }
        }

        return res;
    }
}