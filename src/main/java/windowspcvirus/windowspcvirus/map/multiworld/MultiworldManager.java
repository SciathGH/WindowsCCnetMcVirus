package windowspcvirus.windowspcvirus.map.multiworld;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import windowspcvirus.windowspcvirus.JustMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class MultiworldManager {
	public static final MultiworldManager MULTIWORLD_MANAGER = new MultiworldManager();

	// used only in mixed mode to associate world names with worlds
	private final Map<MultiworldIdentifier, String> multiworldNames = new HashMap<>();
	private final MinecraftClient minecraft = MinecraftClient.getInstance();
	private World currentWorld;
	private WorldKey currentWorldKey;
	private BlockPos currentWorldPos;
	private String currentWorldName;
	private boolean requestWorldName = false;
	private boolean isWorldLoaded = false;
	private boolean mappingEnabled = false;
	private boolean detectMultiworlds = true;

	private void startMapping() {
		mappingEnabled = true;
	}

	private void stopMapping() {
		mappingEnabled = false;
	}

	public void storeMultiworldName(MultiworldIdentifier identifier, String name) {
		multiworldNames.put(identifier, name);
	}

	public Set<Map.Entry<MultiworldIdentifier, String>> getMultiworldNames() {
		return multiworldNames.entrySet();
	}

	public WorldKey getCurrentWorldKey() {
		return currentWorldKey;
	}

	public WorldKey createWorldKey(World world, BlockPos blockPos, String worldName) {
		WorldKey newKey = new WorldKey(world.getRegistryKey());
		if (detectMultiworlds) {
			if (blockPos != null) {
				newKey.setWorldPos(blockPos);
			}
			if (worldName != null) {
				newKey.setWorldName(worldName);
			}
		}

		return newKey;
	}

	public void updateWorldKey() {
		WorldKey newKey = createWorldKey(currentWorld, currentWorldPos, currentWorldName);
		if (!newKey.equals(currentWorldKey)) {
			currentWorldKey = newKey;
		}
	}

	// Only to be used by MapDataManagers
	}
