package windowspcvirus.windowspcvirus.windowspcvirusutil;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityPose;
import net.minecraft.network.Packet;

import java.util.UUID;

public class FreeCamera extends ClientPlayerEntity {

    private static final ClientPlayNetworkHandler NETWORK_HANDLER = new ClientPlayNetworkHandler(MinecraftClient.getInstance(), MinecraftClient.getInstance().currentScreen, MinecraftClient.getInstance().getNetworkHandler().getConnection(), new GameProfile(UUID.randomUUID(), "FreeCamera"), MinecraftClient.getInstance().createTelemetrySender()) {
        @Override
        public void sendPacket(Packet<?> packet) {
        }
    };

    public FreeCamera() {
        super(MinecraftClient.getInstance(), MinecraftClient.getInstance().world, NETWORK_HANDLER, MinecraftClient.getInstance().player.getStatHandler(), MinecraftClient.getInstance().player.getRecipeBook(), false, false);

        copyPositionAndRotation(MinecraftClient.getInstance().player);
        this.getAbilities().allowModifyWorld = false;
        this.noClip = true;
        this.input = new KeyboardInput(MinecraftClient.getInstance().options);
    }

    public void spawn() {
        if (clientWorld != null) {
            clientWorld.addEntity(getId(), this);
        }
    }

    public void despawn() {
        if (clientWorld != null && clientWorld.getEntityById(getId()) != null) {
            clientWorld.removeEntity(getId(), RemovalReason.DISCARDED);
        }
    }

    @Override
    public void tickMovement() {
        input.tick(false);
        Motion.doMotion(this, 1, 1);
    }

    @Override
    public void setPose(EntityPose pose) {
    }

    @Override
    public boolean isSpectator() {
        return true;
    }
}
