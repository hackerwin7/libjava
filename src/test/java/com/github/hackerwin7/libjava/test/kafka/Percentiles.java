package com.github.hackerwin7.libjava.test.kafka;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.common.metrics.CompoundStat;
import org.apache.kafka.common.metrics.Measurable;
import org.apache.kafka.common.metrics.MetricConfig;
import org.apache.kafka.common.metrics.stats.Histogram;
import org.apache.kafka.common.metrics.stats.Percentile;
import org.apache.kafka.common.metrics.stats.SampledStat;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2018/03/16
 * Time: 4:54 PM
 * Desc:
 */
public class Percentiles extends SampledStat implements CompoundStat {
    public enum BucketSizing {
        CONSTANT, LINEAR
    }

    private final int buckets;
    private final Percentile[] percentiles;
    private final Histogram.BinScheme binScheme;

    public Percentiles(int sizeInBytes, double max, org.apache.kafka.common.metrics.stats.Percentiles.BucketSizing bucketing, Percentile... percentiles) {
        this(sizeInBytes, 0.0, max, bucketing, percentiles);
    }

    public Percentiles(int sizeInBytes, double min, double max, org.apache.kafka.common.metrics.stats.Percentiles.BucketSizing bucketing, Percentile... percentiles) {
        super(0.0);
        this.percentiles = percentiles;
        this.buckets = sizeInBytes / 4;
        if (bucketing == org.apache.kafka.common.metrics.stats.Percentiles.BucketSizing.CONSTANT) {
            this.binScheme = new Histogram.ConstantBinScheme(buckets, min, max);
        } else if (bucketing == org.apache.kafka.common.metrics.stats.Percentiles.BucketSizing.LINEAR) {
            if (min != 0.0d)
                throw new IllegalArgumentException("Linear bucket sizing requires min to be 0.0.");
            this.binScheme = new Histogram.LinearBinScheme(buckets, max);
        } else {
            throw new IllegalArgumentException("Unknown bucket type: " + bucketing);
        }
    }

    @Override
    public List<CompoundStat.NamedMeasurable> stats() {
        List<CompoundStat.NamedMeasurable> ms = new ArrayList<CompoundStat.NamedMeasurable>(this.percentiles.length);
        for (Percentile percentile : this.percentiles) {
            final double pct = percentile.percentile();
            ms.add(new CompoundStat.NamedMeasurable(percentile.name(), new Measurable() {
                public double measure(MetricConfig config, long now) {
                    return value(config, now, pct / 100.0);
                }
            }));
        }
        return ms;
    }

    public double value(MetricConfig config, long now, double quantile) {
        purgeObsoleteSamples(config, now);
        float count = 0.0f;
        for (SampledStat.Sample sample : this.samples)
            count += sample.eventCount;
        if (count == 0.0f)
            return Double.NaN;
        float sum = 0.0f;
        float quant = (float) quantile;
        for (int b = 0; b < buckets; b++) {
            for (int s = 0; s < this.samples.size(); s++) {
                HistogramSample sample = (HistogramSample) this.samples.get(s);
                float[] hist = sample.histogram.counts();
                sum += hist[b];
                if (sum / count > quant)
                    return binScheme.fromBin(b);
            }
        }
        return Double.POSITIVE_INFINITY;
    }

    public double combine(List<SampledStat.Sample> samples, MetricConfig config, long now) {
        return value(config, now, 0.5);
    }

    @Override
    protected HistogramSample newSample(long timeMs) {
        return new HistogramSample(this.binScheme, timeMs);
    }

    @Override
    protected void update(SampledStat.Sample sample, MetricConfig config, double value, long timeMs) {
        HistogramSample hist = (HistogramSample) sample;
        hist.histogram.record(value);
    }

    private static class HistogramSample extends SampledStat.Sample {
        private final Histogram histogram;

        private HistogramSample(Histogram.BinScheme scheme, long now) {
            super(0.0, now);
            this.histogram = new Histogram(scheme);
        }

        @Override
        public void reset(long now) {
            super.reset(now);
            this.histogram.clear();
        }
    }
}
