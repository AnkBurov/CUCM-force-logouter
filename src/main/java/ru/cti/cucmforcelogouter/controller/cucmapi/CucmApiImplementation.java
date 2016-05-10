package ru.cti.cucmforcelogouter.controller.cucmapi;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class CucmApiImplementation implements CucmApiInterface {
    private String serverURL;
    private String login;
    private String password;

    public CucmApiImplementation(String serverURL, String login, String password) {
        this.serverURL = serverURL;
        this.login = login;
        this.password = password;
    }

    private static final Logger logger = LogManager.getLogger(CucmApiImplementation.class);

    private void disableSslVerification() throws KeyManagementException, NoSuchAlgorithmException {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };
        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

    /**
     * Send logout message to CUCM
     * 0 - successful logout
     * 1 - already logged out
     * 2 - exception
     *
     * @param deviceName
     */
    @Override
    public int sendLogout(String deviceName) {
        int result = 1;
        try {
            disableSslVerification();
            //Check if Cisco IP Communicator is running
            //E/M API service URL on UC Manager host ds-ucm851.cisco.com
            //Note this sample assumes the certificate for the host with subject
            //name 'ds-ucm851.cisco.com' has been imported into the Java keystore
            //To test with insecure connection use the URL as http://ds-ucm851.cisco.com:8080/emservice/EMServiceServlet
            URL url = new URL(serverURL);

            //Create a java.net URLConnection object to make the HTTP request
            URLConnection conn = url.openConnection();
            //setDoOutput=true causes the URLConnection to perform a POST operation
            conn.setDoOutput(true);
            //The request body will be in HTTP form encoded format
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            //Build a string containing the contents of the E/M API XML request
            String EMRequest = "<request> \n" +
                    "    <appInfo> \n" +
                    "        <appID>" + login + "</appID> \n" +
                    "        <appCertificate>" + password + "</appCertificate> \n" +
                    "    </appInfo> \n" +
                    "    <logout> \n" +
                    "        <deviceName>" + deviceName + "</deviceName>\n" +
                    "    </logout>\n" +
                    "</request>";
            //URL encode/escape the request
            EMRequest = URLEncoder.encode(EMRequest, "UTF-8");
            //Build the complete HTTP form request body
            EMRequest = "xml=" + EMRequest;

            //Create an OutputStreamWriter for the URLConnection object and make the request
            OutputStreamWriter writer = null;
            BufferedReader reader = null;
            try {
                writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(EMRequest);
                logger.info("Logout message of phone " + deviceName + " has been send");
                writer.flush();

                //Read the response
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                //Output the response
                String line;
                while ((line = reader.readLine()) != null) {
                    // log response
                    if (line.contains("success")) {
                        logger.info("Phone " + deviceName + " has been logged out");
                        result = 0;
                    }
                    if (line.contains("Application Authentication Error")) {
                        logger.error("CUCM Authentication Error");
                        throw new RuntimeException("CUCM Authentication Error");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                logger.catching(e);
                result = 2;
            } finally {
                //Cleanup the stream objects
                writer.close();
                reader.close();
            }
        } catch (Exception e) {
            logger.catching(e);
            e.printStackTrace();
        }
        return result;
    }
}