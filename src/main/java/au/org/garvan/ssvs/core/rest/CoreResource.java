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

package au.org.garvan.ssvs.core.rest;

import au.org.garvan.ssvs.core.entity.CoreResponse;
import au.org.garvan.ssvs.core.entity.POSTParamsJaxBean;
import au.org.garvan.ssvs.core.service.CoreService;
import au.org.garvan.ssvs.core.entity.CoreQuery;
import au.org.garvan.ssvs.core.util.CoreQueryUtils;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * VSAL Core rest resource.
 *
 * @author Dmitry Degrave
 * @version 1.0
 */
@Path("/search")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
public class CoreResource {

    @Inject
    private CoreService service;

    @GET
    public CoreResponse query(@QueryParam("chr") String chr,
                              @QueryParam("start") Integer positionStart,
                              @QueryParam("end") Integer positionEnd,
                              @QueryParam("alt") String alt,
                              @QueryParam("ref") String ref,
                              @QueryParam("limit") Integer lim,
                              @QueryParam("skip") Integer skip,
                              @QueryParam("annot") Boolean annot,
                              @QueryParam("sortBy") String sortBy,
                              @QueryParam("descend") Boolean descend,
                              @QueryParam("count") Boolean count,
                              @QueryParam("beacon") Boolean beacon,
                              @QueryParam("dataset") String dataset,
                              @Context HttpHeaders headers) {

        List<String> authzScheme = headers.getRequestHeader("Authorization");
        String authz = (authzScheme != null && !authzScheme.isEmpty()) ? authzScheme.get(0) : null;
        CoreQuery coreQuery = CoreQueryUtils.getCoreQuery(dataset, chr, positionStart, positionEnd, alt, ref, lim, skip, annot,
                                                          sortBy, descend, count, beacon, authz);
        return service.query(coreQuery);
    }

    /**
     * VSAL REST end point: /find
     * <p>
     * Either <b>chromosome</b> or <b>dbSNP</b> or <b>pheno</b> is required. <b>dataset</b> is always required. Everything else is optional.
     * <p>
     * @return {@link CoreResponse}
     */
    @POST
    @Consumes({"application/json"})
    public CoreResponse queryPost(POSTParamsJaxBean params, @Context HttpHeaders headers) {
        List<String> authzScheme = headers.getRequestHeader("Authorization");
        String authz = (authzScheme != null && !authzScheme.isEmpty()) ? authzScheme.get(0) : null;
        CoreQuery coreQuery = CoreQueryUtils.getCoreQuery(params.dataset,params.chromosome, params.positionStart, params.positionEnd,
                                                          params.altAllele, params.refAllele, params.limit, params.skip, params.annot,
                                                          params.sortBy, params.descend, params.count, params.beacon, authz);
        return service.query(coreQuery);
    }
}