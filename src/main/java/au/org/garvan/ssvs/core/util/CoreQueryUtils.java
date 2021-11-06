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

package au.org.garvan.ssvs.core.util;

import au.org.garvan.ssvs.core.entity.*;

import java.util.*;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;

/**
 * Utils for query manipulation.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @author Dmitry Degrave
 * @version 1.0
 */
public class CoreQueryUtils {

    private static final Map<Reference, String> chromMapping = new HashMap<>();
    private static final int MAX_VARIANTS = 10000;

    static {
        chromMapping.put(Reference.HG38, "GRCh38");
        chromMapping.put(Reference.HG19, "GRCh37");
        chromMapping.put(Reference.HG18, "NCBI36");
        chromMapping.put(Reference.HG17, "NCBI35");
        chromMapping.put(Reference.HG16, "NCBI34");
    }

    /**
     * Generates a canonical chrom ID.
     *
     * @param chrom chromosome
     * @return normalized chromosome value
     */
    private static Chromosome normalizeChromosome(String chrom) {
        // parse chrom value
        if (chrom != null) {
            String orig = chrom.toUpperCase();
            for (Chromosome c : Chromosome.values()) {
                if (orig.endsWith(c.toString())) {
                    return c;
                }
            }
        }
        return null;
    }

    /**
     * Converts 0-based position to 1-based position.
     *
     * @param pos 0-based position
     * @return 1-based position
     */
    public static Long normalizePosition(Long pos) {
        if (pos == null) {
            return null;
        }
        return ++pos;
    }

    /**
     * Generate a canonical allele string.
     *
     * @param allele denormalized allele
     * @return normalized allele
     */
    private static String normalizeAllele(String allele) {
        if (allele == null || allele.isEmpty()) {
            return null;
        }

        String res = allele.toUpperCase();
        if (res.equals("DEL") || res.equals("INS")) {
            return res.substring(0, 1);
        }
        if (Pattern.matches("([D,I])|([A,C,T,G]+)", res)) {
            return res;
        }

        return null;
    }

    /**
     * Generate a canonical genome representation (hg*).
     *
     * @param ref denormalized genome
     * @return normalized genome
     */
    private static Reference normalizeReference(String ref) {
        if (ref == null || ref.isEmpty()) {
            return Reference.HG19;
        }

        for (Reference s : chromMapping.keySet()) {
            if (s.toString().equalsIgnoreCase(ref)) {
                return s;
            }
        }
        for (Map.Entry<Reference, String> e : chromMapping.entrySet()) {
            if (e.getValue().equalsIgnoreCase(ref)) {
                return e.getKey();
            }
        }

        return null;
    }

    private static Field normalizeSortBy(String sortBy) {
        if (sortBy != null) {
            for (Field f : Field.values()) {
                if (sortBy.equalsIgnoreCase(f.toString())) {
                    return f;
                }
            }
        }
        return null;
    }

    /**
     * Obtains a canonical query object.
     */
    public static CoreQuery getCoreQuery(String dataset, String chromosome, Integer position_start, Integer position_end,
                                         String alt, String ref, Integer limit, Integer skip, Boolean annot, String sortBy,
                                         Boolean descend, Boolean count, Boolean beacon, String authz) {
        DatasetID datasetId = DatasetID.fromString(dataset);
        Chromosome c = normalizeChromosome(chromosome);
        Field sortField = normalizeSortBy(sortBy);
        Integer lim = (limit == null || limit < 0 || limit > MAX_VARIANTS) ? MAX_VARIANTS : limit; // production limits
        Boolean desc = (descend == null) ? false : descend;
        Boolean cnt = (count == null) ? false : count;
        Boolean annotations = (annot == null) ? false : annot;
        Boolean bcn = (beacon == null) ? false : beacon;
        Allele aa = (alt == null) ? Allele.None : Allele.fromString(alt);
        Allele ra = (ref == null) ? Allele.None : Allele.fromString(ref);
        String jwtFinal = (authz != null && authz.startsWith("Bearer")) ? authz.substring("Bearer".length()).trim() : null;

        return new CoreQuery(datasetId, c, position_start, position_end, lim, skip, annotations, sortField, desc, cnt, aa, ra, bcn, jwtFinal);
    }

    /*
     * Converts csv string to Array of Ints.
     * If any value is not valid, the whole returned array is null, invalidating parameter.
     */
    private static int[] csvStrToInt(String csv) {
        if (csv == null) return null;
        String[] a = csv.split("\\s*,\\s*");
        int[] ia = new int[a.length];
        try {
            for (int i = 0; i < a.length; ++i) ia[i] = Integer.valueOf(a[i]);
        } catch (NumberFormatException e) {
            return null;
        }
        return ia;
    }

    /*
     * Converts csv string to Array of Chromosomes.
     * If any value is not valid, the whole returned array is null, invalidating parameter.
     */
    private static Chromosome[] csvStrToChr(String csv) {
        if (csv == null) return null;
        String[] a = csv.split("\\s*,\\s*");
        Chromosome[] ca = new Chromosome[a.length];
        for (int i = 0; i < a.length; ++i) {
            ca[i] = normalizeChromosome(a[i]);
            if (ca[i] == null) return null;
        }
        return ca;
    }
}