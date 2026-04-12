package design.pattern.project.service;

/**
 * Singleton class responsible for managing Azure endpoint connections.
 * Ensures only a single instance exists throughout the application lifecycle.
 */
public class AzureConnectionManager {
    
    private static AzureConnectionManager instance;
    private String azureEndpoint;
    private boolean isConnected;
    
    /**
     * Private constructor to prevent instantiation from outside the class.
     */
    private AzureConnectionManager() {
        this.isConnected = false;
    }
    
    /**
     * Thread-safe method to get the singleton instance.
     * Uses synchronized block to ensure only one instance is created.
     * 
     * @return the singleton instance of AzureConnectionManager
     */
    public static synchronized AzureConnectionManager getInstance() {
        if (instance == null) {
            instance = new AzureConnectionManager();
        }
        return instance;
    }
    
    /**
     * Initializes connection to the Azure endpoint.
     * 
     * @param endpoint the Azure endpoint URL
     */
    public void connect(String endpoint) {
        this.azureEndpoint = endpoint;
        this.isConnected = true;
    }
    
    /**
     * Closes the connection to the Azure endpoint.
     */
    public void disconnect() {
        this.isConnected = false;
        this.azureEndpoint = null;
    }
    
    /**
     * Gets the current Azure endpoint.
     * 
     * @return the Azure endpoint URL
     */
    public String getAzureEndpoint() {
        return this.azureEndpoint;
    }
    
    /**
     * Checks if the connection to Azure endpoint is active.
     * 
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        return this.isConnected;
    }
}
