package cn.byteboy.demo.jvm.io;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author hongshaochuan
 * @Date 2021/11/9
 */
public class EChartHandler {

    public static final String path =  System.getProperty("user.home") + File.separator + "temp" + File.separator + "code.js";

    // https://echarts.apache.org/examples/zh/editor.html?c=line-simple&lang=js&version=5.2.2
    public static final String template = "option = {\n" +
            "  xAxis: {\n" +
            "    type: 'category',\n" +
            "    data: {0}\n" +
            "  },\n" +
            "  yAxis: {\n" +
            "    type: 'value'\n" +
            "  },\n" +
            "  series: [\n" +
            "    {\n" +
            "      data: {1},\n" +
            "      type: 'line', smooth: true\n" +
            "    }\n" +
            "  ]\n" +
            "};\n";

    public static <T extends Number> void generateCode(SpeedRecorder<T> recorder) {
        List<SpeedRecorder<T>.PeriodResult> resultList = recorder.parse();

        AtomicLong p = new AtomicLong(0);
        String var0 = resultList.stream().map(v -> p.addAndGet(v.getPeriod())).collect(Collectors.toList()).toString();
        String var1 = resultList.stream().map(v -> v.getSum() / milliseconds2Seconds(v.getPeriod()) / 1024d / 1024d).collect(Collectors.toList()).toString();
        String code = template.replace("{0}", var0).replace("{1}", var1);
        FileUtil.writeString(code, path, StandardCharsets.UTF_8);
    }

    private static double milliseconds2Seconds(long ms) {
        return ms / 1000d;
    }

}
