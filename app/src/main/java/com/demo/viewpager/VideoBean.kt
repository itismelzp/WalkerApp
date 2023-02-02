package com.demo.viewpager

data class VideoBean(
    var thumb: Int, // 封面缩略图
    var videoPath: String, // 视频路径
    @MediaType var mediaType: Int = MediaType.IMAGE // 1-图片，2-视频
)


