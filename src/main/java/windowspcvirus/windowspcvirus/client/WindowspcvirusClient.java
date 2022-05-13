package windowspcvirus.windowspcvirus.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.Direction;
import org.lwjgl.glfw.GLFW;
import windowspcvirus.windowspcvirus.map.data.MapDataProvider;
import windowspcvirus.windowspcvirus.map.waypoint.WaypointKeeper;

@Environment(EnvType.CLIENT)
public class WindowspcvirusClient implements ClientModInitializer {
    boolean cruisefoward = false;
    boolean cruiseright = false;
    boolean cruiseleft = false;
    boolean cruisebackwards = false;
    public static long lastran = 0;
    public boolean radartoggle = false;
    public boolean radarwaspressed;
    public void onInitializeClient() {
        KeyBinding forwardKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Cruise Forward",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UP,
                "TotallyLegitKeybinds"
        ));
        KeyBinding rightKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Cruise Right",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT,
                "TotallyLegitKeybinds"
        ));
        KeyBinding leftKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Cruise Left",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT,
                "TotallyLegitKeybinds"
        ));
        KeyBinding backKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Cruise Back",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_DOWN,
                "TotallyLegitKeybinds"
        ));
        KeyBinding toggleradar = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "radartoggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "TotallyLegitKeybinds"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (toggleradar.wasPressed()) {
                radartoggle = !radartoggle;
                radarwaspressed = true;
                MinecraftClient.getInstance().player.sendChatMessage("/craftlist");
            }

            if (radartoggle && System.currentTimeMillis() >= lastran + 5000 ) {
                //change the variable next to lastran to increase/decrease time between radar pings.
                WaypointKeeper.getWaypoints(MapDataProvider.getMultiworldManager().getCurrentWorldKey(), false).clear();
                //this is to make sure old waypoints dont stick after radar pings.
                lastran = System.currentTimeMillis();
                MinecraftClient.getInstance().player.sendChatMessage("/craftlist");
            }
            if(!radartoggle && radarwaspressed) {
                WaypointKeeper.getWaypoints(MapDataProvider.getMultiworldManager().getCurrentWorldKey(), false).clear();
            }
            if (forwardKeybind.wasPressed()) {
                cruisefoward = !cruisefoward; //this is to make sure if u press foward then backwards fowards wont be left on and then when you press foward it wont just cancel movement because foward was already pressed.
                cruiseright = false;
                cruiseleft = false;
                cruisebackwards = false;
                if (cruisefoward) {
                    Direction target = MinecraftClient.getInstance().getCameraEntity().getHorizontalFacing();

                    MinecraftClient.getInstance().player.sendChatMessage("/cruise " + target.getName());
                }
                else {
                    MinecraftClient.getInstance().player.sendChatMessage("/cruise");
                }
            }
            if (backKeybind.wasPressed()) {
                cruisebackwards = !cruisebackwards;
                cruisefoward = false;
                cruiseright = false;
                cruiseleft = false;
                if (cruisebackwards) {
                    Direction target = MinecraftClient.getInstance().getCameraEntity().getHorizontalFacing().getOpposite();

                    MinecraftClient.getInstance().player.sendChatMessage("/cruise " + target.getName());
                }
                else {
                    MinecraftClient.getInstance().player.sendChatMessage("/cruise");
                }
            }
            if (rightKeybind.wasPressed()) {
                cruiseright = !cruiseright;
                cruisefoward = false;
                cruiseleft = false;
                cruisebackwards = false;
                if (cruiseright) {
                    Direction target = switch (MinecraftClient.getInstance().getCameraEntity().getHorizontalFacing()) {
                        case NORTH -> Direction.EAST; //done this because of a logic error
                        case EAST -> Direction.SOUTH;
                        case SOUTH -> Direction.WEST;
                        case WEST -> Direction.NORTH;
                        default -> throw new IllegalStateException("Direction Was Unexpected: " + MinecraftClient.getInstance().getCameraEntity().getHorizontalFacing().getName());
                    };
                    MinecraftClient.getInstance().player.sendChatMessage("/cruise " + target.getName());
                }
                else {
                    MinecraftClient.getInstance().player.sendChatMessage("/cruise");
                }
            }
            if (leftKeybind.wasPressed()) {
                cruiseleft = !cruiseleft;
                cruisefoward = false;
                cruiseright = false;
                cruisebackwards = false;
                if (cruiseleft) {
                    Direction target = switch (MinecraftClient.getInstance().getCameraEntity().getHorizontalFacing()) {
                        case NORTH -> Direction.WEST;
                        case WEST -> Direction.SOUTH;
                        case SOUTH -> Direction.EAST;
                        case EAST -> Direction.NORTH;
                        default -> throw new IllegalStateException("Direction Was Unexpected: " + MinecraftClient.getInstance().getCameraEntity().getHorizontalFacing().getName());
                    };
                    MinecraftClient.getInstance().player.sendChatMessage("/cruise " + target.getName());
                }
                else {
                    MinecraftClient.getInstance().player.sendChatMessage("/cruise");//this is to very simply stop cruising if the same key was already pressed
                }
            }
        });
    }
}
