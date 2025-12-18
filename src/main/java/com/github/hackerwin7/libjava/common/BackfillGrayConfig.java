package com.github.hackerwin7.libjava.common;

import com.github.hackerwin7.libjava.utils.GsonUtils;
import lombok.ToString;
import lombok.Value;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Value
@ToString
public class BackfillGrayConfig {
    //由于当前线上triggerId/backfillId都已经超过10000000，所以这里直接使用10000000作为灰度的分界线
    private static final Long DEFAULT_MAX_BACKFILL_ID = 10000000L;
    public static final BackfillGrayConfig DEFAULT_BACKFILL_GRAY_CONFIG =
            new BackfillGrayConfig(
                    false, true, DEFAULT_MAX_BACKFILL_ID,
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList()
            );
    boolean fullRelease;
    boolean enableGray;
    Long maxBackfillId;
    List<Long> projectIds;
    List<Long> excludedGrayProjectIds;
    List<String> grayUsers;
    List<String> excludedGrayUsers;
    List<Integer> supportTaskTypes;

    public List<Long> getProjectIds() {
        return Optional.ofNullable(projectIds).orElseGet(Collections::emptyList);
    }

    public List<String> getGrayUsers() {
        return Optional.ofNullable(excludedGrayUsers).orElseGet(Collections::emptyList);
    }

    public List<String> getExcludedGrayUsers() {
        return Optional.ofNullable(excludedGrayUsers).orElseGet(Collections::emptyList);
    }

    public List<Long> getExcludedProjectIds() {
        return Optional.ofNullable(excludedGrayProjectIds).orElseGet(Collections::emptyList);
    }

    public Long getMaxBackfillId() {
        return Optional.ofNullable(maxBackfillId).orElse(DEFAULT_MAX_BACKFILL_ID);
    }

    public static void main(String[] args) {
        String backfillStr = "{}";
        BackfillGrayConfig config = GsonUtils.fromJson(backfillStr, BackfillGrayConfig.class);
        System.out.println(config);
        System.out.println(config.maxBackfillId);
    }
}
