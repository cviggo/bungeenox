package org.bungeenox;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeNoxListener implements Listener {
    private final BungeeNox bungeeNox;

    public BungeeNoxListener(BungeeNox bungeeNox) {
        this.bungeeNox = bungeeNox;
    }

    @EventHandler
    public void onChatEvent(final ChatEvent event) {
        bungeeNox.logInfo("onChatEvent");
    }

    @EventHandler
    public void onLoginEvent(final LoginEvent event) {
        bungeeNox.logInfo("onLoginEvent");
    }

    @EventHandler
    public void onPermissionCheckEvent(final PermissionCheckEvent event) {
        bungeeNox.logInfo("onPermissionCheckEvent");
    }

    @EventHandler
    public void onPlayerDisconnect(final PlayerDisconnectEvent event) {
        bungeeNox.logInfo("onPlayerDisconnect");
    }

    @EventHandler
    public void onPlayerHandshake(final PlayerHandshakeEvent event) {
        bungeeNox.logInfo("onPlayerHandshake");
    }

    @EventHandler
    public void onPluginMessage(final PluginMessageEvent event) {

        //bungeeNox.logInfo("onPluginMessage: " + event.toString());
    }

    @EventHandler
    public void onPostLogin(final PostLoginEvent event) {
        bungeeNox.logInfo("onPostLogin");
    }

//    @EventHandler
//    public void onPreLogin(final PreLoginEvent event) {
//        bungeeNox.logInfo("onPreLogin");
//    }

    @EventHandler
    public void onProxyPing(final ProxyPingEvent event) {
        bungeeNox.logInfo("onProxyPing");
    }

//    @EventHandler
//    public void onProxyReload(final ProxyReloadEvent event) {
//        bungeeNox.logInfo("onProxyReload");
//    }

    /*
    http://ci.md-5.net/job/BungeeCord/ws/api/target/apidocs/net/md_5/bungee/api/event/ServerConnectedEvent.html

    Not to be confused with ServerConnectEvent, this event is called once a connection to a server is fully operational,
    and is about to hand over control of the session to the player. It is useful if you wish to send information to
    the server before the player logs in.
    */
    @EventHandler
    public void onServerConnected(final ServerConnectedEvent event) {
        bungeeNox.logInfo("onServerConnected");
//        try {
//            Thread.sleep(2000);
//            bungeeNox.logInfo("done waiting");
//        } catch (InterruptedException e) {
//            bungeeNox.logSevere(e.toString());
//        }
        //event.getPlayer().sendMessage(new ComponentBuilder("Welcome to " + event.getServer().getInfo().getName() + "!").color(ChatColor.GREEN).create());
    }

    @EventHandler
    public void onServerConnect(final ServerConnectEvent event) {
        bungeeNox.logInfo("onServerConnect");

        final ProxiedPlayer player = event.getPlayer();

        if (player == null) {
            return;
        }

        final Server server = player.getServer();

        if (server == null) {
            return;
        }

        //event.setCancelled(true);

        try {

            bungeeNox.logInfo("player came from server: " + server.getInfo().getName());

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("BungeeNox");
            out.writeUTF("SavePlayer");
            out.writeUTF(player.getName());

            server.sendData("BungeeCord", out.toByteArray());


            Thread.sleep(1000);

            bungeeNox.logInfo("onServerConnect done waiting");

        } catch (InterruptedException e) {
            bungeeNox.logSevere(e.toString());
        }
    }

//    @EventHandler
//    public void onServerDisconnect(final ServerDisconnectEvent event) {
//        bungeeNox.logInfo("onServerDisconnect");
//    }

    @EventHandler
    public void onServerKick(final ServerKickEvent event) {
        bungeeNox.logInfo("onServerKick");
    }

    @EventHandler
    public void onServerSwitch(final ServerSwitchEvent event) {
        bungeeNox.logInfo("onServerSwitch");
    }

//    @EventHandler
//    public void onTabComplete(final TabCompleteEvent event) {
//        bungeeNox.logInfo("onTabComplete");
//    }

    @EventHandler
    public void onTargeted(final TargetedEvent event) {
        bungeeNox.logInfo("onTargeted");
    }
}