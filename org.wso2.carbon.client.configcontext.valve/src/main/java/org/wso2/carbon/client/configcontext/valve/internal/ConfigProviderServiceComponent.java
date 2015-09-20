/*
 *  Copyright (c) 2005-2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.client.configcontext.valve.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.client.configcontext.provider.Axis2ClientConfigContextProvider;

/**
 * @scr.component name="configProviderServiceComponent" immediate="true"
 * @scr.reference name="config.context.service"
 *                interface="org.wso2.carbon.client.configcontext.provider.Axis2ClientConfigContextProvider"
 *                cardinality="1..1" policy="dynamic"
 *                bind="setProviderService"
 *                unbind="unsetProviderService"
*/
public class ConfigProviderServiceComponent {
	private static Log log = LogFactory
			.getLog(ConfigProviderServiceComponent.class);
	private ConfigContextProviderHolder configProviderHolder = ConfigContextProviderHolder.getInstance();

    protected void activate(ComponentContext componentContext) {
    	log.debug("Client Configuration Context Provider Service Activated");
    }

    protected void deactivate(ComponentContext componentContext) {
    	log.debug("Client Configuration Context Provider Service Deactivated");
    }
    
    protected void setProviderService(Axis2ClientConfigContextProvider contextProvider) {
    	configProviderHolder.setContextProvider(contextProvider);
    }

    protected void unsetProviderService(Axis2ClientConfigContextProvider contextProvider) {
    	configProviderHolder.setContextProvider(null);
    }
}
