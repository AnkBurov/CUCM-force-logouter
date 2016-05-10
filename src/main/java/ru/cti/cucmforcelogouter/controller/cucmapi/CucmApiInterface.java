package ru.cti.cucmforcelogouter.controller.cucmapi;

public interface CucmApiInterface {

    /**
     * Send logout message to CUCM
     * 0 - successful logout
     * 1 - already logged out
     * 2 - exception
     */
    public int sendLogout(String deviceName);
}
