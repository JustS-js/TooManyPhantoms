package net.just_s.tmp;

import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

public class Config {
    private final File file = FabricLoader.getInstance().getConfigDir().resolve("TooMuchPhantoms.conf").toFile();

    // Спаунятся ли фантомы после отсутствия сна (примечание: игровое правило не работает)
    public boolean doInsomnia = true;

    // Через сколько секунд начинают спауниться фантомы из-за бессонницы (Ванилла: 3600)
    public int insomniaSpawnStartTimer = 3600;

    // Минимальное время в секундах между спаунами фантомов (Ванилла: 60)
    public int insomniaMinCycleTime = 90;

    // Максимальное случайное время в секундах, добавляемое к минимальному времени между спаунами (Ванилла: 60)
    public int insomniaRandomizationTime = 80;

    // Уровень освещения, при котором фантомы не спаунятся (Ванилла: 999 (отключено))
    public int insomniaLightStopsPhantoms = 15;

    // Радиус вокруг точки спауна мира, где фантомы не появляются (Ванилла: 0 (отключено))
    public int phantomFreeArea = 0;

    // Лимиты количества фантомов в одной группе при спауне
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
            writer.write("# Спаунятся ли фантомы после отсутствия сна (примечание: игровое правило не работает)\n");
            writer.write("doInsomnia=" + doInsomnia + "\n\n");

            writer.write("# Через сколько секунд начинают спауниться фантомы из-за бессонницы (Ванилла: 3600)\n");
            writer.write("insomniaSpawnStartTimer=" + insomniaSpawnStartTimer + "\n\n");

            writer.write("# Минимальное время в секундах между спаунами фантомов (Ванилла: 60)\n");
            writer.write("insomniaMinCycleTime=" + insomniaMinCycleTime + "\n\n");

            writer.write("# Максимальное случайное время в секундах, добавляемое к минимальному времени между спаунами (Ванилла: 60)\n");
            writer.write("insomniaRandomizationTime=" + insomniaRandomizationTime + "\n\n");

            writer.write("# Уровень освещения, при котором фантомы не спаунятся (Ванилла: 999 (отключено))\n");
            writer.write("insomniaLightStopsPhantoms=" + insomniaLightStopsPhantoms + "\n\n");

            writer.write("# Радиус вокруг точки спауна мира, где фантомы не появляются (Ванилла: 0 (отключено))\n");
            writer.write("phantomFreeArea=" + phantomFreeArea + "\n\n");

            writer.write("# Лимиты количества фантомов в одной группе при спауне\n");
            writer.write("minAmountPerSpawn=" + minAmountPerSpawn + "\n");
            writer.write("maxAmountPerSpawn=" + maxAmountPerSpawn);
            writer.close();
            TMPMod.LOGGER.info("TMP Config file created with path: " + file.getAbsolutePath());
        } catch (IOException e) {
            TMPMod.LOGGER.error("Error on Config.dump() > " + e.getMessage());
        }
    }
}