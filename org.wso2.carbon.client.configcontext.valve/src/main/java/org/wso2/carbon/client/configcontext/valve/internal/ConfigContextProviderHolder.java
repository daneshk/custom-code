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

import org.wso2.carbon.client.configcontext.provider.Axis2ClientConfigContextProvider;

public class ConfigContextProviderHolder {
    private static ConfigContextProviderHolder instance = new ConfigContextProviderHolder();
    
    /**
     * The name of the property that stores a reference to the axis2 configuration context provider instance.
     */ 
    private Axis2ClientConfigContextProvider contextProvider;
    
    public static ConfigContextProviderHolder getInstance(){
    	return instance;
    }

	public Axis2ClientConfigContextProvider getContextProvider() {
		return contextProvider;
	}

	public void setContextProvider(Axis2ClientConfigContextProvider contextProvider) {
		this.contextProvider = contextProvider;
	}
    
}
