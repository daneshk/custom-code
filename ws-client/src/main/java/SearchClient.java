/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.wso2.carbon.base.ServerConfiguration;
import org.wso2.carbon.governance.api.generic.GenericArtifactManager;
import org.wso2.carbon.governance.api.generic.dataobjects.GenericArtifact;
import org.wso2.carbon.governance.api.util.GovernanceUtils;
import org.wso2.carbon.governance.client.WSRegistrySearchClient;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.pagination.PaginationContext;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;

import javax.xml.namespace.QName;
import java.io.File;
import java.util.*;

public class SearchClient {

    private static ConfigurationContext configContext = null;

    private static final String CARBON_HOME = "/home/daneshk/wso2greg-4.6.0";
    private static final String axis2Repo = CARBON_HOME + File.separator + "repository" +
            File.separator + "deployment" + File.separator + "client";
    private static final String axis2Conf =
            ServerConfiguration.getInstance().getFirstProperty("Axis2Config.clientAxis2XmlLocation");
    private static final String username = "admin";
    private static final String password = "admin";
    private static final String serverURL = "https://localhost:9443/services/";

    private static WSRegistryServiceClient initialize() throws Exception {

        System.setProperty("javax.net.ssl.trustStore", CARBON_HOME + File.separator + "repository" +
                File.separator + "resources" + File.separator + "security" + File.separator +
                "wso2carbon.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        System.setProperty("carbon.repo.write.mode", "true");
        configContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                axis2Repo, axis2Conf);
        return new WSRegistryServiceClient(serverURL, username, password, configContext);
    }


    public static void main(String[] args) throws Exception {
        try {

            final Registry registry = initialize();
            Registry gov = GovernanceUtils.getGovernanceUserRegistry(registry, "admin");
            // Should be load the governance artifact.
            GovernanceUtils.loadGovernanceArtifacts((UserRegistry) gov, GovernanceUtils.findGovernanceArtifactConfigurations(gov));
            //Initialize the pagination context.
            //Top five services, sortBy name , and sort order descending.
            PaginationContext.init(0, 5, "DES", "overview_name", 100);
            WSRegistrySearchClient wsRegistrySearchClient =
                    new WSRegistrySearchClient(serverURL, username, password, configContext);
            //This should be execute to initialize the AttributeSearchService.
            wsRegistrySearchClient.init();
            //Initialize the GenericArtifactManager
            GenericArtifactManager artifactManager =
                    new GenericArtifactManager(gov, "service");
            Map<String, List<String>> listMap = new HashMap<String, List<String>>();
            // Create the search attribute map
            listMap.put("overview_name", new ArrayList<String>() {
                {
                    add("FlightService7");//holidays //FlightService7
                }
            });
            listMap.put("overview_version", new ArrayList<String>() {
                {
                    add("1.0.0-SNAPSHOT"); //1.0.0-SNAPSHOT
                }
            });
            //Find the results.
            System.out.println("\n\nSearch services having ServiceLifeCycle and development state ...\n");
            System.out.println("Get top five services ..\n");
            System.out.println("Sort by  service name ..\n");
            System.out.println("Sort order descending ..\n\n");
            Date time1 = Calendar.getInstance().getTime();
            GenericArtifact[] genericArtifacts = artifactManager.findGenericArtifacts(listMap);
            Date time2 = Calendar.getInstance().getTime();
            System.out.println("Time for the search result by artifactManager: "+(time2.getTime()-time1.getTime()));

            if (genericArtifacts.length == 0) {
                System.out.println("No results found ..");
            }

            System.out.println("\nResults found ... \n");
            for (GenericArtifact artifact : genericArtifacts) {
                System.out.println(artifact.getPath());
            }

        } finally {
            PaginationContext.destroy();
        }
        System.exit(1);

    }

    private static void addServices(Registry govRegistry) throws RegistryException {
        GenericArtifactManager artifactManager = new GenericArtifactManager(govRegistry, "service");
        System.out.println("#############################################\n");
        for (int i = 1; i < 10; i++) {
            System.out.println("Adding FlightService" + i + " ....");
            GenericArtifact artifact = artifactManager.newGovernanceArtifact(new QName("ns", "FlightService" + i));
            artifactManager.addGenericArtifact(artifact);
        }
        //Services need to be index before search.
        try {
            System.out.println("\n.....Waiting to index services .....!");
            Thread.sleep(2 * 60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
