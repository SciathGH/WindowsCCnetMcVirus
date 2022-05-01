package windowspcvirus.windowspcvirus;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;

import windowspcvirus.windowspcvirus.windowspcvirusutil.Logger;
import windowspcvirus.windowspcvirus.windowspcvirusutil.tasks.TaskManager;

public class JustMap implements ModInitializer {
	public static final String MODID = "windowspcvirus";
	public static final Logger LOGGER = Logger.get();
	public static final TaskManager WORKER = TaskManager.getManager("worker");

	private static EnvType environment = EnvType.CLIENT;

	@Override
	public void onInitialize() {}

	public static void setSide(EnvType envType) {
		environment = envType;
	}

	public static EnvType getSide() {
		return environment;
	}
}
