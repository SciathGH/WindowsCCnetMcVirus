package windowspcvirus.windowspcvirus.windowspcvirusmixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import windowspcvirus.windowspcvirus.map.data.MapDataProvider;
import windowspcvirus.windowspcvirus.map.multiworld.WorldKey;
import windowspcvirus.windowspcvirus.map.waypoint.Waypoint;
import windowspcvirus.windowspcvirus.map.waypoint.WaypointKeeper;
import windowspcvirus.windowspcvirus.windowspcvirusutil.colors.Colors;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    @Shadow @Final private MinecraftClient client;

    @Shadow public abstract void addMessage(Text message);

    public int shipCoordinatesnumberX = 0;
    public int shipCoordinatesnumberY = 0;
    public int shipCoordinatesnumberZ = 0;
    public double angletotarget = 0.0;
    public double playerangle = MinecraftClient.getInstance().getCameraEntity().getHeadYaw();
    public int distancetotarget = 0;
    public String playername = MinecraftClient.getInstance().player.getName().asString();
    private String format;

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"))
    public void wewillnevercallthisfunction(Text message, CallbackInfo info) {
        String rawMessage = message.getString();
        String shipdetection;
        if (rawMessage.contains("• A")) {
            if (rawMessage.contains("friendly")) {
                format = message.getStyle().toString();
                shipdetection =  rawMessage.replaceAll(",", "");
                String[] coords = shipdetection.split(" ");
                shipCoordinatesnumberX = Integer.valueOf(coords[6]);
                shipCoordinatesnumberY = Integer.valueOf(coords[7]);
                shipCoordinatesnumberZ = Integer.valueOf(coords[8]);
                shipdetected(MapDataProvider.getMultiworldManager().getCurrentWorldKey(), shipdetectedblockposition());
            }
            else if(rawMessage.contains("neutral")){
                format = message.getStyle().toString();
                shipdetection = rawMessage.replaceAll(",", "");
                String[] coords = shipdetection.split(" ");
                shipCoordinatesnumberX = Integer.valueOf(coords[6]);
                shipCoordinatesnumberY = Integer.valueOf(coords[7]);
                shipCoordinatesnumberZ = Integer.valueOf(coords[8]);
                shipdetected(MapDataProvider.getMultiworldManager().getCurrentWorldKey(), shipdetectedblockposition());
            }
        }
        if (rawMessage.contains("• Your")){
            format = message.getStyle().toString();
            shipdetection = rawMessage.replaceAll(",", "");
            String[] coords = shipdetection.split(" ");
            shipCoordinatesnumberX = Integer.valueOf(coords[5]);
            shipCoordinatesnumberY = Integer.valueOf(coords[6]);
            shipCoordinatesnumberZ = Integer.valueOf(coords[7]);
            //shipdetected(MapDataProvider.getMultiworldManager().getCurrentWorldKey(), shipdetectedblockposition());
           }
        if (rawMessage.contains("• An")){
            if (rawMessage.contains("allied")){
                format = message.getStyle().toString();
                shipdetection = rawMessage.replaceAll(",", "");
                String[] coords = shipdetection.split(" ");
                shipCoordinatesnumberX = Integer.valueOf(coords[6]);
                shipCoordinatesnumberY = Integer.valueOf(coords[7]);
                shipCoordinatesnumberZ = Integer.valueOf(coords[8]);
                shipdetected(MapDataProvider.getMultiworldManager().getCurrentWorldKey(), shipdetectedblockposition());
                }
            if (rawMessage.contains("enemy")){
                format = message.getStyle().toString();
                shipdetection = rawMessage.replaceAll(",", "");
                String[] coords = shipdetection.split(" ");
                shipCoordinatesnumberX = Integer.valueOf(coords[6]);
                shipCoordinatesnumberY = Integer.valueOf(coords[7]);
                shipCoordinatesnumberZ = Integer.valueOf(coords[8]);
                shipdetected(MapDataProvider.getMultiworldManager().getCurrentWorldKey(), shipdetectedblockposition());
                }
            }
        }
        public BlockPos shipdetectedblockposition () {
            int posX = this.shipCoordinatesnumberX;
            int posZ = this.shipCoordinatesnumberZ;
            int posY = this.shipCoordinatesnumberY;

            return new BlockPos(posX, posY, posZ);
        }
    public void shipdetected(WorldKey world, BlockPos pos) {
        Waypoint waypoint = new Waypoint();
        waypoint.world = world;
        waypoint.name = "shipdetected";
        if(format.contains("color=red"))waypoint.setIcon(Waypoint.getIcon(Waypoint.Icons.DIAMOND_RED), Colors.RED);        //enemy
        if(format.contains("color=#26A9EB"))waypoint.setIcon(Waypoint.getIcon(Waypoint.Icons.DIAMOND_BLUE), Colors.BLUE);  //ally
        if(format.contains("color=white"))waypoint.setIcon(Waypoint.getIcon(Waypoint.Icons.DIAMOND_WHITE), Colors.WHITE);  //neutral
        if(format.contains("color=#85EB26"))waypoint.setIcon(Waypoint.getIcon(Waypoint.Icons.DIAMOND_GREEN), Colors.GREEN);//nation
        waypoint.pos = pos;
        if (format.contains("color=red")) waypoint.color = Colors.RED;       //enemy
        if (format.contains("color=#26A9EB"))  waypoint.color = Colors.CYAN;  //ally
        if (format.contains("color=white")) waypoint.color = Colors.WHITE;   //neutral
        if (format.contains("color=#85EB26")) waypoint.color = Colors.GREEN; //nation
        WaypointKeeper.getInstance().addNew(waypoint);
        WaypointKeeper.getInstance().saveWaypoints();
    }
}