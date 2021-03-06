package org.bungeenox;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class BungeeNoxListener implements Listener {
    private final BungeeNox plugin;

    public BungeeNoxListener(BungeeNox plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChatEvent(final ChatEvent event) {

        final String message = event.getMessage();
        plugin.logInfo(event.getSender().toString() + " : " + message);

        if ("/saveallplayers".equals(message)) {
            final Collection<ProxiedPlayer> proxiedPlayers = plugin.getProxy().getPlayers();

            plugin.logInfo("Saving all players to all servers");

            for (ProxiedPlayer proxiedPlayer : proxiedPlayers) {
                final Server server = proxiedPlayer.getServer();

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("BungeeNox");
                out.writeUTF("SavePlayer");
                out.writeUTF(proxiedPlayer.getName());

                server.sendData("BungeeCord", out.toByteArray());
            }

            plugin.logInfo("Done saving all players to all servers");
        }
    }

    @EventHandler
    public void onLoginEvent(final LoginEvent event) {
        plugin.logInfo("onLoginEvent");
    }

    @EventHandler
    public void onPermissionCheckEvent(final PermissionCheckEvent event) {
        //plugin.logInfo("onPermissionCheckEvent: " + event.getPermission());

    }

    @EventHandler
    public void onPlayerDisconnect(final PlayerDisconnectEvent event) {
        plugin.logInfo("onPlayerDisconnect");
    }

    @EventHandler
    public void onPlayerHandshake(final PlayerHandshakeEvent event) {
        plugin.logInfo("onPlayerHandshake");
    }

    @EventHandler
    public void onPluginMessage(final PluginMessageEvent event) {

        //plugin.logInfo("onPluginMessage: " + event.getTag());

        //final String tag = event.getTag();
//        if ("CC".equals(tag)
//                || "MEK".equals(tag)
//                || "AE".equals(tag)
//                || "BC".equals(tag)
//                || "OpenMods".equals(tag)
//                || "EnderIO".equals(tag)
//                || "gravisuite".equals(tag)
//                || "OpenMods|s".equals(tag)
//                || "BC".equals(tag)
//
//        )
//        {
//            return;
//        }

//        if (!tag.toLowerCase().contains("cloud")) {
//            return;
//        }
//
//        final byte[] message = event.getData();
//
//        if (message == null) {
//            return;
//        }
//
//        ByteArrayDataInput in = ByteStreams.newDataInput(message);
//
//        if (in == null) {
//            return;
//        }
//
//        String part = null;
//
//        while (true) {
//            try {
//                part += in.readUTF() + ", ";
//                break;
//
//            } catch (Throwable t) {
//                break;
//            }
//        }
//
//        if (part != null) {
//            plugin.logInfo("-----PluginMsg:" + tag + ": " + part);
//        }

    }

    @EventHandler
    public void onPostLogin(final PostLoginEvent event) {
        plugin.logInfo("onPostLogin");

        //event.getPlayer().disconnect("World server reboot in progress. Hang on :) - it will only take a few minutes");
    }

//    @EventHandler
//    public void onPreLogin(final PreLoginEvent event) {
//        plugin.logInfo("onPreLogin");
//    }

    @EventHandler
    public void onProxyPing(final ProxyPingEvent event) {
        plugin.logInfo("onProxyPing");
    }

//    @EventHandler
//    public void onProxyReload(final ProxyReloadEvent event) {
//        plugin.logInfo("onProxyReload");
//    }

    /*
    http://ci.md-5.net/job/BungeeCord/ws/api/target/apidocs/net/md_5/bungee/api/event/ServerConnectedEvent.html

    Not to be confused with ServerConnectEvent, this event is called once a connection to a server is fully operational,
    and is about to hand over control of the session to the player. It is useful if you wish to send information to
    the server before the player logs in.
    */
    @EventHandler
    public void onServerConnected(final ServerConnectedEvent event) {
        plugin.logInfo("onServerConnected");
//        try {
//            Thread.sleep(2000);
//            plugin.logInfo("done waiting");
//        } catch (InterruptedException e) {
//            plugin.logSevere(e.toString());
//        }
        //event.getPlayer().sendMessage(new ComponentBuilder("Welcome to " + event.getServer().getInfo().getName() + "!").color(ChatColor.GREEN).create());
    }

    ConcurrentMap<String, Object> delayedWarps = new ConcurrentHashMap<String, Object>();

    @EventHandler
    public void onServerConnect(final ServerConnectEvent event) {
        plugin.logInfo("onServerConnect");

        final ProxiedPlayer player = event.getPlayer();

        if (player == null) {
            return;
        }

        final Server server = player.getServer();

        if (server == null) {
            return;
        }

        if (server.getInfo().getName().toLowerCase().equals(event.getTarget().getName().toLowerCase())){
            return;
        }

        try {


            String key = server.getInfo().getName().toLowerCase() + player.getName().toLowerCase() + event.getTarget().getName().toLowerCase();

            plugin.logInfo(key);

            if(delayedWarps.containsKey(key)){
                //plugin.logInfo("removed key: " + key);
                delayedWarps.remove(key);
                return;
            }

            //plugin.logInfo("adding key: " + key);
            delayedWarps.put(key, key);

            //plugin.logInfo("player came from server: " + server.getInfo().getName());

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("BungeeNox");
            out.writeUTF("SavePlayer");
            out.writeUTF(player.getName());

            server.sendData("BungeeCord", out.toByteArray());

            event.setCancelled(true);

            ByteArrayDataOutput out2 = ByteStreams.newDataOutput();
            out2.writeUTF("BungeeNox");
            out2.writeUTF("WarpingPlayer");
            out2.writeUTF(player.getName());
            out2.writeUTF(event.getTarget().getName());

            server.sendData("BungeeCord", out.toByteArray());

            player.sendMessage("Warping to " + event.getTarget().getName() + " in 5 seconds.");

            plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
                        @Override
                        public void run() {
                            plugin.logInfo("Connecting player " + player.getName() + " to server " + event.getTarget().getName() );
                            player.connect(event.getTarget());
                        }
                    },
                    5,
                    TimeUnit.SECONDS
            );

            //Thread.sleep(5000);

            //bungeeNox.logInfo("onServerConnect done waiting");

        } catch (Throwable t) {
            //String str = ExceptionUtils.getStackTrace(t);
            plugin.logSevere(t.toString());
            t.printStackTrace();
        }
    }

//    @EventHandler
//    public void onServerDisconnect(final ServerDisconnectEvent event) {
//        plugin.logInfo("onServerDisconnect");
//    }

    @EventHandler
    public void onServerKick(final ServerKickEvent event) {
        plugin.logInfo("onServerKick");
    }

    @EventHandler
    public void onServerSwitch(final ServerSwitchEvent event) {
        plugin.logInfo("onServerSwitch");
    }

//    @EventHandler
//    public void onTabComplete(final TabCompleteEvent event) {
//        plugin.logInfo("onTabComplete");
//    }

    @EventHandler
    public void onTargeted(final TargetedEvent event) {
        plugin.logInfo("onTargeted");
    }
}