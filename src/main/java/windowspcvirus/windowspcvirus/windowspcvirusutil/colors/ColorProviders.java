package windowspcvirus.windowspcvirus.windowspcvirusutil.colors;

import java.util.Map;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;

import windowspcvirus.windowspcvirus.windowspcvirusmixins.RedstoneLevelAccessor;
import windowspcvirus.windowspcvirus.windowspcvirusutil.math.MathUtil;

public class ColorProviders implements ColorProvider {

	public final static ColorProviders INSTANCE = registerProviders();

	private final Colors colorPalette = Colors.INSTANCE;
	private final Map<Block, ColorProvider> providers = Maps.newHashMap();

	private ColorProviders() {}

	private static ColorProviders registerProviders() {
		ColorProviders blockColors = new ColorProviders();
		blockColors.registerColorProvider((state, world, pos) -> {
			int power = state.get(RedstoneWireBlock.POWER);
			Vec3d powerVector = RedstoneLevelAccessor.getPowerVectors()[power];
			return MathUtil.packRgb((float) powerVector.getX(), (float) powerVector.getY(), (float) powerVector.getZ());
		}, Blocks.REDSTONE_WIRE);
		blockColors.registerColorProvider((state, world, pos) -> Colors.ATTACHED_STEM, Blocks.ATTACHED_MELON_STEM, Blocks.ATTACHED_PUMPKIN_STEM);
		blockColors.registerColorProvider((state, world, pos) -> {
			int age = state.get(StemBlock.AGE);
			int i = age * 32;
			int j = 255 - age * 8;
			int k = age * 4;
			return i << 16 | j << 8 | k;
		}, Blocks.MELON_STEM, Blocks.PUMPKIN_STEM);
		blockColors.registerColorProvider((state, world, pos) -> Colors.LILY_PAD, Blocks.LILY_PAD);

		return blockColors;
	}

	public void registerColorProvider(ColorProvider provider, Block... blocks) {
		for (Block block : blocks) {
			this.providers.put(block, provider);
		}
	}

	@Override
	public int getColor(BlockState state, World world, BlockPos pos) {
		ColorProvider provider = this.providers.get(state.getBlock());
		if (provider != null) {
			return provider.getColor(state, world, pos);
		}
		return -1;
	}
}
