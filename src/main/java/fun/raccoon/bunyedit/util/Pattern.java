package fun.raccoon.bunyedit.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import fun.raccoon.bunyedit.BunyEdit;
import fun.raccoon.bunyedit.data.BlockData;
import net.minecraft.core.util.collection.Pair;

public class Pattern {
    private static Function<BlockData, BlockData> subPattern(String patternStr) {
        BlockData blockData_ = BlockData.fromString(patternStr);
        if (blockData_ == null)
            return null;
        return blockData -> blockData_;
    }

    public static Function<BlockData, BlockData> fromString(String patternArg) {
        List<Pair<Integer, Function<BlockData, BlockData>>> subPatterns = new ArrayList<>();
        String[] patternStrings = patternArg.split("/");

        int totalWeight = 0;

        for (String patternString : patternStrings) {
            String[] weightPatternParts = patternString.split("\\*");

            BunyEdit.LOGGER.info(String.format("len %s", weightPatternParts.length));

            int weight;
            String patternStr;
            switch (weightPatternParts.length) {
                case 2:
                    try {
                        weight = Integer.parseInt(weightPatternParts[0]);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                    patternStr = weightPatternParts[1];
                    break;
                case 1:
                    weight = 1;
                    patternStr = weightPatternParts[0];
                    break;
                default:
                    return null;
            }

            BunyEdit.LOGGER.info(String.format("%s %s", weight, patternStr));

            totalWeight += weight;

            Function<BlockData, BlockData> subPattern = subPattern(patternStr);
            if (subPattern == null)
                return null;

            subPatterns.add(Pair.of(weight, subPattern));
        }

        int totalWeight_ = totalWeight;
        return blockData -> {
            int rand = new Random().nextInt(totalWeight_+1);
            for (Pair<Integer, Function<BlockData, BlockData>> pair : subPatterns) {
                rand -= pair.getLeft();
                if (rand <= 0)
                    return pair.getRight().apply(blockData);
            }
            BunyEdit.LOGGER.info("wah!!!");
            return null;
        };
    }
}
