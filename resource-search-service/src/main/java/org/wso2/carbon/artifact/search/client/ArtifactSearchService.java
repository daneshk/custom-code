/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.artifact.search.client;

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.artifact.search.internal.SearchDataHolder;
import org.wso2.carbon.artifact.search.utils.SearchConstants;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.context.RegistryType;
import org.wso2.carbon.registry.api.Registry;
import org.wso2.carbon.registry.api.RegistryException;
import org.wso2.carbon.registry.api.Resource;
import org.wso2.carbon.registry.common.ResourceData;
import org.wso2.carbon.registry.common.services.RegistryAbstractAdmin;
import org.wso2.carbon.registry.core.RegistryConstants;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ArtifactSearchService extends RegistryAbstractAdmin {

    SearchDataHolder searchHolder = SearchDataHolder.getInstance();
    private static final Log log = LogFactory.getLog(ArtifactSearchService.class);

    public String[] getResultArtifacts(final String serviceName, final String version, final String mediaType)
            throws AxisFault {

        List<String> endpoints = new LinkedList<String>();
        try {

            Map<String, String> listMap = new HashMap<String, String>();
            // Create the search attribute map
            if (serviceName != null) {
                listMap.put(SearchConstants.SERVICE_NAME, serviceName.toLowerCase());
            }
            if (version != null) {
                listMap.put(SearchConstants.SERVICE_VERSION, version.toLowerCase());
            }
            if (mediaType != null && !("".equals(mediaType))) {
                listMap.put(SearchConstants.MEDIA_TYPE, mediaType.toLowerCase());
            } else {
                listMap.put(SearchConstants.MEDIA_TYPE, SearchConstants.DEFAULT_SERVICE_MEDIA_TYPE);
            }

            for (ResourceData resourceData : searchHolder.getAttributeIndexingService().search(listMap)) {
                Registry govRegistry = CarbonContext.getThreadLocalCarbonContext().getRegistry(RegistryType.USER_GOVERNANCE);
                String path = resourceData.getResourcePath().substring(RegistryConstants.GOVERNANCE_REGISTRY_BASE_PATH.length());
                Resource resource = govRegistry.get(path);
                endpoints.addAll(resource.getPropertyValues(SearchConstants.SERVICE_ENDPOINT_ENTRY));
            }
        } catch (RegistryException ex) {
            log.error("Failed to returnServices ",ex);
        }

        return endpoints.toArray(new String[endpoints.size()]);
    }
}
