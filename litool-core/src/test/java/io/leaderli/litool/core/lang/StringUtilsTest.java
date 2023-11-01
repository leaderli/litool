package io.leaderli.litool.core.lang;

import com.google.gson.Gson;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.text.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

/**
 * @author leaderli
 * @since 2022/6/15
 */
class StringUtilsTest {

    @Test
    void just() {
        Assertions.assertEquals("**1**", StringUtils.just("1", 4, '*'));
        Assertions.assertEquals("  1  ", StringUtils.just("1", 4));
        Assertions.assertEquals("    ", StringUtils.just(null, 4));
        Assertions.assertEquals("12345", StringUtils.just("12345", 4));
    }

    @Test
    void ljust() {

        Assertions.assertEquals("***1", StringUtils.ljust("1", 4, '*'));
        Assertions.assertEquals("   1", StringUtils.ljust("1", 4));
        Assertions.assertEquals("    ", StringUtils.ljust(null, 4));
        Assertions.assertEquals("12345", StringUtils.ljust("12345", 4));
    }

    @Test
    void rjust() {

        Assertions.assertEquals("1***", StringUtils.rjust("1", 4, '*'));
        Assertions.assertEquals("1   ", StringUtils.rjust("1", 4));
        Assertions.assertEquals("    ", StringUtils.rjust(null, 4));
        Assertions.assertEquals("12345", StringUtils.rjust("12345", 4));
    }

    @Test
    void split() {

        Assertions.assertNull(StringUtils.chunk(null, 4));
        Assertions.assertEquals("123", StringUtils.chunk("123", 4));
        Assertions.assertEquals("12 3", StringUtils.chunk("123", 2));
        Assertions.assertEquals("12 34", StringUtils.chunk("1234", 2));
        Assertions.assertEquals("12 34 5", StringUtils.chunk("12345", 2));
    }

    @Test
    void join() {

        Assertions.assertEquals("1 2 3", StringUtils.join(" ", 1, 2, 3));
        Assertions.assertEquals("", StringUtils.join(" ", (Object[]) null));
        Assertions.assertEquals("", StringUtils.join(" ", (Object) null));
        Assertions.assertEquals("1,2", StringUtils.join(null, 1, 2));
        Assertions.assertEquals(",1", StringUtils.join(null, Arrays.asList(null, 1)));
        Assertions.assertEquals(",1", StringUtils.join(",", Arrays.asList(null, 1)));
        Assertions.assertEquals("1,2", StringUtils.join(null, Arrays.asList(1, 2)));
        Assertions.assertEquals("1,2", StringUtils.join(null, Stream.of(1, 2)));
    }

    @SuppressWarnings("all")
    @Test
    void obj2String() {

        FieldsString obj = new FieldsString();
        obj.name = "hello";


        Assertions.assertEquals("{name=hello, age=0}", StringUtils.obj2String(obj));
        Assertions.assertEquals("1", StringUtils.obj2String("1"));
        Assertions.assertEquals("1", StringUtils.obj2String(1));
        Assertions.assertEquals("null", StringUtils.obj2String(null));


    }

    @Test
    void line() {

        try {

            outmock();
        } catch (Exception e) {
            Assertions.assertTrue(StringUtils.localMessageAtLineOfClass(e, Lino.class).contains("Lino$Some.map("));
            Assertions.assertTrue(StringUtils.localMessageAtLineOfClass(e, StringUtilsTest.class).contains("StringUtilsTest.outmock"));
            Assertions.assertFalse(StringUtils.localMessageAtLineOfClass(e, null).isEmpty());
        }
        Lino.of(0).mapIgnoreError(i -> 5 / i, e -> Assertions.assertTrue(StringUtils.localMessageAtLineOfClass(e, Lino.class).contains("Some.mapIgnoreError(")));
        Lino.of(0).mapIgnoreError(i -> 5 / i, e -> Assertions.assertTrue(StringUtils.localMessageAtLineOfPackage(e, Lino.class.getPackage()).contains("Some.mapIgnoreError(")));
        Lino.of(0).mapIgnoreError(i -> 5 / i, e -> {
            String s = StringUtils.localMessageAtLineOfPackage(e, StringUtilsTest.class.getPackage());
            Assertions.assertTrue(s.contains("line$"));
        });

        Assertions.assertEquals("", StringUtils.localMessageAtLineOfClass(null, null));


        Assertions.assertEquals("io.leaderli.litool.core.lang.StringUtilsTest$ZeroStackTraceElementException", StringUtils.localMessageStartWith(new ZeroStackTraceElementException(), ""));
    }

