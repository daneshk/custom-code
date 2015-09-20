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
package org.wso2.carbon.artifact.search.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.registry.common.AttributeSearchService;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.indexing.service.ContentSearchService;

/**
 * @scr.component name="artifact.search.component" immediate="true"
 * @scr.reference name="registry.service"
 * interface="org.wso2.carbon.registry.core.service.RegistryService" cardinality="1..1"
 * policy="dynamic" bind="setRegistryService" unbind="unsetRegistryService"
 * @scr.reference name="registry.indexing"
 * interface="org.wso2.carbon.registry.indexing.service.ContentSearchService" cardinality="1..1"
 * policy="dynamic" bind="setIndexingService" unbind="unsetIndexingService"
 * @scr.reference name="registry.attribute.indexing"
 * interface="org.wso2.carbon.registry.common.AttributeSearchService" cardinality="1..1"
 * policy="dynamic" bind="setAttributeIndexingService" unbind="unsetAttributeIndexingService"
 */
public class ArtifactSearchServiceComponent {

    private static Log log = LogFactory.getLog(ArtifactSearchServiceComponent.class);
    private SearchDataHolder dataHolder = SearchDataHolder.getInstance();

    protected void setRegistryService(RegistryService registryService) {
        dataHolder.setRegistryService(registryService);
    }

    protected void unsetRegistryService(RegistryService registryService) {
        dataHolder.setRegistryService(null);
    }

    protected void setIndexingService(ContentSearchService contentSearchService){
        dataHolder.setContentSearchService(contentSearchService);
    }

    protected void unsetIndexingService(ContentSearchService contentSearchService){
        dataHolder.setContentSearchService(null);
    }

    protected void setAttributeIndexingService(AttributeSearchService attributeIndexingService) {
        dataHolder.setAttributeIndexingService(attributeIndexingService);
    }

    protected void unsetAttributeIndexingService(AttributeSearchService attributeIndexingService) {
        dataHolder.setAttributeIndexingService(null);
    }

    protected void activate(ComponentContext context) {
        log.info("******* Registry Artifact Search bundle is activated ******* ");
    }

    protected void deactivate(ComponentContext context) {
        log.info("******* Registry Artifact Search bundle is deactivated ******* ");
    }
}
