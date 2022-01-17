/*
 * Copyright (c) 2015-2022, Patricia Maraver
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *  
 */

package org.neuromorpho.literature.gateway.filter.pre;

import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.ZuulFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class SimpleFilter extends ZuulFilter {

  private static Logger log = LoggerFactory.getLogger(SimpleFilter.class);

  /**
   * @apiVersion 1.0.0
   * @api   URIs Configuration 
   * @apiParam {:8189/metadata/}  metadata Redirects to :8180
   * @apiParam {:8189/fulltext/}  fulltext Redirects to :8182
   * @apiParam {:8189/emails/}  agenda Redirects to :8183
   * @apiParam {:8189/evaluate/}  evaluate Redirects to :8184
   * @apiParam {:8189/search/}  search Redirects to :8187
   * @apiParam {:8189/articles/}  articles Redirects to :8188
   * @apiParam {:8189/pdf/}  pdf Redirects to :8186
   * @apiParam {:8189/fileacquisition/}  fileacquisition Redirects to :8190
   * @apiParam {:8189/reports/}  reports Redirects to :8193
   * @apiParam {:8189/release/}  release Redirects to :8192

   * @apiDescription The gateway unifies the ports. Use the port 8189 for all the services API and append the names
   * as follows:
   * @apiName Gateway
   * @apiGroup  Gateway
   *
   */
  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public int filterOrder() {
    return 1;
  }

  @Override
  public boolean shouldFilter() {
    return true;
  }

  @Override
  public Object run() {
    RequestContext ctx = RequestContext.getCurrentContext();
    HttpServletRequest request = ctx.getRequest();

    log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

    return null;
  }

}
