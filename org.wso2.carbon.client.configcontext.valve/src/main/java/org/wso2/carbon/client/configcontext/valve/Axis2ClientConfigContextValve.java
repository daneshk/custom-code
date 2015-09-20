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
package org.wso2.carbon.client.configcontext.valve;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.client.configcontext.provider.Axis2ClientConfigContextProvider;
import org.wso2.carbon.client.configcontext.valve.internal.ConfigContextProviderHolder;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.context.PrivilegedCarbonContext;

public class Axis2ClientConfigContextValve extends ValveBase {
	private static Log log = LogFactory
			.getLog(Axis2ClientConfigContextValve.class);

	@Override
	public void invoke(Request request, Response response) throws IOException,
			ServletException {
		try {
			initAxis2ClientConfigContext();
			getNext().invoke(request, response);
		} catch (Exception e) {
			log.error("Could not handle request: " + request.getRequestURI(), e);
		}
	}

	private void initAxis2ClientConfigContext() {
		Axis2ClientConfigContextProvider contextProvider = (Axis2ClientConfigContextProvider) CarbonContext
				.getThreadLocalCarbonContext().getProperty(
						Axis2ClientConfigContextProvider.AXIS2_CLIENT_CONTEXT_PROVIDER_KEY);
		if (contextProvider == null) {
			contextProvider = ConfigContextProviderHolder.getInstance().getContextProvider();
		}
		PrivilegedCarbonContext.getThreadLocalCarbonContext().setProperty(
				Axis2ClientConfigContextProvider.AXIS2_CLIENT_CONTEXT_PROVIDER_KEY,contextProvider);
	}
}