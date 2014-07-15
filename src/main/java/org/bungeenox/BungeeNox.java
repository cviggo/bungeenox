package org.bungeenox;

import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Logger;

public class BungeeNox extends Plugin {

    private Logger logger;

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerListener(this, new BungeeNoxListener(this));
        logger = getLogger();
        logInfo("Enabled");
    }

    public void logInfo(String message){
        logger.info(message);
    }

    public void logWarn(String message){
        logger.warning(message);
    }

    public void logSevere(String message){
        logger.severe(message);
    }
}