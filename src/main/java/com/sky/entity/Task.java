package com.sky.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 任务字段
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    //id
    private Long Id;

    //任务名称
    private String taskName;

    //任务介绍

    private String introduction;

    //任务类型
    private Integer Level;

    //任务需要存储、发送的文件路径
    private String Content;

    //任务补充
    private String taskLimit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //任务开始时间
    private LocalDateTime taskBeginTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //任务结束时间
    private LocalDateTime taskOverTime;

    @JsonIgnore
    private Double latitude;

    @JsonIgnore
    private Double longitude;

    @JsonIgnore
    private Integer readingDuration;




    /**
     * 解析任务介绍字段，根据任务级别提取不同的信息
     * @param introduction 任务介绍字段
     */
    public void parseIntroduction(String introduction, int Level) {
        if (introduction != null && !introduction.isEmpty()) {
            String[] parts = introduction.split(" ");
            for (String part : parts) {
                if (Level == 101 || Level == 201) { // 如果任务级别是101或201，提取经纬度信息
                    if (part.startsWith("经度:")) {
                        String longitudeStr = part.substring(3); // 去除 "经度:" 部分，获取经度值
                        try {
                            this.longitude = Double.parseDouble(longitudeStr);
                        } catch (NumberFormatException e) {
                            // 处理解析错误
                            System.err.println("Error parsing longitude: " + longitudeStr);
                        }
                    } else if (part.startsWith("纬度:")) {
                        String latitudeStr = part.substring(3); // 去除 "纬度:" 部分，获取纬度值
                        try {
                            this.latitude = Double.parseDouble(latitudeStr);
                        } catch (NumberFormatException e) {
                            // 处理解析错误
                            System.err.println("Error parsing latitude: " + latitudeStr);
                        }
                    }
                } else if (Level == 102 || Level == 202) { // 如果任务级别是102或202，提取阅读时长信息
                    if (part.startsWith("阅读时间:")) {
                        String durationStr = part.substring(5); // 去除 "阅读时间:" 部分，获取阅读时长值
                        // 判断是否包含"分钟"，若包含则去掉
                        if (durationStr.endsWith("分钟")) {
                            durationStr = durationStr.substring(0, durationStr.length() - 2);
                        }
                        try {
                            this.readingDuration = Integer.parseInt(durationStr);
                        } catch (NumberFormatException e) {
                            // 处理解析错误
                            System.err.println("Error parsing reading duration: " + durationStr);
                        }
                    }
                }
            }
        }
    }

}



