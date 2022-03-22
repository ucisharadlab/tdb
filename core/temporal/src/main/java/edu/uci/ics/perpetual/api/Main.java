package edu.uci.ics.perpetual.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import edu.uci.ics.perpetual.api.configs.ConfigsModel;
import edu.uci.ics.perpetual.api.utilities.CORSFilter;
import edu.uci.ics.perpetual.api.utilities.ConnectionPool;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.grizzly.http.server.*;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

/**
 * Main class.
 *
 */
public class Main {
    private static String configsURL;
    private static ConfigsModel configs;

    private static HttpServer server;

    // Base URI the Grizzly HTTP server will listen on
    private static String BASE_URI;// = "http://localhost:3000/api";

    // mysql database connection params
    private static String MYSQL_DRIVER;// = "com.mysql.cj.jdbc.Driver";
    private static String DB_URL;// = "jdbc:mysql://localhost/tippers_test?autoReconnect=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=PST";
    private static String DB_USERNAME;// = "cs122b_db107";
    private static String DB_PASSWORD;// = "c^TmIXtuYI4X";
    private static int DB_CONNECTIONS;// = 5

    private static ConnectionPool conPool = null;
    private static boolean ENABLE_LOGGING;


    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("No config file path specified, using 'config.yaml' by default.");
            System.err.println("To specify path of config file, use command line args.");
            System.err.println("e.g.   $ java -jar target/tippers-api.jar config.yaml");
            configsURL = "/Users/peeyush/Code/perpetual-db/temporal/src/main/resources/config4001.yaml";
        }
        else if (args.length > 1) {
            System.err.println("Invalid args.");
            stopServer();
        }
        else
            configsURL = args[0];

        System.out.println("Loading configs from " + configsURL + "...");
        loadConfigs();
        System.out.println("Configs loaded.");

        System.out.println("Initializing connection pool...");
        initConPool();
        System.out.println("Initialized connection pool.");

        System.out.println("Starting server...");
        server = startServer();

        ClassLoader loader = Main.class.getClassLoader();
        CLStaticHttpHandler docsHandler = new CLStaticHttpHandler(loader, configs.getServiceConfig().get("swaggerURL"));
        docsHandler.setFileCacheEnabled(false);
        //StaticHttpHandler docsHandler = new StaticHttpHandler(configs.getServiceConfig().get("swaggerURL"));
        //docsHandler.setFileCacheEnabled(false);

        ServerConfiguration cfg = server.getServerConfiguration();
        cfg.addHttpHandler(docsHandler, "/docs/");
        cfg.addHttpHandler(new HttpHandler() {
            @Override
            public void service(final Request request, final Response response) throws Exception {
                response.sendRedirect("/portal/");
            }
        }, "/");

        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        stopServer();
    }

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */

    private static void loadConfigs () throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        configs = mapper.readValue(new File(configsURL), ConfigsModel.class);

        BASE_URI = configs.getServiceConfig().get("scheme") + configs.getServiceConfig().get("hostName") + ":" +
                configs.getServiceConfig().get("port") + configs.getServiceConfig().get("path");
        MYSQL_DRIVER = configs.getDatabaseConfig().get("dbDriver");
        DB_URL = "jdbc:postgresql://" + configs.getDatabaseConfig().get("dbHostname") + ":" +
                configs.getDatabaseConfig().get("dbPort") +"/" +
                configs.getDatabaseConfig().get("dbName") + configs.getDatabaseConfig().get("dbSettings");
        DB_USERNAME = configs.getDatabaseConfig().get("dbUsername");
        DB_PASSWORD = configs.getDatabaseConfig().get("dbPassword");
        DB_CONNECTIONS = Integer.parseInt(configs.getDatabaseConfig().get("dbConnections"));
        ENABLE_LOGGING = Boolean.parseBoolean(configs.getServiceConfig().get("enableLogging"));
    }


    private static HttpServer startServer() {
        // this is for swagger vv
        //BasicConfigurator.configure();
        String resources = "edu.uci.ics.perpetual.api.resources";
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.1");
        beanConfig.setSchemes(new String[]{"http", "https"});
        beanConfig.setBasePath(configs.getServiceConfig().get("path"));
        beanConfig.setResourcePackage(resources);
        beanConfig.setScan(true);


        // create a resource config that scans for JAX-RS resources and providers
        // in edu.uci.ics.perpetual.api.resources package
        ResourceConfig rc = new ResourceConfig().packages("edu.uci.ics.perpetual.api.resources");
        rc.register(ApiListingResource.class);
        rc.register(SwaggerSerializers.class);
        rc.register(JacksonFeature.class);
        rc.register(JacksonJsonProvider.class);

        // CORS filter
        rc.register(new CORSFilter());

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    private static void initConPool () {
        try {
            conPool = new ConnectionPool(DB_CONNECTIONS, MYSQL_DRIVER, DB_URL, DB_USERNAME, DB_PASSWORD);
        }
        catch (ClassNotFoundException | SQLException | NullPointerException e) {
            System.out.println("Unable to connect to database.");
            e.printStackTrace();
            stopServer();
        }
    }

    private static void stopServer() {
        server.shutdown();
    }

    public static ConnectionPool getConPool () {
        return conPool;
    }

    public static boolean getEnableLogging () { return ENABLE_LOGGING; }
}

