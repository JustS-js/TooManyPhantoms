package net.just_s.tmp.mixin;

import net.just_s.tmp.TMPMod;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.spawner.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnMixin {
	private int tick;

	@Inject(at = @At("HEAD"), method = "spawn", cancellable = true)
	private void tick(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals, CallbackInfoReturnable<Integer> cir) {
		if (!spawnMonsters) {
			cir.setReturnValue(0);
		}
		if (TMPMod.CONFIG.doInsomnia) {
			--this.tick;
			if (this.tick <= 0) {
				this.tick += 20 * (TMPMod.CONFIG.insomniaMinCycleTime + world.random.nextInt(TMPMod.CONFIG.insomniaRandomizationTime));
				if (!world.isDay()) {
					cir.setReturnValue(customSpawn(world));
				}
			}
		}
		cir.setReturnValue(0);
	}

	private int customSpawn(ServerWorld world) {
		Random random = world.random;
		int phantomsSpawned = 0;

		for (ServerPlayerEntity player : world.getPlayers()) {
			if (!player.isCreative() && !player.isSpectator()) {
				ServerStatHandler stats = player.getStatHandler();
				int noRest = Math.max(1, Math.min(2147483647, stats.getStat(Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST))));
				if (random.nextInt(noRest) > TMPMod.CONFIG.insomniaSpawnStartTimer * 20) {
					BlockPos bpos = player.getBlockPos();
					BlockPos worldSpawn = world.getSpawnPos();
					boolean isInRadius = bpos.getX() > (worldSpawn.getX() - TMPMod.CONFIG.phantomFreeArea) &&
							bpos.getX() < (worldSpawn.getX() + TMPMod.CONFIG.phantomFreeArea) &&
							bpos.getZ() > (worldSpawn.getZ() - TMPMod.CONFIG.phantomFreeArea) &&
							bpos.getZ() < (worldSpawn.getZ() + TMPMod.CONFIG.phantomFreeArea);
					if (
							bpos.getY() >= world.getSeaLevel() &&
							world.isSkyVisible(bpos) &&
							TMPMod.CONFIG.insomniaLightStopsPhantoms >= world.getLightLevel(bpos) &&
							!isInRadius
					) {
						BlockPos spawnPos;
						BlockState blockState;
						FluidState fluidState;
						do {
							spawnPos = bpos.up(20 + random.nextInt(15)).east(-10 + random.nextInt(21)).south(-10 + random.nextInt(21));
							blockState = world.getBlockState(spawnPos);
							fluidState = world.getFluidState(spawnPos);
						} while (!SpawnHelper.isClearForSpawn(world, spawnPos, blockState, fluidState, EntityType.PHANTOM));
						EntityData entityData = null;
						LocalDifficulty localDifficulty = world.getLocalDifficulty(bpos);
						int amountOfPhantoms = TMPMod.CONFIG.minAmountPerSpawn + random.nextInt(localDifficulty.getGlobalDifficulty().getId() + 1);
						amountOfPhantoms = MathHelper.clamp(amountOfPhantoms, TMPMod.CONFIG.minAmountPerSpawn, TMPMod.CONFIG.maxAmountPerSpawn);
						for (int m = 0; m < amountOfPhantoms; ++m) {
							PhantomEntity phantomEntity = EntityType.PHANTOM.create(world);
							phantomEntity.refreshPositionAndAngles(spawnPos, 0.0F, 0.0F);
							entityData = phantomEntity.initialize(world, localDifficulty, SpawnReason.NATURAL, entityData, (NbtCompound)null);
							world.spawnEntityAndPassengers(phantomEntity);
						}
						phantomsSpawned += amountOfPhantoms;
					}
				}
			}
		}

		return phantomsSpawned;
	}
}