    static class ZeroStackTraceElementException extends RuntimeException {
        @Override
        public StackTraceElement[] getStackTrace() {
            return new StackTraceElement[0];
        }
    }

    @Test
    void compress() {

        String json = "{\n" +
                "    \"l10n.allNumber\": \"全部 ({{number}})\",\n" +
                "    \"l10n.play\": \"开始游戏\",\n" +
                "    \"l10n.games\": \"游戏\",\n" +
                "    \"l10n.optimized\": \"已优化\",\n" +
                "    \"l10n.optimizedNumber\": \"已优化 ({{number}})\",\n" +
                "    \"l10n.favorites\": \"喜爱的项目\",\n" +
                "    \"l10n.favoritesNumber\": \"喜爱项目 ({{number}})\",\n" +
                "    \"l10n.favorite\": \"最喜爱的\",\n" +
                "    \"l10n.anselReadyNumber\": \"Ansel 准备就绪 ({{number}})\",\n" +
                "    \"l10n.removeFavorite\": \"删除喜爱的项目\",\n" +
                "    \"l10n.recent\": \"最近\",\n" +
                "    \"l10n.playtime\": \"游戏时间\",\n" +
                "    \"l10n.tags\": \"标签\",\n" +
                "    \"l10n.showGame\": \"显示游戏\",\n" +
                "    \"l10n.hideGame\": \"隐藏游戏\",\n" +
                "    \"l10n.events\": \"赛事\",\n" +
                "    \"l10n.hints\": \"提示\",\n" +
                "    \"l10n.leaderboards\": \"排行榜\",\n" +
                "    \"l10n.optimization\": \"优化\",\n" +
                "    \"l10n.showHiddenGames\": \"显示隐藏的游戏\",\n" +
                "    \"l10n.hideHiddenGames\": \"不显示隐藏游戏\",\n" +
                "    \"l10n.pluggedIn\": \"接通电源\",\n" +
                "    \"l10n.onBattery\": \"用电池\",\n" +
                "    \"l10n.notOptimized\": \"未优化\",\n" +
                "    \"l10n.revert\": \"恢复\",\n" +
                "    \"l10n.setting\": \"设置\",\n" +
                "    \"l10n.current\": \"当前\",\n" +
                "    \"l10n.optimal\": \"最佳\",\n" +
                "    \"l10n.lastPlayed\": \"最后一次游戏时间： \",\n" +
                "    \"l10n.timeago\": \"{{number}} {{timeUnit}} 之前\",\n" +
                "    \"l10n.yesterday\": \"昨天\",\n" +
                "    \"l10n.monthYear\": \"{{month}} {{year}}\",\n" +
                "    \"l10n.totalPlayed\": \"游戏总时间：{{number}} {{timeUnit}}\",\n" +
                "    \"l10n.usingOptimalSettings\": \"使用最佳设置\",\n" +
                "    \"l10n.usingCustomSettings\": \"正在使用自定义设置\",\n" +
                "    \"l10n.scanForGames\": \"扫描游戏\",\n" +
                "    \"l10n.optimizeAllGames\": \"优化所有游戏\",\n" +
                "    \"l10n.manageTags\": \"管理标签\",\n" +
                "    \"l10n.enterTag\": \"输入标签\",\n" +
                "    \"l10n.secondaryTag\": \"+标签\",\n" +
                "    \"l10n.inGame\": \"游戏内\",\n" +
                "    \"l10n.nvidia\": \"NVIDIA\",\n" +
                "    \"l10n.scanning\": \"正在扫描……\",\n" +
                "    \"l10n.scanFinished\": \"扫描完成\",\n" +
                "    \"l10n.optimizing\": \"正在优化……\",\n" +
                "    \"l10n.optimizeFinished\": \"优化完成\",\n" +
                "    \"l10n.watchNow\": \"现在观看\",\n" +
                "    \"l10n.playLocally\": \"在本机上玩游戏\",\n" +
                "    \"l10n.streamFromPC\": \"从我的家庭版 PC 上进行串流\",\n" +
                "    \"l10n.playOnGeForce\": \"在 GeForce Now 上游戏\",\n" +
                "    \"l10n.doNotOptimize\": \"不优化\",\n" +
                "    \"l10n.resolution\": \"分辨率：\",\n" +
                "    \"l10n.displayMode\": \"显示模式：\",\n" +
                "    \"l10n.optimizeFor\": \"优化：\",\n" +
                "    \"l10n.performance\": \"性能\",\n" +
                "    \"l10n.battery\": \"电池\",\n" +
                "    \"l10n.quality\": \"质量\",\n" +
                "    \"l10n.couldNotRetrieveCustomSettings\": \"无法检索自定义设置。请稍后重试\",\n" +
                "    \"l10n.recommended\": \"{{string}}（推荐）\",\n" +
                "    \"l10n.apply\": \"应用\",\n" +
                "    \"l10n.optimizeRecommended\": \"优化（推荐）\",\n" +
                "    \"l10n.optimizePerformance\": \"优化性能\",\n" +
                "    \"l10n.optimizeQuality\": \"优化质量\",\n" +
                "    \"l10n.reset\": \"重置\",\n" +
                "    \"l10n.dynamicSuperResolutions\": \"Dynamic super resolutions\",\n" +
                "    \"l10n.wideScreen1610\": \"宽屏 (16:10)\",\n" +
                "    \"l10n.wideScreen169\": \"宽屏 (16:9)\",\n" +
                "    \"l10n.standard43\": \"标准 (4:3)\",\n" +
                "    \"l10n.standard54\": \"标准 (5:4)\",\n" +
                "    \"l10n.custom\": \"自定义\",\n" +
                "    \"l10n.customSettings\": \"自定义设置\",\n" +
                "    \"l10n.customSettingsPluggedIn\": \"自定义设置：接通电源\",\n" +
                "    \"l10n.customSettingsOnBattery\": \"自定义设置：用电池\",\n" +
                "    \"l10n.gameInfo\": \"游戏信息\",\n" +
                "    \"l10n.gameIsNotOptimized\": \"游戏未优化\",\n" +
                "    \"l10n.gameIsOptimized\": \"游戏已优化\",\n" +
                "    \"l10n.videos\": \"视频\",\n" +
                "    \"l10n.description\": \"说明\",\n" +
                "    \"l10n.noDescription\": \"无法获取描述\",\n" +
                "    \"l10n.systemRequirement\": \"系统要求\",\n" +
                "    \"l10n.noContent\": \"此处空白\",\n" +
                "    \"l10n.feelingLonelyHere\": \"在这里感觉很孤单……\",\n" +
                "    \"l10n.findAWayToPlay\": \"寻找支持的游戏\",\n" +
                "    \"l10n.gamesAreNotOptimized\": \"游戏未优化\",\n" +
                "    \"l10n.noFavoriteGames\": \"没有喜欢的游戏\",\n" +
                "    \"l10n.gamesAreHidden\": \"游戏已被隐藏\",\n" +
                "    \"l10n.noImagesAvailable\": \"无法获取图像\",\n" +
                "    \"l10n.UnableToLoadImages\": \"无法加载图像\",\n" +
                "    \"l10n.UnableToRetrieveCurrentSettings\": \"无法检索当前的游戏设置。启动然后关闭游戏，或者编辑游戏内设置，应该可以修复此问题。\",\n" +
                "    \"l10n.gameIsUsingCustomSettings\": \"游戏正在使用自定义设置\",\n" +
                "    \"l10n.gameIsUsingCustomBatterySettings\": \"游戏正在使用电池模式的自定义设置\",\n" +
                "    \"l10n.gameIsOptimizedForBattery\": \"游戏已为电池模式优化设置\",\n" +
                "    \"l10n.gameIsUsingPluggedInForBattery\": \"游戏并未优化电池设置，且正在使用交流电设置\",\n" +
                "    \"l10n.systemBelowMinspecForGame\": \"您的系统不满足此游戏最佳设置的最低规格。最佳设置可能会导致无法运行的帧率\",\n" +
                "    \"l10n.gameCannotBeOptimized\": \"游戏无法优化\",\n" +
                "    \"l10n.markFavorite\": \"标记喜爱的项目\",\n" +
                "    \"l10n.unableToRetrieveSettings\": \"无法检索设置。请稍后重试。\",\n" +
                "    \"l10n.systemDoesNotSupportOps\": \"GeForce Experience 无法优化此 PC 上的游戏\",\n" +
                "    \"l10n.needToMeetSystemRequirement\": \"请确保您满足\",\n" +
                "    \"l10n.betterQuality\": \"更高质量\",\n" +
                "    \"l10n.tileView\": \"平铺视图\",\n" +
                "    \"l10n.detailsView\": \"详情视图\",\n" +
                "    \"l10n.usingBatteryBoostSettings\": \"使用 BatteryBoost 设置\",\n" +
                "    \"l10n.usingCustomBatterySettings\": \"正在使用自定义电池设置\",\n" +
                "    \"l10n.whenOnBattery\": \"使用电池时\",\n" +
                "    \"l10n.whenPluggedIn\": \"接通电源时\",\n" +
                "    \"l10n.dsr\": \"DSR\",\n" +
                "    \"l10n.dsrInfo\": \"游戏将以高分辨率渲染，然后缩放至监视器的原始分辨率，从而提高图像质量。\",\n" +
                "    \"l10n.unableToUpdateGameSettings\": \"无法更新游戏设置。\",\n" +
                "    \"l10n.scanningFailed\": \"扫描失败\",\n" +
                "    \"l10n.unableToReachNvidiaServers\": \"无法连接到 NVIDIA 服务器。\",\n" +
                "    \"l10n.viewHiddenGames\": \"查看隐藏的游戏\",\n" +
                "    \"l10n.viewAllGames\": \"查看所有游戏\",\n" +
                "    \"l10n.noHiddenGames\": \"没有隐藏的游戏\",\n" +
                "    \"l10n.hiddenNumber\": \"隐藏 ({{number}})\",\n" +
                "    \"l10n.gamesYouPlay\": \"此处显示您玩的游戏\",\n" +
                "    \"l10n.gamesAppearHere\": \"此处显示您的游戏\",\n" +
                "    \"l10n.gameIsOptimizedForQuietMode\": \"游戏已针对 WhisperMode 进行优化\",\n" +
                "    \"l10n.usingOptimalSettingsForQuietMode\": \"使用 WhisperMode 最佳设置\",\n" +
                "    \"l10n.gameIsNotOptimizedForQuietMode\": \"游戏未针对 WhisperMode 进行优化\",\n" +
                "    \"l10n.customSettingsQuietMode\": \"自定义设置：WHISPER MODE\",\n" +
                "    \"l10n.usingCustomSettingsQuietMode\": \"使用 WhisperMode 自定义设置\",\n" +
                "    \"l10n.gameIsUsingCustomSettingsForQuietMode\": \"游戏已针对 WhisperMode 进行自定义设置\",\n" +
                "    \"l10n.noAnselReadyGames\": \"无 Ansel 准备就绪的游戏\",\n" +
                "    \"l10n.frameRateWhisperMode\": \"帧速率：WHISPER MODE\",\n" +
                "    \"l10n.frameRateBatteryBoost\": \"帧速率：电池升压\",\n" +
                "    \"l10n.quietModeFps\": \"WHISPER MODE 每秒帧数\",\n" +
                "    \"l10n.batteryBoostFps\": \"电池升压每秒帧数\",\n" +
                "    \"l10n.fpsCount\": \"{{number}}每秒帧数\",\n" +
                "    \"l10n.couldNotRetrieveWMFps\": \"无法检索 WHISPER MODE 每秒帧数\",\n" +
                "    \"l10n.couldNotRetrieveBBFps\": \"无法检索电池升压每秒帧数\",\n" +
                "    \"l10n.recommendedFps\": \"推荐\",\n" +
                "    \"l10n.freestyleReadyNumber\": \"Freestyle 就绪 ({{number}})\",\n" +
                "    \"l10n.noFreestyleReadyGames\": \"无 Freestyle 就绪游戏\",\n" +
                "    \"l10n.playCloud\": \"在云端游戏\",\n" +
                "    \"l10n.gameIsOptimizedForPerformanceMode\": \"游戏已针对性能进行优化\",\n" +
                "    \"l10n.usingOptimalSettingsForPerformanceMode\": \"使用性能最佳设置\",\n" +
                "    \"l10n.gameIsNotOptimizedForPerformanceMode\": \"游戏无法针对性能进行优化\",\n" +
                "    \"l10n.customSettingsPerformanceMode\": \"自定义设置：性能模式\",\n" +
                "    \"l10n.usingCustomSettingsPerformanceMode\": \"使用性能自定义设置\",\n" +
                "    \"l10n.gameIsUsingCustomSettingsForPerformanceMode\": \"游戏已针对性能进行定制\"\n" +
                "}";

        String compress = StringUtils.compress(json);

        Assertions.assertTrue(json.getBytes().length > compress.getBytes().length);

        String decompress = StringUtils.decompress(compress);

        Assertions.assertFalse(new Gson().fromJson(decompress, Map.class).isEmpty());

    }

    private void outmock() {
        try {

            mock();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void mock() {

        try {

            Lino.of(0).map(i -> new Random().nextInt(i));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static class FieldsString {

        public static int size = 0;
        private String name;
        private int age;

    }
}
