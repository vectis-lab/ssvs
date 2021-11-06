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

package au.org.garvan.ssvs.core.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import au.org.garvan.ssvs.core.dao.SummStatsDao;
import au.org.garvan.ssvs.core.entity.*;

@Stateless
public class SummStatsJPA2Impl implements SummStatsDao {

    @PersistenceContext(unitName="SummStatsPersistenceUnit")
    private EntityManager entityManager;

    public Long count(Chromosome chr, Integer start, Integer end) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Variant> root = cq.from(Variant.class);

        ParameterExpression<String>  paramChr = cb.parameter(String.class);
        Predicate predChr = cb.equal(root.get("chr"), paramChr);

        Expression<Integer> pos = root.get("start");
        Predicate predStart = cb.between(pos, start, end);

        cq.select(cb.count(root));
        cq.where(cb.and(predChr,predStart));

        TypedQuery<Long> q = entityManager.createQuery(cq);
        q.setParameter(paramChr, chr.toString());

        return q.getSingleResult();
    }

//    public BeaconResponse beacon(Chromosome chr, Integer start, Allele allele) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<V> cq = cb.createQuery(V.class);
//        Root<Variant> root = cq.from(Variant.class);
//
//        ParameterExpression<String>  paramChr = cb.parameter(String.class);
//        Predicate predChr = cb.equal(root.get("chr"), paramChr);
//
//        Expression<Integer> pos = root.get("start");
//        Predicate predStart = cb.equal(pos, start);
//
//        ParameterExpression<String>  paramAlt = cb.parameter(String.class);
//        Predicate predAlt = cb.equal(root.get("alt"), paramAlt);
//
//        cq.select(cb.construct(V.class, root.get("v"), root.get("chr"), root.get("start"), root.get("rsid"),
//            root.get("af"), root.get("nHet"), root.get("nHomVar")));
//        cq.where(cb.and(predChr,predStart,predAlt));
//
//        TypedQuery<V> q = entityManager.createQuery(cq);
//        q.setParameter(paramChr, chr.toString());
//        q.setParameter(paramAlt, allele.toString());
//
//        V res = q.getSingleResult();
//
//        return new BeaconResponse(allele.toString(), res.getAf(), res.getnHet() + res.getnHom() * 2);
//    }
//
    public List<Variant> q(Chromosome chr, Integer start, Integer end, Integer lim, Integer skip) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<V> cq = cb.createQuery(V.class);
        Root<V> root = cq.from(V.class);

        ParameterExpression<String>  paramChr = cb.parameter(String.class);
        Predicate predChr = cb.equal(root.get("chr"), paramChr);

        Expression<Integer> pos = root.get("start");
        Predicate predStart = cb.between(pos, start, end);

        cq.select(cb.construct(V.class, root.get("v"), root.get("chr"), root.get("start"), root.get("rsid"),
                               root.get("af"), root.get("nHet"), root.get("nHom")));
        cq.where(cb.and(predChr,predStart));

        TypedQuery<V> q = entityManager.createQuery(cq);
        q.setParameter(paramChr, chr.toString());

        q.setMaxResults(lim);

        if (skip != null && skip >= 0)
            q.setFirstResult(skip);

        List<V> qVariants = q.getResultList();
        List<Variant> variants =  new ArrayList<>(qVariants.size());

        Iterator<V> it = qVariants.iterator();
        while (it.hasNext()) {
            V v = it.next();
            variants.add(new Variant(v.getV(), v.getChr(), v.getStart(), v.getRsid(), v.getAf(), v.getnHet(), v.getnHom()));
        }
        return variants;
    }

    public List<Variant> query(Chromosome chr, Integer start, Integer end, Integer lim, Integer skip, Field sortBy, Boolean desc) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Variant> cq = cb.createQuery(Variant.class);
        Root<Variant> root = cq.from(Variant.class);

        ParameterExpression<String>  paramChr = cb.parameter(String.class);
        Predicate predChr = cb.equal(root.get("chr"), paramChr);

        Expression<Integer> pos = root.get("start");
        Predicate predStart = cb.between(pos, start, end);

        cq.select(cb.construct(Variant.class, root.get("v"), root.get("chr"), root.get("start"), root.get("ref"), root.get("alt"),
            root.get("rsid"), root.get("ac"), root.get("af"), root.get("nHomRef"), root.get("nHet"), root.get("nHomVar"),
            root.get("type"), root.get("cato"), root.get("eigen"), root.get("sift"), root.get("polyPhen"), root.get("hrcAF"),
            root.get("gnomadAF"), root.get("gnomadAF_AFR"), root.get("gnomadAF_AMR"), root.get("gnomadAF_ASJ"),
            root.get("gnomadAF_EAS"), root.get("gnomadAF_FIN"), root.get("gnomadAF_NFE"), root.get("gnomadAF_OTHD"),
            root.get("ensemblId"), root.get("consequences"), root.get("geneSymbol"), root.get("clinvar"), root.get("wasSplit")));
        cq.where(cb.and(predChr,predStart));

        if (sortBy != null && desc)
            cq.orderBy(cb.desc(root.get(sortBy.toString())));
        else if (sortBy != null)
            cq.orderBy(cb.asc(root.get(sortBy.toString())));

        TypedQuery<Variant> q = entityManager.createQuery(cq);
        q.setParameter(paramChr, chr.toString());

        q.setMaxResults(lim);

        if (skip != null && skip >= 0)
            q.setFirstResult(skip);

        return q.getResultList();
    }
}