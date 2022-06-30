package net.just_s.tmp;

import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

public class Config {
    private final File file = FabricLoader.getInstance().getConfigDir().resolve("TooMuchPhantoms.conf").toFile();

    // Whether phantoms spawn after no sleep (note: gamerule does nothing)
    public boolean doInsomnia = true;

    // How many seconds until phantoms should start spawning based on insomnia (Vanilla: 3600)
    public int insomniaSpawnStartTimer = 3600;

    // Min seconds after phantoms have spawned to try to spawn more (Vanilla: 60)
    public int insomniaMinCycleTime = 90;

    // Max seconds to randomly add from the min spawn cycle time (Vanilla: 60)
    public int insomniaRandomizationTime = 80;

    // Light level where the player is standing that phantom spawning (Vanilla: 999 (disabled))
    public int insomniaLightStopsPhantoms = 15;

    // Radius from world spawn where no phantoms would spawn (Vanilla: 0 (disabled))
    public int phantomFreeArea = 0;

    // Limits for amount of phantoms spawning per group
    public int minAmountPerSpawn = 1;
    public int maxAmountPerSpawn = 3;

    public void load() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line = br.readLine().replaceAll(" ", "");
            while (line != null) {
                if (!line.startsWith("#")) {
                    if (line.contains("=")) {
                        line = line.replaceAll(" ", "");
                        String key = line.substring(0, line.indexOf("="));
                        String value = line.substring(line.indexOf("=") + 1);
                        if (value.contains("#")) {value = value.substring(0, value.indexOf("#"));}

                        switch (key) {
                            case "doInsomnia" -> doInsomnia = Boolean.valueOf(value);
                            case "insomniaSpawnStartTimer" -> insomniaSpawnStartTimer = Integer.parseInt(value);
                            case "insomniaMinCycleTime" -> insomniaMinCycleTime = Integer.parseInt(value);
                            case "insomniaRandomizationTime" -> insomniaRandomizationTime = Integer.parseInt(value);
                            case "insomniaLightStopsPhantoms" -> insomniaLightStopsPhantoms = Integer.parseInt(value);
                            case "phantomFreeArea" -> phantomFreeArea = Integer.parseInt(value);
                            case "minAmountPerSpawn" -> minAmountPerSpawn = Integer.parseInt(value);
                            case "maxAmountPerSpawn" -> maxAmountPerSpawn = Integer.parseInt(value);
                        }
                    }
                }
                line = br.readLine();
            }
            br.close();
        }  catch (IOException e) {
            TMPMod.LOGGER.warn("Error on Config.load() > " + e.getMessage());
            dump();
        }
    }

    public void dump() {
        try {
            TMPMod.LOGGER.info("Generating brand new .conf file...");
            FileWriter writer = new FileWriter(file);
            writer.write("# Whether phantoms spawn after no sleep (note: gamerule does nothing)\n");
            writer.write("doInsomnia=" + doInsomnia + "\n\n");

            writer.write("# How many seconds until phantoms should start spawning based on insomnia (Vanilla: 3600)\n");
            writer.write("insomniaSpawnStartTimer=" + insomniaSpawnStartTimer + "\n\n");

            writer.write("# Min seconds after phantoms have spawned to try to spawn more (Vanilla: 60)\n");
            writer.write("insomniaMinCycleTime=" + insomniaMinCycleTime + "\n\n");

            writer.write("# Max seconds to randomly add from the min spawn cycle time (Vanilla: 60)\n");
            writer.write("insomniaRandomizationTime=" + insomniaRandomizationTime + "\n\n");

            writer.write("# Light level where the player is standing that phantom spawning (Vanilla: 999 (disabled))\n");
            writer.write("insomniaLightStopsPhantoms=" + insomniaLightStopsPhantoms + "\n\n");

            writer.write("# Radius from world spawn where no phantoms would spawn (Vanilla: 0 (disabled))\n");
            writer.write("phantomFreeArea=" + phantomFreeArea + "\n\n");

            writer.write("# Limits for amount of phantoms spawning per group\n");
            writer.write("minAmountPerSpawn=" + minAmountPerSpawn + "\n");
            writer.write("maxAmountPerSpawn=" + maxAmountPerSpawn);
            writer.close();
            TMPMod.LOGGER.info("TMP Config file created with path: " + file.getAbsolutePath());
        } catch (IOException e) {
            TMPMod.LOGGER.error("Error on Config.dump() > " + e.getMessage());
        }

    }
}
