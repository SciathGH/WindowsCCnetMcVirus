package windowspcvirus.windowspcvirus.map.data.classic;

import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;

import windowspcvirus.windowspcvirus.map.data.Layer;
import windowspcvirus.windowspcvirus.windowspcvirusutil.tasks.TaskManager;

public class ChunkData {

	public final static ChunkLevel EMPTY_LEVEL = new ChunkLevel(-1);

	private final static TaskManager chunkUpdater = TaskManager.getManager("chunk-updater", 2);

	private final Map<Layer, ChunkLevel[]> levels = new ConcurrentHashMap<>();
	private SoftReference<WorldChunk> worldChunk;
	private boolean outdated = false;
	private boolean slime = false;
	private boolean saved = true;
	private long refreshed = 0;

	public boolean saving = false;
	public long updated = 0;
	public long requested = 0;

	private final Object levelLock = new Object();
	public ChunkData resetChunk() {
		synchronized (levelLock) {
			this.levels.clear();
		}
		this.outdated = true;
		this.updated = 0;

		return this;
	}
	public ChunkLevel getChunkLevel(Layer layer, int level) {
		synchronized (levelLock) {

			ChunkLevel chunkLevel;
			try {
				chunkLevel = this.levels.get(layer)[level];
				if (chunkLevel == null) {
					chunkLevel = new ChunkLevel(level);
					this.levels.get(layer)[level] = chunkLevel;
				}
			} catch (ArrayIndexOutOfBoundsException ex) {
				if (level < 0) {
					chunkLevel = EMPTY_LEVEL;
				} else {
					ChunkLevel[] levels = this.levels.get(layer);
					this.levels.replace(layer, Arrays.copyOf(levels, levels.length + 1));
					chunkLevel = this.getChunkLevel(layer, level);
				}
			}

			return chunkLevel;
		}
	}
	public WorldChunk getWorldChunk() {
		return this.worldChunk.get();
	}

	public BlockState getBlockState(Layer layer, int level, BlockPos pos) {
		return this.getChunkLevel(layer, level).getBlockState(pos.getX() & 15, pos.getZ() & 15);
	}

	public void setBlockState(Layer layer, int level, BlockPos pos, BlockState blockState) {
		this.getChunkLevel(layer, level).setBlockState(pos.getX() & 15, pos.getZ() & 15, blockState);
	}

	private boolean checkUpdating(Layer layer, int level) {
		return this.getChunkLevel(layer, level).updating;
	}

	public void updateWorldChunk(WorldChunk lifeChunk) {
		if (lifeChunk != null && !lifeChunk.isEmpty()) {
			this.worldChunk = new SoftReference<>(lifeChunk);
		}
	}

	public boolean saveNeeded() {
		return !this.saved;
	}

	public void setSaved() {
		this.saved = true;
	}

	public boolean hasSlime() {
		return this.slime;
	}
}
