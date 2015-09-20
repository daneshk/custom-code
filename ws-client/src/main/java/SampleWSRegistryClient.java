import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.wso2.carbon.governance.api.common.dataobjects.GovernanceArtifact;
import org.wso2.carbon.governance.api.generic.GenericArtifactManager;
import org.wso2.carbon.governance.api.generic.dataobjects.GenericArtifact;
import org.wso2.carbon.governance.api.schema.SchemaManager;
import org.wso2.carbon.governance.api.schema.dataobjects.Schema;
import org.wso2.carbon.governance.api.util.GovernanceUtils;
import org.wso2.carbon.governance.api.wsdls.WsdlManager;
import org.wso2.carbon.governance.api.wsdls.dataobjects.Wsdl;
import org.wso2.carbon.governance.client.WSRegistrySearchClient;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.pagination.PaginationContext;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SampleWSRegistryClient {

    private static final String CARBON_HOME = "/home/daneshk/wso2greg-4.6.0";

    private static final String axis2Repo = CARBON_HOME + File.separator +"repository" +
            File.separator + "deployment" + File.separator + "client";
    private static final String axis2Conf = CARBON_HOME + "/repository/conf/axis2/axis2_client.xml";
    private static final String username = "admin";
    private static final String password = "admin";
    private static final String serverURL = "https://localhost:9443/services/";

    private static Registry wsRegistry = null;
    private static Registry governance = null;
    public static GenericArtifactManager artifactManager;

    private static void initialize() throws Exception {

        System.setProperty("javax.net.ssl.trustStore", CARBON_HOME + File.separator + "repository" +
                File.separator + "resources" + File.separator + "security" + File.separator +
                "wso2carbon.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        System.setProperty("carbon.repo.write.mode", "true");
        ConfigurationContext configContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                axis2Repo, axis2Conf);
        wsRegistry =  new WSRegistryServiceClient(serverURL, username, password, configContext);
        governance = GovernanceUtils.getGovernanceUserRegistry(wsRegistry, username);
        // Should be load the governance artifact.
        GovernanceUtils.loadGovernanceArtifacts((UserRegistry) governance, GovernanceUtils.findGovernanceArtifactConfigurations(governance));
        artifactManager = new GenericArtifactManager(governance, "service");


        WSRegistrySearchClient wsRegistrySearchClient = new WSRegistrySearchClient(serverURL, username, password, configContext);
//        boolean authenticate = wsRegistrySearchClient.authenticate(configContext, serverURL, username, password);
        //This should be execute to initialize the AttributeSearchService.
        wsRegistrySearchClient.init();
    }

    public static void main(String[] args) throws Exception {
        initialize();
        try {
/*            Resource resource = wsRegistry.newResource();
            resource.setContent("Hello Out there!");

            String resourcePath = "/_system/conf/foo/abc";
            wsRegistry.put(resourcePath, resource);

            System.out.println("A resource added to: " + resourcePath);

            wsRegistry.rateResource(resourcePath, 3);

            System.out.println("Resource rated with 3 stars!");
            Comment comment = new Comment();
            comment.setText("Wow! A comment out there");
            wsRegistry.addComment(resourcePath, comment);
            System.out.println("Comment added to resource");

            Resource getResource = wsRegistry.get("/_system/conf/foo/abc");
            System.out.println("Resource retrived");
            System.out.println("Printing retrieved resource content: " +
                    new String((byte[])getResource.getContent()));*/

/*
            addResources();
*/
            String [] resultArray = returnServices("flightservice1","1.0.0-SNAPSHOT");
            System.out.println("resultArray size: "+resultArray.length);
        } finally {
            //Close the session
            ((WSRegistryServiceClient)wsRegistry).logut();
        }
        System.exit(0);

    }


/*    private static void addResources() throws IOException, RegistryException {
        addWSDL();
        addSchema();
    }*/

/*    private static void addWSDL()
            throws IOException, RegistryException {

        WsdlManager wsdlManager = new WsdlManager(governance);
        Wsdl wsdl;
        String wsdlFilePath = ClassLoader.getSystemResource("AmazonWebServices.wsdl").getPath();
        wsdl = wsdlManager.newWsdl(readFile(wsdlFilePath).getBytes(), "AmazonWebServices.wsdl");
        wsdlManager.addWsdl(wsdl);
        wsdl = wsdlManager.getWsdl(wsdl.getId());
        Resource resource = governance.get(wsdl.getPath());
        resource.addProperty("wsdlProperty", "10");
        governance.put(wsdl.getPath(), resource);
    }*/

/*    private static void addSchema() throws IOException, RegistryException {

        SchemaManager schemaManager = new SchemaManager(governance);
        String schemaFilePath = ClassLoader.getSystemResource("library.xsd").getPath();
        Schema schema = schemaManager.newSchema(readFile(schemaFilePath).getBytes(), "library.xsd");
        schemaManager.addSchema(schema);
        schema = schemaManager.getSchema(schema.getId());
        Resource resource = governance.get(schema.getPath());
        resource.addProperty("wsdlProperty", "20");
        governance.put(schema.getPath(), resource);
    }*/

/*    public static String readFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        String line;
        while((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }

        reader.close();
        return stringBuilder.toString();
    }*/

    public static String[]
    returnServices(final String serviceName, final String version)
            throws Exception {

        List<String> endpoints = new LinkedList<String>();

        try {

            Map<String, List<String>> listMap = new HashMap<String, List<String>>();
            // Create the search attribute map
            listMap.put("overview_name", new ArrayList<String>() {
                {
                    add(serviceName);//holidays //FlightService7
                }
            });
              listMap.put("overview_version", new ArrayList<String>() {
                {
                    add(version); //1.0.0-SNAPSHOT
                }
            });
/*            listMap.put("lcName", new ArrayList<String>() {{
                add("ServiceLifeCycle");
            }});
            listMap.put("lcState", new ArrayList<String>() {{
                add("Development");
            }});*/
/*            PaginationContext.init(0, 10, "DES", "overview_name", 100);
            String mediaType = "application/vnd.wso2-service+xml";

            Date time1 = Calendar.getInstance().getTime();
            List<GovernanceArtifact> governanceArtifacts = GovernanceUtils.findGovernanceArtifacts(listMap,governance,mediaType);
            Date time2 = Calendar.getInstance().getTime();
            System.out.println("Time for the search result GovernanceUtils: "+(time2.getTime()-time1.getTime()));*/

            Date time3 = Calendar.getInstance().getTime();
            PaginationContext.init(0, 10, "DES", "overview_name", 100);
            GenericArtifact[] genericArtifacts = artifactManager.findGenericArtifacts(listMap);
            Date time4 = Calendar.getInstance().getTime();
            System.out.println("Time for the search result by artifactManager: "+(time4.getTime()-time3.getTime()));

/*                    artifactManager.findGenericArtifacts(listMap);*/

            for (GovernanceArtifact artifact : genericArtifacts) {
                endpoints.add(artifact.getAttribute("overview_name") +", Version " +artifact.getAttribute("overview_version"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return endpoints.toArray(new String[endpoints.size()]);
    }
}
