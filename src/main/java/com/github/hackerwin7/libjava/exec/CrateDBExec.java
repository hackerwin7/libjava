package com.github.hackerwin7.libjava.exec;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CrateDBExec {

//    private static final long[] ADVID = {
//            1642912232584332L,
//            1661211542362126L,
//            1654967947338766L,
//            1651774077493252L
//    };
//
//    public static void main(String[] args) throws Exception {
//        Connection conn = DriverManager.getConnection("crate://10.8.176.98:4200/");
//        for (long advid : ADVID) {
//            PreparedStatement stmt = conn.prepareStatement(String.format(
//                    "select app_code,image_mode,material_id,stat_time_day," +
//                            "sum(form_click_button),sum(play_75_feed_break),sum(stat_cost),sum(next_day_open),sum(dy_comment),sum(in_app_cart),sum(play_25_feed_break),sum(dy_home_visited),sum(consult_effective),sum(view),sum(location_click),sum(phone_confirm),sum(ies_music_click),sum(wifi_play),sum(shopping),sum(qq),sum(play_50_feed_break),sum(active),sum(game_addiction),sum(play_duration_10s),sum(download_start),sum(convert_cnt),sum(phone),sum(active_pay),sum(phone_effective),sum(in_app_pay),sum(ies_challenge_click),sum(total_play),sum(play_duration_2s),sum(play_99_feed_break),sum(button),sum(play_duration_sum),sum(click_cnt),sum(dy_like),sum(click_start_cnt),sum(consult),sum(dy_share),sum(map),sum(vote),sum(valid_play),sum(redirect),sum(cost),sum(phone_connect),sum(coupon),sum(play_duration_3s),sum(wechat),sum(coupon_single_page),sum(download_finish_cnt),sum(lottery),sum(coupon_addition),sum(play_over),sum(in_app_order),sum(install_finish_cnt),sum(message),sum(click_counsel),sum(in_app_detail_uv),sum(dy_follow),sum(play_duration_cnt),sum(form),sum(click_call_cnt),sum(show_cnt),sum(active_register),sum(in_app_uv) " +
//                            "from material_statistics_report_test_1 " +
//                            "where advertiser_id = %d " +
//                            "group by app_code, material_id, image_mode, stat_time_day", advid));
//            long start = System.currentTimeMillis();
//            ResultSet rs = stmt.executeQuery();
//            long elapsed = System.currentTimeMillis() - start;
//            rs.last();
//            System.out.println(advid + " elapsed: " + elapsed + ", rows: " + rs.getRow());
//        }
//
//    }
}
